# SimpliNote4j PowerShell Launcher
# This script launches SimpliNote4j on Windows

Write-Host "SimpliNote4j Launcher" -ForegroundColor Green
Write-Host "=====================" -ForegroundColor Green
Write-Host ""

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Java is not installed or not in PATH." -ForegroundColor Red
    Write-Host "Please install Java 17 or later from: https://adoptium.net/" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if the JAR file exists
$jarFile = "SimpliNote4j-1.0.0.jar"
if (-not (Test-Path $jarFile)) {
    Write-Host "ERROR: $jarFile not found in current directory." -ForegroundColor Red
    Write-Host "Please ensure the JAR file is in the same directory as this script." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Starting SimpliNote4j..." -ForegroundColor Cyan

# Launch the application
try {
    & java --module-path . --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.media,javafx.swing -jar $jarFile
} catch {
    Write-Host ""
    Write-Host "ERROR: Failed to start SimpliNote4j." -ForegroundColor Red
    Write-Host "Error details: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Try running this command manually:" -ForegroundColor Yellow
    Write-Host "java -jar $jarFile" -ForegroundColor White
}

Write-Host ""
Write-Host "SimpliNote4j has closed." -ForegroundColor Green
Read-Host "Press Enter to exit"