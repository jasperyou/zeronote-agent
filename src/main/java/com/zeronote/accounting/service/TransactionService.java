package com.zeronote.accounting.service;

import com.zeronote.accounting.dto.TransactionRequest;
import com.zeronote.accounting.dto.TransactionResponse;
import com.zeronote.accounting.model.Transaction;
import com.zeronote.accounting.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 交易业务服务层
 * 负责交易的核心业务逻辑
 */
@Service
@Transactional
public class TransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    private final TransactionRepository transactionRepository;
    private final AiAnalysisService aiAnalysisService;
    
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, 
                            AiAnalysisService aiAnalysisService) {
        this.transactionRepository = transactionRepository;
        this.aiAnalysisService = aiAnalysisService;
    }
    
    /**
     * 创建新交易（智能分类）
     */
    public TransactionResponse createTransaction(TransactionRequest request) {
        logger.info("创建新交易: {}", request);
        
        // 使用AI分析交易信息
        AiAnalysisService.AiAnalysisResult analysis = aiAnalysisService.analyzeTransaction(
                request.getAmount(),
                request.getDescription(),
                request.getMerchant(),
                request.getLocation()
        );
        
        // 创建交易实体
        Transaction transaction = new Transaction(request.getAmount());
        transaction.setType(analysis.getType());
        transaction.setCategory(analysis.getCategory());
        transaction.setScenario(analysis.getScenario());
        transaction.setDescription(analysis.getDescription());
        transaction.setMerchant(analysis.getMerchant());
        transaction.setLocation(request.getLocation());
        transaction.setTransactionDate(request.getTransactionDate() != null ? 
                request.getTransactionDate() : LocalDateTime.now());
        transaction.setSource(request.getSource() != null ? request.getSource() : "手动输入");
        transaction.setExternalId(request.getExternalId());
        transaction.setAiAnalysis(analysis.getAnalysis());
        
        // 保存交易
        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("交易创建成功: {}", savedTransaction);
        
        return convertToResponse(savedTransaction);
    }
    
    /**
     * 根据ID获取交易
     */
    @Transactional(readOnly = true)
    public Optional<TransactionResponse> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    /**
     * 分页查询所有交易
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAllByOrderByTransactionDateDesc(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * 获取最近的交易
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getRecentTransactions() {
        return transactionRepository.findTop10ByOrderByTransactionDateDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据分类查询交易
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionsByCategory(String category, Pageable pageable) {
        try {
            var categoryEnum = com.zeronote.accounting.model.TransactionCategory.valueOf(category.toUpperCase());
            return transactionRepository.findByCategoryOrderByTransactionDateDesc(categoryEnum, pageable)
                    .map(this::convertToResponse);
        } catch (IllegalArgumentException e) {
            logger.warn("无效的分类: {}", category);
            return Page.empty(pageable);
        }
    }
    
    /**
     * 根据时间范围查询交易
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 搜索交易
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> searchTransactions(String keyword) {
        List<Transaction> transactions = transactionRepository.findByDescriptionContainingIgnoreCase(keyword);
        transactions.addAll(transactionRepository.findByMerchantContainingIgnoreCase(keyword));
        
        return transactions.stream()
                .distinct()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取统计信息
     */
    @Transactional(readOnly = true)
    public TransactionStatistics getStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalExpenses = transactionRepository.sumExpensesByDateRange(startDate, endDate);
        BigDecimal totalIncome = transactionRepository.sumIncomeByDateRange(startDate, endDate);
        
        return TransactionStatistics.builder()
                .totalExpenses(totalExpenses)
                .totalIncome(totalIncome)
                .netAmount(totalIncome.subtract(totalExpenses))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
    
    /**
     * 更新交易
     */
    public Optional<TransactionResponse> updateTransaction(Long id, TransactionRequest request) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    // 更新基本信息
                    transaction.setAmount(request.getAmount());
                    transaction.setDescription(request.getDescription());
                    transaction.setMerchant(request.getMerchant());
                    transaction.setLocation(request.getLocation());
                    transaction.setTransactionDate(request.getTransactionDate());
                    
                    // 重新进行AI分析
                    AiAnalysisService.AiAnalysisResult analysis = aiAnalysisService.analyzeTransaction(
                            request.getAmount(),
                            request.getDescription(),
                            request.getMerchant(),
                            request.getLocation()
                    );
                    
                    transaction.setType(analysis.getType());
                    transaction.setCategory(analysis.getCategory());
                    transaction.setScenario(analysis.getScenario());
                    transaction.setAiAnalysis(analysis.getAnalysis());
                    
                    Transaction savedTransaction = transactionRepository.save(transaction);
                    logger.info("交易更新成功: {}", savedTransaction);
                    
                    return convertToResponse(savedTransaction);
                });
    }
    
    /**
     * 删除交易
     */
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            logger.info("交易删除成功: {}", id);
            return true;
        }
        return false;
    }
    
    /**
     * 转换为响应DTO
     */
    private TransactionResponse convertToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setCategory(transaction.getCategory());
        response.setDescription(transaction.getDescription());
        response.setMerchant(transaction.getMerchant());
        response.setLocation(transaction.getLocation());
        response.setScenario(transaction.getScenario());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setAiAnalysis(transaction.getAiAnalysis());
        response.setSource(transaction.getSource());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
    
    /**
     * 交易统计信息内部类
     */
    public static class TransactionStatistics {
        private BigDecimal totalExpenses;
        private BigDecimal totalIncome;
        private BigDecimal netAmount;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        
        // Builder模式
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private TransactionStatistics stats = new TransactionStatistics();
            
            public Builder totalExpenses(BigDecimal totalExpenses) {
                stats.totalExpenses = totalExpenses;
                return this;
            }
            
            public Builder totalIncome(BigDecimal totalIncome) {
                stats.totalIncome = totalIncome;
                return this;
            }
            
            public Builder netAmount(BigDecimal netAmount) {
                stats.netAmount = netAmount;
                return this;
            }
            
            public Builder startDate(LocalDateTime startDate) {
                stats.startDate = startDate;
                return this;
            }
            
            public Builder endDate(LocalDateTime endDate) {
                stats.endDate = endDate;
                return this;
            }
            
            public TransactionStatistics build() {
                return stats;
            }
        }
        
        // Getter方法
        public BigDecimal getTotalExpenses() { return totalExpenses; }
        public BigDecimal getTotalIncome() { return totalIncome; }
        public BigDecimal getNetAmount() { return netAmount; }
        public LocalDateTime getStartDate() { return startDate; }
        public LocalDateTime getEndDate() { return endDate; }
    }
} 