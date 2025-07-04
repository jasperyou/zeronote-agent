# AI Simple Accounting - 项目结构

## 📁 目录结构

```
ai-simple-accounting/
├── 📄 pom.xml                           # Maven项目配置
├── 📄 README.md                         # 项目说明文档
├── 📄 PROJECT_STRUCTURE.md              # 项目结构说明
├── 📄 run.bat                           # Windows启动脚本
├── 📄 run.sh                            # Linux/macOS启动脚本
├── 📄 test-api.bat                      # Windows API测试脚本
├── 📄 test-api.sh                       # Linux/macOS API测试脚本
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/zeronote/accounting/
│   │   │   ├── 📄 AiSimpleAccountingApplication.java    # 主应用类
│   │   │   │
│   │   │   ├── 📁 config/
│   │   │   │   └── 📄 SecurityConfig.java              # 安全配置
│   │   │   │
│   │   │   ├── 📁 controller/
│   │   │   │   └── 📄 TransactionController.java       # REST API控制器
│   │   │   │
│   │   │   ├── 📁 dto/
│   │   │   │   ├── 📄 TransactionRequest.java          # 请求DTO
│   │   │   │   └── 📄 TransactionResponse.java         # 响应DTO
│   │   │   │
│   │   │   ├── 📁 model/
│   │   │   │   ├── 📄 Transaction.java                 # 交易实体
│   │   │   │   ├── 📄 TransactionType.java             # 交易类型枚举
│   │   │   │   ├── 📄 TransactionCategory.java         # 交易分类枚举
│   │   │   │   └── 📄 TransactionScenario.java         # 交易场景枚举
│   │   │   │
│   │   │   ├── 📁 repository/
│   │   │   │   └── 📄 TransactionRepository.java       # 数据访问层
│   │   │   │
│   │   │   └── 📁 service/
│   │   │       ├── 📄 AiAnalysisService.java           # AI分析服务
│   │   │       └── 📄 TransactionService.java          # 交易业务服务
│   │   │
│   │   └── 📁 resources/
│   │       └── 📄 application.yml                      # 应用配置文件
│   │
│   └── 📁 test/
│       └── 📁 java/com/zeronote/accounting/service/
│           └── 📄 TransactionServiceTest.java          # 单元测试
│
└── 📁 data/                             # 数据目录（运行时创建）
    └── 📄 accounting.mv.db              # H2数据库文件
```

## 🏗️ 架构设计

### 分层架构

```
┌─────────────────────────────────────┐
│           Controller Layer          │  ← REST API接口层
├─────────────────────────────────────┤
│            Service Layer            │  ← 业务逻辑层
├─────────────────────────────────────┤
│          Repository Layer           │  ← 数据访问层
├─────────────────────────────────────┤
│            Model Layer              │  ← 数据模型层
└─────────────────────────────────────┘
```

### 核心组件

#### 1. 控制器层 (Controller)
- **TransactionController**: 提供RESTful API接口
- 支持快速记账、完整记账、查询、统计等功能
- 统一的错误处理和响应格式

#### 2. 服务层 (Service)
- **TransactionService**: 交易业务逻辑处理
- **AiAnalysisService**: AI智能分析服务
- 事务管理和业务规则验证

#### 3. 数据访问层 (Repository)
- **TransactionRepository**: 交易数据访问接口
- 支持复杂查询和统计分析
- 基于Spring Data JPA

#### 4. 模型层 (Model)
- **Transaction**: 交易实体类
- **TransactionType**: 交易类型枚举
- **TransactionCategory**: 交易分类枚举
- **TransactionScenario**: 交易场景枚举

#### 5. DTO层
- **TransactionRequest**: 请求数据传输对象
- **TransactionResponse**: 响应数据传输对象

## 🔧 技术栈详解

### 后端框架
- **Spring Boot 3.2.0**: 主框架
- **Spring Data JPA**: 数据持久化
- **Spring Security**: 安全框架
- **H2 Database**: 嵌入式数据库

