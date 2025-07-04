package com.zeronote.accounting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易实体类
 * 代表用户的每一笔财务交易
 */
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type = TransactionType.EXPENSE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory category = TransactionCategory.OTHER;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 200)
    private String merchant;
    
    @Column(length = 100)
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionScenario scenario = TransactionScenario.REGULAR;
    
    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(length = 1000)
    private String aiAnalysis;
    
    @Column(length = 50)
    private String source; // 数据来源：手动输入、微信支付、支付宝等
    
    @Column(length = 100)
    private String externalId; // 外部系统ID
    
    // 构造函数
    public Transaction() {}
    
    public Transaction(BigDecimal amount) {
        this.amount = amount;
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    
    public TransactionCategory getCategory() { return category; }
    public void setCategory(TransactionCategory category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public TransactionScenario getScenario() { return scenario; }
    public void setScenario(TransactionScenario scenario) { this.scenario = scenario; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(String aiAnalysis) { this.aiAnalysis = aiAnalysis; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{id=%d, amount=%s, category=%s, description='%s', scenario=%s}",
                id, amount, category, description, scenario);
    }
} 