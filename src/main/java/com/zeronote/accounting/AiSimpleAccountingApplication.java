package com.zeronote.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * AI Simple Accounting 主应用类
 * 
 * 智能记账系统 - 基于AI的自动化个人财务管理
 * 核心特性：
 * - 智能交易分类
 * - 最小化用户输入（只需输入金额）
 * - 隐私优先的本地存储
 * - 场景识别（报销、退款等）
 */
@SpringBootApplication
@EnableAsync
public class AiSimpleAccountingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AiSimpleAccountingApplication.class, args);
    }
} 