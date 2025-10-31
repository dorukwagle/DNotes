@echo off
setlocal enabledelayedexpansion

:: -----------------------------
:: Config
:: -----------------------------
set APP_NAME=dNotes
set APP_VERSION=1.0.0
set MAIN_CLASS=com.doruk.dnotes.Main

set JAR_FILE=target\dNotes-%APP_VERSION%-shaded.jar
set IMAGE_DIR=target\dNotes-win\jre
set APP_DIR=target\dNotes-win
set MSI_DIR=target\dist-win
set ICON_FILE=src\main\resources\icon.png

:: -----------------------------
:: Clean previous build
:: -----------------------------
if exist "%APP_DIR%" rmdir /s /q "%APP_DIR%"
if exist "%MSI_DIR%" rmdir /s /q "%MSI_DIR%"

mkdir "%APP_DIR%"

:: -----------------------------
:: Build minimal JRE
:: -----------------------------
jlink ^
  --module-path "%JAVA_HOME%\jmods" ^
  --add-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql,javafx.base,javafx.controls,javafx.graphics ^
  --compress=2 ^
  --strip-debug ^
  --no-header-files ^
  --no-man-pages ^
  --bind-services ^
  --limit-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql,javafx.base,javafx.controls,javafx.graphics ^
  --output "%IMAGE_DIR%"

:: Remove unused JVM folders (server/client)
rmdir /s /q "%IMAGE_DIR%\lib\server"
rmdir /s /q "%IMAGE_DIR%\lib\client"
ren "%IMAGE_DIR%\lib\minimal" server

:: -----------------------------
:: Copy app jar, scripts, icon
:: -----------------------------
copy "%JAR_FILE%" "%APP_DIR%\dNotes.jar"
xcopy /s /y scripts "%APP_DIR%\scripts"
copy "%ICON_FILE%" "%APP_DIR%\icon.png"

:: -----------------------------
:: Make launcher executable (if using .bat)
:: -----------------------------
:: no chmod needed for Windows

:: -----------------------------
:: Create MSI installer using jpackage
:: -----------------------------
jpackage ^
  --name "%APP_NAME%" ^
  --app-version "%APP_VERSION%" ^
  --vendor "Doruk" ^
  --description "dNotes - Modern note-taking application" ^
  --copyright "Copyright © 2025 Doruk" ^
  --runtime-image "%IMAGE_DIR%" ^
  --input "%APP_DIR%" ^
  --main-jar dNotes.jar ^
  --main-class %MAIN_CLASS% ^
  --dest "%MSI_DIR%" ^
  --type msi ^
  --icon "%ICON_FILE%" ^
  --win-shortcut ^
  --win-menu ^
  --win-dir-chooser ^
  --java-options "-Dfile.encoding=UTF-8 -Xms64m -Xmx256m"

echo.
echo ✓ MSI installer created at %MSI_DIR%
echo Done.

pause
