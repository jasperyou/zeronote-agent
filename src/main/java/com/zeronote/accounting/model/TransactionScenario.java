package com.zeronote.accounting.model;

/**
 * 交易场景枚举
 * 用于AI识别特殊交易场景
 */
public enum TransactionScenario {
    REGULAR("常规交易"),
    REIMBURSEMENT("报销"),
    REFUND("退款"),
    SUBSCRIPTION("订阅"),
    RECURRING("定期交易"),
    SPLIT_PAYMENT("分摊支付"),
    GIFT("礼物"),
    BUSINESS_EXPENSE("商务支出"),
    PERSONAL_EXPENSE("个人支出");
    
    private final String displayName;
    
    TransactionScenario(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 