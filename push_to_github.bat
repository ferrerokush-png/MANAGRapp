@echo off
setlocal EnableExtensions EnableDelayedExpansion

rem ==============================================
rem MANAGR -> GitHub Push Helper (Windows / cmd.exe)
rem ==============================================

set "LOG=%~dp0push_to_github.log"

call :log "=== Run started at %DATE% %TIME% ==="
echo.
echo === MANAGR -> GitHub Push Helper ===

rem Ensure Git is available
where git >nul 2>&1
if errorlevel 1 (
  echo ERROR: Git is not installed or not on PATH. | tee
  echo Install Git for Windows: https://git-scm.com/download/win | tee
  call :log "Git not found on PATH"
  goto :fail_with_pause
)

rem Move to the repository root (this script's folder)
set "REPO_ROOT=%~dp0"
cd /d "%REPO_ROOT%"
call :log "Repo root: %REPO_ROOT%"

rem Accept remote URL as first arg or prompt
set "REMOTE_URL=%~1"
set "BRANCH=%~2"
if "%BRANCH%"=="" set "BRANCH=main"

if "%REMOTE_URL%"=="" (
  echo Enter your GitHub remote URL (e.g., https://github.com/USER/REPO.git or git@github.com:USER/REPO.git):
  set /p REMOTE_URL=Remote URL:
)
if "%REMOTE_URL%"=="" (
  echo ERROR: Remote URL not provided. Aborting.
  call :log "No remote URL provided"
  goto :fail_with_pause
)
call :log "Remote: %REMOTE_URL% | Branch: %BRANCH%"

rem Optional debug mode
if /i "%DEBUG%"=="1" (
  set GIT_TRACE=1
  set GIT_CURL_VERBOSE=1
  call :log "Debug mode enabled"
)

rem Ensure Git identity (user.name / user.email)
for /f "delims=" %%a in ('git config --get user.name 2^>nul') do set "GIT_USER_NAME=%%a"
if "%GIT_USER_NAME%"=="" (
  set /p GIT_USER_NAME=Git user.name not set. Enter your name:
  if not "%GIT_USER_NAME%"=="" git config user.name "%GIT_USER_NAME%" >> "%LOG%" 2>&1
)
for /f "delims=" %%a in ('git config --get user.email 2^>nul') do set "GIT_USER_EMAIL=%%a"
if "%GIT_USER_EMAIL%"=="" (
  set /p GIT_USER_EMAIL=Git user.email not set. Enter your email:
  if not "%GIT_USER_EMAIL%"=="" git config user.email "%GIT_USER_EMAIL%" >> "%LOG%" 2>&1
)
call :log "Identity: name=%GIT_USER_NAME% email=%GIT_USER_EMAIL%"

rem Initialize repo if needed
if not exist ".git" (
  echo Initializing a new Git repository...
  git init >> "%LOG%" 2>&1
  if errorlevel 1 goto :fail_with_pause
)

rem Ensure default branch
git checkout -B "%BRANCH%" >nul 2>&1

rem Stage everything according to .gitignore
echo Staging files...
git add -A >> "%LOG%" 2>&1
if errorlevel 1 goto :fail_with_pause

rem If no commits yet, create initial commit
git rev-parse --verify HEAD >nul 2>&1
if errorlevel 1 (
  echo Creating initial commit...
  git commit -m "Initial commit" >> "%LOG%" 2>&1
  if errorlevel 1 (
    echo Commit failed. You may need to configure Git user.name and user.email.
    goto :fail_with_pause
  )
) else (
  rem Repo already has commits. Commit staged changes if any.
  git diff --cached --quiet
  if errorlevel 1 (
    for /f "tokens=1-3 delims=/ " %%a in ("%DATE%") do set "CDATE=%%a-%%b-%%c"
    set "CTIME=%TIME: =0%"
    echo Committing updates...
    git commit -m "Update project: %CDATE% %CTIME%" >> "%LOG%" 2>&1
    if errorlevel 1 goto :fail_with_pause
  ) else (
    echo No changes to commit.
  )
)

rem Configure or update remote origin
(git remote get-url origin >nul 2>&1) || (git remote add origin "%REMOTE_URL%" >> "%LOG%" 2>&1)
if not errorlevel 1 git remote set-url origin "%REMOTE_URL%" >> "%LOG%" 2>&1
if errorlevel 1 goto :fail_with_pause

rem Attempt to push to target branch
echo Pushing to origin/%BRANCH%...
call :log "Pushing to origin/%BRANCH%"
git push -u origin "%BRANCH%" >> "%LOG%" 2>&1
if errorlevel 1 (
  call :log "Primary push failed. Trying fallback branch managr-import"
  echo Primary push failed. Attempting to push to fallback branch 'managr-import'...
  git checkout -B managr-import >nul 2>&1
  git push -u origin managr-import >> "%LOG%" 2>&1
  if errorlevel 1 goto :fail_with_pause
  echo.
  echo Success! Pushed to fallback branch: origin/managr-import
  echo You can set 'managr-import' as default on GitHub or open a Pull Request to your default branch.
  call :log "Success on fallback branch managr-import"
  goto :success_with_pause
)

echo.
echo Success! The project has been pushed to:
for /f "delims=" %%u in ('git remote get-url origin') do echo %%u
call :log "Success on %BRANCH%"

:success_with_pause
if /i not "%NO_PAUSE%"=="1" pause
exit /b 0

:fail_with_pause
echo.
echo ERROR: Operation failed. See messages above for details.
echo See log: %LOG%
call :log "FAILED"
if /i not "%NO_PAUSE%"=="1" pause
exit /b 1

:log
>> "%LOG%" echo %~1
exit /b 0
