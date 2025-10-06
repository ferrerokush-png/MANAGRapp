@echo off
echo Building MANAGR app for testing...
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

REM Clean and build
echo Cleaning project...
call gradlew clean

echo.
echo Building debug APK...
call gradlew assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful! APK location:
    echo app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo You can now install this APK on your Android device.
) else (
    echo.
    echo Build failed! Check the error messages above.
)

pause
