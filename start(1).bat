@echo off
chcp 65001 >nul
echo ========================================
echo    一键启动脚本
echo ========================================
echo.

:: 检查Java环境
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)

:: 检查Maven环境
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Maven环境，请先安装Maven
    pause
    exit /b 1
)

:: 检查Node.js环境
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Node.js环境，请先安装Node.js
    pause
    exit /b 1
)

:: 检查npm环境
where npm >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到npm环境，请先安装npm
    pause
    exit /b 1
)

echo [信息] 环境检查通过
echo.

:: 获取脚本所在目录
set "SCRIPT_DIR=%~dp0"
set "BACKEND_DIR=%SCRIPT_DIR%backend"
set "FRONTEND_DIR=%SCRIPT_DIR%frontend"

:: 获取项目文件夹名称（最后一级路径）
for %%I in ("%SCRIPT_DIR:~0,-1%") do set "PROJECT_NAME=%%~nxI"

:: 检查目录是否存在
if not exist "%BACKEND_DIR%" (
    echo [错误] 后端目录不存在: %BACKEND_DIR%
    pause
    exit /b 1
)

if not exist "%FRONTEND_DIR%" (
    echo [错误] 前端目录不存在: %FRONTEND_DIR%
    pause
    exit /b 1
)

echo [信息] 正在启动后端服务（端口: 8888）...
cd /d "%BACKEND_DIR%"
start "%PROJECT_NAME% - 后端" cmd /k "title %PROJECT_NAME% - 后端 & mvn spring-boot:run"
timeout /t 3 /nobreak >nul

echo [信息] 正在启动前端服务...
cd /d "%FRONTEND_DIR%"
:: 检查node_modules是否存在
if not exist "node_modules" (
    echo [信息] 检测到未安装依赖，正在安装前端依赖...
    npm install --legacy-peer-deps
    if errorlevel 1 (
        echo [错误] 前端依赖安装失败，请检查错误信息
        pause
        exit /b 1
    )
    echo [信息] 前端依赖安装成功
)

:: 检测package.json中的启动命令
set "START_CMD="

:: 检测serve命令
findstr /C:"\"serve\"" package.json >nul 2>&1
if %errorlevel% equ 0 (
    set "START_CMD=serve"
    echo [信息] 检测到 npm run serve 命令
    goto :cmd_found
)

:: 如果没找到serve，尝试dev
findstr /C:"\"dev\"" package.json >nul 2>&1
if %errorlevel% equ 0 (
    set "START_CMD=dev"
    echo [信息] 检测到 npm run dev 命令
    goto :cmd_found
)

:: 如果还没找到，尝试start
findstr /C:"\"start\"" package.json >nul 2>&1
if %errorlevel% equ 0 (
    set "START_CMD=start"
    echo [信息] 检测到 npm start 命令
    goto :cmd_found
)

:: 都没找到，报错
echo [错误] 未找到可用的启动命令（serve/dev/start）
pause
exit /b 1

:cmd_found
echo [调试] 项目名称: %PROJECT_NAME%
echo [调试] 启动命令: npm run %START_CMD%
start "%PROJECT_NAME% - 前端" cmd /k "title %PROJECT_NAME% - 前端 & npm run %START_CMD%"

echo.
echo ========================================
echo   启动完成！
echo ========================================
echo   后端服务: http://localhost:8888/api
echo   前端服务: 请查看前端服务窗口获取端口信息
echo.
echo   提示: 关闭对应的窗口即可停止对应的服务
echo ========================================
echo.
pause
