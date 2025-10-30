@echo off
setlocal

set APP_NAME=dNotes
set APP_VERSION=1.0.0
set JAR_FILE=target\dNotes-%APP_VERSION%-shaded.jar
set JAVAFX_PATH=C:\javafx-sdk-25\lib
set IMAGE_DIR=target\jre-windows

REM Clean previous JRE
if exist "%IMAGE_DIR%" rmdir /s /q "%IMAGE_DIR%"

REM Create minimal JRE
jlink --module-path "%JAVA_HOME%\jmods" ^
      --add-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql ^
      --compress=2 ^
      --strip-debug ^
      --no-header-files ^
      --no-man-pages ^
      --bind-services ^
      --limit-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql ^
      --output "%IMAGE_DIR%"

echo Minimal JRE created at %IMAGE_DIR%

REM Build MSI
if exist target\dist-windows rmdir /s /q target\dist-windows
jpackage --name "%APP_NAME%" --app-version "%APP_VERSION%" --vendor "Doruk" --runtime-image "%IMAGE_DIR%" --input target --main-jar dNotes-%APP_VERSION%-shaded.jar --main-class com.doruk.dnotes.Main --dest target\dist-windows --type msi --win-shortcut --win-menu --win-menu-group "dNotes" --icon src\main\resources\icon.png

echo MSI created at target\dist-windows\%APP_NAME%-%APP_VERSION%.msi

REM Create portable ZIP
if exist target\portable-windows rmdir /s /q target\portable-windows
mkdir target\portable-windows
xcopy /E /I "%IMAGE_DIR%" target\portable-windows\jre
copy "%JAR_FILE%" target\portable-windows\dNotes.jar

echo @echo off > target\portable-windows\dnotes.bat
echo set SCRIPT_DIR=%%~dp0 >> target\portable-windows\dnotes.bat
echo "%%SCRIPT_DIR%%jre\bin\java.exe" -Dfile.encoding=UTF-8 -Xms64m -Xmx512m -jar "%%SCRIPT_DIR%%dNotes.jar" %%* >> target\portable-windows\dnotes.bat

powershell Compress-Archive -Path target\portable-windows -DestinationPath target\dNotes-%APP_VERSION%-windows-portable.zip -Force

echo Portable ZIP created at target\dNotes-%APP_VERSION%-windows-portable.zip
endlocal
