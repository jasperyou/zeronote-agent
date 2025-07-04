@echo off
echo ========================================
echo AI Simple Accounting - 智能记账系统
echo ========================================
echo.

REM 检查Java版本
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java，请确保已安装Java 21+
    pause
    exit /b 1
)

REM 检查Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Maven，请确保已安装Maven 3.9+
    pause
    exit /b 1
)

REM 检查OpenAI API Key
if "%OPENAI_API_KEY%"=="" (
    echo 警告: 未设置OPENAI_API_KEY环境变量
    echo 请设置您的OpenAI API密钥:
    echo set OPENAI_API_KEY=your-api-key-here
    echo.
    set /p OPENAI_API_KEY="请输入您的OpenAI API密钥: "
    if "!OPENAI_API_KEY!"=="" (
        echo 错误: 必须提供OpenAI API密钥才能运行AI功能
        pause
        exit /b 1
    )
)

echo 正在启动AI Simple Accounting...
echo API密钥: %OPENAI_API_KEY:~0,10%...
echo.

REM 编译并运行
mvn clean compile
if errorlevel 1 (
    echo 编译失败，请检查错误信息
    pause
    exit /b 1
)

echo 编译成功，正在启动应用...
echo.
echo 应用启动后，您可以访问:
echo - API健康检查: http://localhost:8080/api/transactions/health
echo - H2数据库控制台: http://localhost:8080/h2-console
echo - JDBC URL: jdbc:h2:file:./data/accounting
echo - 用户名: sa
echo - 密码: (留空)
echo.

mvn spring-boot:run

pause 