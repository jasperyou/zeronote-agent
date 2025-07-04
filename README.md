# ZeroNoteAgent

基于AI的自动化个人财务管理系统，实现最小化用户输入的智能记账体验。

## 🚀 核心特性

- **智能交易分类**: 基于AI自动识别交易类型和分类
- **最小化输入**: 用户只需输入金额，其他信息由AI推断
- **隐私优先**: 本地数据存储，端到端加密
- **场景识别**: 自动识别报销、退款、订阅等特殊场景
- **实时分析**: 提供消费趋势和财务洞察

## 🛠️ 技术栈

- **后端**: Spring Boot 3.2.0, Java 21
- **数据库**: H2 (开发环境)
- **AI集成**: OpenAI GPT-3.5-turbo
- **安全**: Spring Security, BCrypt加密
- **API**: RESTful API设计

## 📋 快速开始

### 环境要求

- Java 21+
- Maven 3.9+
- OpenAI API Key

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd ai-simple-accounting
```

2. **配置环境变量**
```bash
# Windows
set OPENAI_API_KEY=your-openai-api-key-here

# Linux/macOS
export OPENAI_API_KEY=your-openai-api-key-here
```

3. **编译运行**
```bash
mvn clean compile
mvn spring-boot:run
```

4. **访问应用**
- API文档: http://localhost:8080/api/transactions/health
- H2数据库控制台: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/accounting`
  - 用户名: `sa`
  - 密码: (留空)

## 📚 API使用指南

### 快速记账（推荐）

只需输入金额，AI自动分类：

```bash
curl -X POST "http://localhost:8080/api/transactions/quick?amount=25.50"
```

### 完整记账

提供更多信息以获得更准确的分类：

```bash
curl -X POST "http://localhost:8080/api/transactions" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 25.50,
    "description": "午餐",
    "merchant": "星巴克",
    "location": "北京朝阳区"
  }'
```

### 查询交易

```bash
# 获取所有交易
curl "http://localhost:8080/api/transactions"

# 获取最近的交易
curl "http://localhost:8080/api/transactions/recent"

# 按分类查询
curl "http://localhost:8080/api/transactions/category/FOOD_DINING"

# 搜索交易
curl "http://localhost:8080/api/transactions/search?keyword=星巴克"
```

### 统计分析

```bash
# 获取本月统计
curl "http://localhost:8080/api/transactions/statistics?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59"
```

## 🏗️ 项目结构

```
src/main/java/com/zeronote/accounting/
├── AiSimpleAccountingApplication.java    # 主应用类
├── config/
│   └── SecurityConfig.java              # 安全配置
├── controller/
│   └── TransactionController.java       # REST API控制器
├── dto/
│   ├── TransactionRequest.java          # 请求DTO
│   └── TransactionResponse.java         # 响应DTO
├── model/
│   ├── Transaction.java                 # 交易实体
│   ├── TransactionType.java             # 交易类型枚举
│   ├── TransactionCategory.java         # 交易分类枚举
│   └── TransactionScenario.java         # 交易场景枚举
├── repository/
│   └── TransactionRepository.java       # 数据访问层
└── service/
    ├── AiAnalysisService.java           # AI分析服务
    └── TransactionService.java          # 交易业务服务
```

## 🎯 核心功能详解

### 1. 智能分类系统

系统使用OpenAI GPT-3.5-turbo模型分析交易信息：

- **交易类型**: 支出、收入、转账
- **交易分类**: 餐饮、交通、购物、娱乐等20+分类
- **场景识别**: 报销、退款、订阅、定期交易等

### 2. 最小化输入设计

- **快速记账**: 只需输入金额
- **智能推断**: 基于金额、时间、位置等信息自动分类
- **上下文学习**: 根据历史交易模式优化分类准确性

### 3. 隐私保护

- **本地存储**: 数据存储在本地H2数据库
- **端到端加密**: 敏感数据加密存储
- **零信任架构**: 最小化数据传输

## 🔧 配置说明

### 应用配置 (application.yml)

```yaml
# AI配置
ai:
  openai:
    api-key: ${OPENAI_API_KEY}
    model: gpt-3.5-turbo
    timeout: 30000

# 数据库配置
spring:
  datasource:
    url: jdbc:h2:file:./data/accounting
    username: sa
    password: 

# 应用配置
app:
  enable-ai-analysis: true
  default-page-size: 20
```

### 环境变量

| 变量名 | 说明 | 必需 |
|--------|------|------|
| `OPENAI_API_KEY` | OpenAI API密钥 | 是 |
| `ENCRYPTION_KEY` | 数据加密密钥 | 否 |

## 🧪 测试

### 单元测试

```bash
mvn test
```

### 集成测试

```bash
mvn verify
```

### API测试示例

```bash
# 测试快速记账
curl -X POST "http://localhost:8080/api/transactions/quick?amount=15.80"

# 测试完整记账
curl -X POST "http://localhost:8080/api/transactions" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 89.99,
    "description": "购买耳机",
    "merchant": "京东"
  }'

# 查看分类结果
curl "http://localhost:8080/api/transactions/recent"
```

## 🚀 部署

### 开发环境

```bash
mvn spring-boot:run
```

### 生产环境

```bash
# 打包
mvn clean package

# 运行
java -jar target/ai-simple-accounting-0.1.0-SNAPSHOT.jar
```

### Docker部署

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/ai-simple-accounting-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 📄 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 支持

如果您遇到问题或有建议，请：

1. 查看 [Issues](../../issues) 页面
2. 创建新的Issue
3. 联系开发团队

## 🔮 未来规划

- [ ] 移动端应用
- [ ] 多用户支持
- [ ] 数据导入导出
- [ ] 预算管理
- [ ] 财务报告
- [ ] 机器学习优化

---

**AI Simple Accounting** - 让记账变得简单智能！ 💰🤖
