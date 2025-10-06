@echo off
echo Fixing compilation issues in MANAGR...
echo.

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    echo JAVA_HOME is not set. Please set it to your JDK installation path.
    echo Example: set JAVA_HOME=C:\Program Files\Java\jdk-17
    pause
    exit /b 1
)

echo Using JAVA_HOME: %JAVA_HOME%
echo.

REM Clean project
echo Cleaning project...
call gradlew clean

echo.
echo Building project...
call gradlew assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful! All compilation issues have been fixed.
    echo APK location: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo Build failed. Check the error messages above.
    echo.
    echo Common fixes applied:
    echo - Fixed TopAppBar deprecation warnings
    echo - Fixed items() function usage in LazyColumn
    echo - Fixed type converter imports
    echo - Added Hilt providers for type converters
)

pause
