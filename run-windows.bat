@echo off
REM SimpliNote4j Windows Launcher Script
REM This script launches the SimpliNote4j application

echo Starting SimpliNote4j...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH.
    echo Please install Java 17 or later and make sure it's in your PATH.
    echo You can download Java from: https://adoptium.net/
    pause
    exit /b 1
)

REM Run the application
java --module-path . --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.media,javafx.swing -jar SimpliNote4j-1.0.0.jar

REM If we get here, the application has closed
echo.
echo SimpliNote4j has closed.
pause