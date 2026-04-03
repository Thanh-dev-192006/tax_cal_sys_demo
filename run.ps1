# Tax Return System - Run Script
# Chạy project với compile tự động

Write-Host "=== Tax Return System ===" -ForegroundColor Green
Write-Host "Compiling project..." -ForegroundColor Yellow

# Compile với Java 17
javac -encoding UTF-8 -source 17 -target 17 -cp "lib/*" -d bin src/com/oop/project/Main.java src/com/oop/project/config/*.java src/com/oop/project/exception/*.java src/com/oop/project/model/*.java src/com/oop/project/repository/*.java src/com/oop/project/service/*.java src/com/oop/project/ui/*.java src/com/oop/project/util/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!" -ForegroundColor Green
    Write-Host "Running application..." -ForegroundColor Yellow
    Write-Host ""

    # Chạy ứng dụng
    java -cp "lib/*;bin" com.oop.project.Main
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}
