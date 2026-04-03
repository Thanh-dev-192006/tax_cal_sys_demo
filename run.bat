@echo off
REM Tax Return System - Run Script
REM Chạy project với compile tự động

echo === Tax Return System ===
echo Compiling project...

REM Compile với Java 17
javac -encoding UTF-8 -source 17 -target 17 -cp "lib/*" -d bin src/com/oop/project/Main.java src/com/oop/project/config/*.java src/com/oop/project/exception/*.java src/com/oop/project/model/*.java src/com/oop/project/repository/*.java src/com/oop/project/service/*.java src/com/oop/project/ui/*.java src/com/oop/project/util/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running application...
    echo.

    REM Chạy ứng dụng
    java -cp "lib/*;bin" com.oop.project.Main
) else (
    echo Compilation failed!
    pause
    exit /b 1
)
