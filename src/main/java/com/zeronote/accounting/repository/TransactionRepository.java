package com.zeronote.accounting.repository;

import com.zeronote.accounting.model.Transaction;
import com.zeronote.accounting.model.TransactionCategory;
import com.zeronote.accounting.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易数据访问层
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * 根据类型查询交易
     */
    List<Transaction> findByType(TransactionType type);
    
    /**
     * 根据分类查询交易
     */
    List<Transaction> findByCategory(TransactionCategory category);
    
    /**
     * 根据时间范围查询交易
     */
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据金额范围查询交易
     */
    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    /**
     * 根据商户名称模糊查询
     */
    List<Transaction> findByMerchantContainingIgnoreCase(String merchant);
    
    /**
     * 根据描述模糊查询
     */
    List<Transaction> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * 分页查询所有交易
     */
    Page<Transaction> findAllByOrderByTransactionDateDesc(Pageable pageable);
    
    /**
     * 根据类型分页查询
     */
    Page<Transaction> findByTypeOrderByTransactionDateDesc(TransactionType type, Pageable pageable);
    
    /**
     * 根据分类分页查询
     */
    Page<Transaction> findByCategoryOrderByTransactionDateDesc(TransactionCategory category, Pageable pageable);
    
    /**
     * 统计指定时间范围内的总支出
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 统计指定时间范围内的总收入
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME' AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumIncomeByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 按分类统计支出
     */
    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND t.transactionDate BETWEEN :startDate AND :endDate GROUP BY t.category")
    List<Object[]> sumExpensesByCategory(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找最近的交易
     */
    List<Transaction> findTop10ByOrderByTransactionDateDesc();
    
    /**
     * 根据外部ID查找交易
     */
    Transaction findByExternalId(String externalId);
    
    /**
     * 检查外部ID是否存在
     */
    boolean existsByExternalId(String externalId);
} 