### AI集成
- **OpenAI GPT-3.5-turbo**: 智能分析模型
- **JSON解析**: 结构化AI响应
- **错误处理**: AI服务降级机制

### 开发工具
- **Maven**: 项目构建工具
- **JUnit 5**: 单元测试框架
- **Mockito**: 测试模拟框架

## 📊 数据模型

### 交易实体 (Transaction)
```java
@Entity
public class Transaction {
    private Long id;                    // 主键
    private BigDecimal amount;          // 金额
    private TransactionType type;       // 交易类型
    private TransactionCategory category; // 交易分类
    private String description;         // 描述
    private String merchant;            // 商户
    private String location;            // 位置
    private TransactionScenario scenario; // 交易场景
    private LocalDateTime transactionDate; // 交易时间
    private String aiAnalysis;          // AI分析结果
    private String source;              // 数据来源
    private String externalId;          // 外部ID
}
```

### 交易分类 (TransactionCategory)
- **餐饮类**: FOOD_DINING, COFFEE_TEA, SNACKS
- **交通类**: TRANSPORTATION, PUBLIC_TRANSPORT, TAXI_RIDESHARE
- **购物类**: SHOPPING, CLOTHING, ELECTRONICS, BOOKS
- **娱乐类**: ENTERTAINMENT, MOVIES, GAMES, SPORTS
- **生活服务**: UTILITIES, RENT, INSURANCE, HEALTHCARE
- **工作相关**: WORK_EXPENSES, REIMBURSEMENT
- **其他**: OTHER, REFUND, TRANSFER

### 交易场景 (TransactionScenario)
- **REGULAR**: 常规交易
- **REIMBURSEMENT**: 报销
- **REFUND**: 退款
- **SUBSCRIPTION**: 订阅
- **RECURRING**: 定期交易
- **GIFT**: 礼物
- **BUSINESS_EXPENSE**: 商务支出
- **PERSONAL_EXPENSE**: 个人支出

## 🔄 业务流程

### 1. 快速记账流程
```
用户输入金额 → AI分析 → 自动分类 → 保存交易 → 返回结果
```

### 2. 完整记账流程
```
用户输入详细信息 → AI分析 → 智能分类 → 保存交易 → 返回结果
```

### 3. AI分析流程
```
交易信息 → OpenAI API → JSON解析 → 分类结果 → 业务处理
```

## 🛡️ 安全设计

### 数据安全
- **本地存储**: 数据存储在本地H2数据库
- **加密传输**: HTTPS协议
- **访问控制**: Spring Security认证授权

### 隐私保护
- **最小化数据收集**: 只收集必要信息
- **本地处理**: AI分析在本地进行
- **数据隔离**: 用户数据独立存储

## 🧪 测试策略

### 单元测试
- **Service层测试**: 业务逻辑验证
- **Repository层测试**: 数据访问验证
- **Mock测试**: 外部依赖模拟

### 集成测试
- **API测试**: 接口功能验证
- **数据库测试**: 数据持久化验证
- **AI服务测试**: 智能分析验证

## 🚀 部署方案

### 开发环境
```bash
mvn spring-boot:run
```

### 生产环境
```bash
mvn clean package
java -jar target/ai-simple-accounting-0.1.0-SNAPSHOT.jar
```

### Docker部署
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/ai-simple-accounting-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📈 性能优化

### 数据库优化
- **索引设计**: 关键字段建立索引
- **查询优化**: 使用JPA查询优化
- **连接池**: HikariCP连接池配置

### AI服务优化
- **缓存机制**: 相似查询结果缓存
- **异步处理**: 非阻塞AI分析
- **降级策略**: AI服务不可用时的处理

## 🔮 扩展性设计

### 模块化设计
- **服务解耦**: 业务逻辑模块化
- **接口抽象**: 便于功能扩展
- **配置外部化**: 支持不同环境配置

### 未来扩展
- **多用户支持**: 用户认证和权限管理
- **数据导入导出**: 支持多种格式
- **移动端API**: 移动应用支持
- **第三方集成**: 银行、支付平台集成 