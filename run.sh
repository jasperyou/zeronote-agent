#!/bin/bash

echo "========================================"
echo "AI Simple Accounting - 智能记账系统"
echo "========================================"
echo

# 检查Java版本
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java，请确保已安装Java 21+"
    exit 1
fi

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven，请确保已安装Maven 3.9+"
    exit 1
fi

# 检查OpenAI API Key
if [ -z "$OPENAI_API_KEY" ]; then
    echo "警告: 未设置OPENAI_API_KEY环境变量"
    echo "请设置您的OpenAI API密钥:"
    echo "export OPENAI_API_KEY=your-api-key-here"
    echo
    read -p "请输入您的OpenAI API密钥: " OPENAI_API_KEY
    if [ -z "$OPENAI_API_KEY" ]; then
        echo "错误: 必须提供OpenAI API密钥才能运行AI功能"
        exit 1
    fi
    export OPENAI_API_KEY
fi

echo "正在启动AI Simple Accounting..."
echo "API密钥: ${OPENAI_API_KEY:0:10}..."
echo

# 编译并运行
mvn clean compile
if [ $? -ne 0 ]; then
    echo "编译失败，请检查错误信息"
    exit 1
fi

echo "编译成功，正在启动应用..."
echo
echo "应用启动后，您可以访问:"
echo "- API健康检查: http://localhost:8080/api/transactions/health"
echo "- H2数据库控制台: http://localhost:8080/h2-console"
echo "- JDBC URL: jdbc:h2:file:./data/accounting"
echo "- 用户名: sa"
echo "- 密码: (留空)"
echo

mvn spring-boot:run 