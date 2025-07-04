package com.zeronote.accounting.model;

/**
 * 交易类型枚举
 */
public enum TransactionType {
    EXPENSE("支出"),
    INCOME("收入"),
    TRANSFER("转账");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 