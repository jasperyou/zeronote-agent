package com.zeronote.accounting.model;

/**
 * 交易分类枚举
 * 基于常见消费场景设计
 */
public enum TransactionCategory {
    // 餐饮类
    FOOD_DINING("餐饮"),
    COFFEE_TEA("咖啡茶饮"),
    SNACKS("零食小吃"),
    
    // 交通类
    TRANSPORTATION("交通"),
    PUBLIC_TRANSPORT("公共交通"),
    TAXI_RIDESHARE("打车"),
    FUEL("加油"),
    PARKING("停车"),
    
    // 购物类
    SHOPPING("购物"),
    CLOTHING("服装"),
    ELECTRONICS("电子产品"),
    BOOKS("图书"),
    GROCERIES("日用品"),
    
    // 娱乐类
    ENTERTAINMENT("娱乐"),
    MOVIES("电影"),
    GAMES("游戏"),
    SPORTS("运动"),
    TRAVEL("旅行"),
    
    // 生活服务类
    UTILITIES("水电费"),
    RENT("房租"),
    INSURANCE("保险"),
    HEALTHCARE("医疗"),
    EDUCATION("教育"),
    
    // 工作相关
    WORK_EXPENSES("工作支出"),
    REIMBURSEMENT("报销"),
    
    // 其他
    OTHER("其他"),
    REFUND("退款"),
    TRANSFER("转账");
    
    private final String displayName;
    
    TransactionCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 