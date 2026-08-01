@echo off
cd /d "%~dp0"
echo ========================================
echo  Die If Tossed - Building...
echo ========================================
call gradlew.bat build
echo.
echo ========================================
echo  Build complete! jar in build\libs\
echo ========================================
pause
