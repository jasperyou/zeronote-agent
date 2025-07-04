package com.zeronote.accounting.controller;

import com.zeronote.accounting.dto.TransactionRequest;
import com.zeronote.accounting.dto.TransactionResponse;
import com.zeronote.accounting.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 交易REST API控制器
 * 提供智能记账系统的核心API接口
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    
    private final TransactionService transactionService;
    
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    /**
     * 创建新交易（智能分类）
     * POST /api/transactions
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        logger.info("收到创建交易请求: {}", request);
        
        try {
            TransactionResponse response = transactionService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("创建交易失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 快速创建交易（只需金额）
     * POST /api/transactions/quick
     */
    @PostMapping("/quick")
    public ResponseEntity<TransactionResponse> createQuickTransaction(@RequestParam("amount") String amount) {
        logger.info("收到快速创建交易请求，金额: {}", amount);
        
        try {
            TransactionRequest request = new TransactionRequest();
            request.setAmount(new java.math.BigDecimal(amount));
            request.setTransactionDate(LocalDateTime.now());
            
            TransactionResponse response = transactionService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NumberFormatException e) {
            logger.error("金额格式错误: {}", amount);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("快速创建交易失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取所有交易（分页）
     * GET /api/transactions
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponse> transactions = transactionService.getAllTransactions(pageable);
        
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * 根据ID获取交易
     * GET /api/transactions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        Optional<TransactionResponse> transaction = transactionService.getTransactionById(id);
        
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 获取最近的交易
     * GET /api/transactions/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<TransactionResponse>> getRecentTransactions() {
        List<TransactionResponse> transactions = transactionService.getRecentTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * 根据分类查询交易
     * GET /api/transactions/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByCategory(category, pageable);
        
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * 根据时间范围查询交易
     * GET /api/transactions/date-range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * 搜索交易
     * GET /api/transactions/search
     */
    @GetMapping("/search")
    public ResponseEntity<List<TransactionResponse>> searchTransactions(@RequestParam String keyword) {
        List<TransactionResponse> transactions = transactionService.searchTransactions(keyword);
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * 获取统计信息
     * GET /api/transactions/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<TransactionService.TransactionStatistics> getStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        TransactionService.TransactionStatistics statistics = transactionService.getStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 更新交易
     * PUT /api/transactions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        
        Optional<TransactionResponse> response = transactionService.updateTransaction(id, request);
        
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 删除交易
     * DELETE /api/transactions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        boolean deleted = transactionService.deleteTransaction(id);
        
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    /**
     * 健康检查
     * GET /api/transactions/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AI Simple Accounting API is running!");
    }
} 