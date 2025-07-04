package com.zeronote.accounting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.zeronote.accounting.model.TransactionCategory;
import com.zeronote.accounting.model.TransactionScenario;
import com.zeronote.accounting.model.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AI分析服务
 * 负责智能分类、场景识别和交易分析
 */
@Service
public class AiAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiAnalysisService.class);
    
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;
    
    @Value("${ai.openai.model:gpt-3.5-turbo}")
    private String model;
    
    public AiAnalysisService(@Value("${ai.openai.api-key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 分析交易并返回智能分类结果
     */
    public AiAnalysisResult analyzeTransaction(BigDecimal amount, String description, 
                                             String merchant, String location) {
        try {
            String prompt = buildAnalysisPrompt(amount, description, merchant, location);
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(List.of(new ChatMessage("user", prompt)))
                    .temperature(0.1)
                    .build();
            
            String response = openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
            
            return parseAnalysisResult(response);
            
        } catch (Exception e) {
            logger.error("AI分析失败", e);
            return getDefaultAnalysis(amount);
        }
    }
    
    /**
     * 构建分析提示词
     */
    private String buildAnalysisPrompt(BigDecimal amount, String description, 
                                     String merchant, String location) {
        return String.format("""
                请分析以下交易信息，并返回JSON格式的分析结果：
                
                金额: %s
                描述: %s
                商户: %s
                位置: %s
                时间: %s
                
                请根据以下规则进行分析：
                1. 交易类型：EXPENSE(支出)、INCOME(收入)、TRANSFER(转账)
                2. 交易分类：从预定义分类中选择最合适的
                3. 交易场景：识别特殊场景如报销、退款、订阅等
                4. 商户名称：提取或推断商户名称
                5. 描述：生成简洁的描述
                
                预定义分类：
                - FOOD_DINING(餐饮)、COFFEE_TEA(咖啡茶饮)、SNACKS(零食小吃)
                - TRANSPORTATION(交通)、PUBLIC_TRANSPORT(公共交通)、TAXI_RIDESHARE(打车)
                - SHOPPING(购物)、CLOTHING(服装)、ELECTRONICS(电子产品)
                - ENTERTAINMENT(娱乐)、MOVIES(电影)、GAMES(游戏)
                - UTILITIES(水电费)、RENT(房租)、HEALTHCARE(医疗)
                - WORK_EXPENSES(工作支出)、REIMBURSEMENT(报销)
                - OTHER(其他)、REFUND(退款)
                
                场景类型：
                - REGULAR(常规交易)、REIMBURSEMENT(报销)、REFUND(退款)
                - SUBSCRIPTION(订阅)、RECURRING(定期交易)、GIFT(礼物)
                
                返回JSON格式：
                {
                    "type": "EXPENSE",
                    "category": "FOOD_DINING",
                    "scenario": "REGULAR",
                    "merchant": "商户名称",
                    "description": "交易描述",
                    "analysis": "AI分析说明"
                }
                """, 
                amount, 
                description != null ? description : "", 
                merchant != null ? merchant : "", 
                location != null ? location : "",
                LocalDateTime.now());
    }
    
    /**
     * 解析AI分析结果
     */
    private AiAnalysisResult parseAnalysisResult(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            
            return AiAnalysisResult.builder()
                    .type(TransactionType.valueOf(jsonNode.get("type").asText()))
                    .category(TransactionCategory.valueOf(jsonNode.get("category").asText()))
                    .scenario(TransactionScenario.valueOf(jsonNode.get("scenario").asText()))
                    .merchant(jsonNode.get("merchant").asText())
                    .description(jsonNode.get("description").asText())
                    .analysis(jsonNode.get("analysis").asText())
                    .build();
                    
        } catch (Exception e) {
            logger.error("解析AI分析结果失败", e);
            return getDefaultAnalysis(null);
        }
    }
    
    /**
     * 获取默认分析结果
     */
    private AiAnalysisResult getDefaultAnalysis(BigDecimal amount) {
        return AiAnalysisResult.builder()
                .type(TransactionType.EXPENSE)
                .category(TransactionCategory.OTHER)
                .scenario(TransactionScenario.REGULAR)
                .merchant("未知商户")
                .description("交易")
                .analysis("AI分析暂时不可用，使用默认分类")
                .build();
    }
    
    /**
     * AI分析结果内部类
     */
    public static class AiAnalysisResult {
        private TransactionType type;
        private TransactionCategory category;
        private TransactionScenario scenario;
        private String merchant;
        private String description;
        private String analysis;
        
        // Builder模式
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private AiAnalysisResult result = new AiAnalysisResult();
            
            public Builder type(TransactionType type) {
                result.type = type;
                return this;
            }
            
            public Builder category(TransactionCategory category) {
                result.category = category;
                return this;
            }
            
            public Builder scenario(TransactionScenario scenario) {
                result.scenario = scenario;
                return this;
            }
            
            public Builder merchant(String merchant) {
                result.merchant = merchant;
                return this;
            }
            
            public Builder description(String description) {
                result.description = description;
                return this;
            }
            
            public Builder analysis(String analysis) {
                result.analysis = analysis;
                return this;
            }
            
            public AiAnalysisResult build() {
                return result;
            }
        }
        
        // Getter方法
        public TransactionType getType() { return type; }
        public TransactionCategory getCategory() { return category; }
        public TransactionScenario getScenario() { return scenario; }
        public String getMerchant() { return merchant; }
        public String getDescription() { return description; }
        public String getAnalysis() { return analysis; }
    }
} 