package com.zeronote.accounting.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易请求DTO
 * 支持最小化输入 - 用户只需输入金额，其他信息由AI推断
 */
public class TransactionRequest {
    
    @NotNull
    @Positive
    private BigDecimal amount;
    
    private String description;
    private String merchant;
    private String location;
    private LocalDateTime transactionDate;
    private String source;
    private String externalId;
    
    // 构造函数
    public TransactionRequest() {}
    
    public TransactionRequest(BigDecimal amount) {
        this.amount = amount;
        this.transactionDate = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    
    @Override
    public String toString() {
        return String.format("TransactionRequest{amount=%s, description='%s', merchant='%s', location='%s'}",
                amount, description, merchant, location);
    }
} 