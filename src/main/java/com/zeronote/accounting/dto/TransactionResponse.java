package com.zeronote.accounting.dto;

import com.zeronote.accounting.model.TransactionCategory;
import com.zeronote.accounting.model.TransactionScenario;
import com.zeronote.accounting.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易响应DTO
 */
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionCategory category;
    private String description;
    private String merchant;
    private String location;
    private TransactionScenario scenario;
    private LocalDateTime transactionDate;
    private String aiAnalysis;
    private String source;
    private LocalDateTime createdAt;
    
    // 构造函数
    public TransactionResponse() {}
    
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
    
    public String getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(String aiAnalysis) { this.aiAnalysis = aiAnalysis; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("TransactionResponse{id=%d, amount=%s, category=%s, description='%s', scenario=%s}",
                id, amount, category, description, scenario);
    }
} 