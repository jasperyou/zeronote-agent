@echo off
setlocal enabledelayedexpansion

REM API测试脚本
set BASE_URL=http://localhost:8080/api/transactions

echo ========================================
echo AI Simple Accounting API 测试
echo ========================================
echo.

REM 等待服务启动
echo 等待服务启动...
timeout /t 5 /nobreak >nul

REM 测试健康检查
echo 1. 测试健康检查...
curl -s "%BASE_URL%/health"
echo.
echo.

REM 测试快速记账
echo 2. 测试快速记账 (金额: 25.50)...
curl -s -X POST "%BASE_URL%/quick?amount=25.50"
echo.
echo.

REM 测试完整记账
echo 3. 测试完整记账...
curl -s -X POST "%BASE_URL%" ^
  -H "Content-Type: application/json" ^
  -d "{\"amount\": 89.99, \"description\": \"购买耳机\", \"merchant\": \"京东\", \"location\": \"北京\"}"
echo.
echo.

REM 测试餐饮记账
echo 4. 测试餐饮记账...
curl -s -X POST "%BASE_URL%" ^
  -H "Content-Type: application/json" ^
  -d "{\"amount\": 15.80, \"description\": \"咖啡\", \"merchant\": \"星巴克\", \"location\": \"上海\"}"
echo.
echo.

REM 等待AI处理
echo 等待AI处理完成...
timeout /t 3 /nobreak >nul

REM 查看最近的交易
echo 5. 查看最近的交易...
curl -s "%BASE_URL%/recent"
echo.
echo.

REM 按分类查询
echo 6. 按餐饮分类查询...
curl -s "%BASE_URL%/category/FOOD_DINING"
echo.
echo.

REM 搜索交易
echo 7. 搜索包含'星巴克'的交易...
curl -s "%BASE_URL%/search?keyword=星巴克"
echo.
echo.

REM 获取统计信息
echo 8. 获取本月统计信息...
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do (
    set MONTH=%%b
    set YEAR=%%c
)
curl -s "%BASE_URL%/statistics?startDate=!YEAR!-!MONTH!-01T00:00:00&endDate=!YEAR!-!MONTH!-31T23:59:59"
echo.
echo.

echo ========================================
echo API测试完成！
echo ========================================

pause 