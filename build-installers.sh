#!/bin/bash
set -e

echo "╔════════════════════════════════════════╗"
echo "║   DNotes Multi-Platform Builder        ║"
echo "║   DEB + MSI + Portable Bundle          ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Configuration
JAVAFX_PATH="$HOME/.Programs/javafx-sdk-25/lib"
APP_VERSION="1.0.0"
APP_NAME="DNotes"
CURRENT_OS=$(uname -s)

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check prerequisites
echo "Checking prerequisites..."
if [ ! -d "$JAVAFX_PATH" ]; then
    echo -e "${RED}✗${NC} JavaFX not found at: $JAVAFX_PATH"
    echo "  Please update JAVAFX_PATH in this script"
    exit 1
fi

if ! command -v jpackage &> /dev/null; then
    echo -e "${RED}✗${NC} jpackage not found. Requires JDK 14+"
    exit 1
fi

echo -e "${GREEN}✓${NC} Prerequisites OK"
echo ""

# =============================================================================
# STEP 1: Build Application
# =============================================================================
echo -e "${BLUE}[1/7]${NC} Building application..."
mvn clean package -q
if [ ! -f "target/DNotes-${APP_VERSION}-shaded.jar" ]; then
    echo -e "${RED}✗${NC} Build failed"
    exit 1
fi
echo -e "${GREEN}✓${NC} Build complete"
echo ""

# =============================================================================
# STEP 2: Collect Dependencies
# =============================================================================
echo -e "${BLUE}[2/7]${NC} Collecting dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory=target/libs -q
echo -e "${GREEN}✓${NC} Dependencies collected"
echo ""

# =============================================================================
# STEP 3: Analyze Required Modules
# =============================================================================
echo -e "${BLUE}[3/7]${NC} Analyzing required Java modules..."

DETECTED_MODULES=$(jdeps \
    --module-path "$JAVAFX_PATH" \
    --multi-release 25 \
    --ignore-missing-deps \
    --print-module-deps \
    --class-path 'target/libs/*' \
    target/DNotes-${APP_VERSION}-shaded.jar 2>/dev/null || echo "")

JAVAFX_MODULES="javafx.controls,javafx.graphics,javafx.base,javafx.fxml"
EXTRA_MODULES="jdk.crypto.ec"

if [ -n "$DETECTED_MODULES" ]; then
    ALL_MODULES="$DETECTED_MODULES,$JAVAFX_MODULES,$EXTRA_MODULES"
else
    ALL_MODULES="java.base,java.desktop,java.sql,java.logging,java.xml,java.naming,$JAVAFX_MODULES,$EXTRA_MODULES"
fi

MODULES=$(echo "$ALL_MODULES" | tr ',' '\n' | sort -u | tr '\n' ',' | sed 's/,$//')

echo -e "${GREEN}✓${NC} Modules identified: $(echo $MODULES | tr ',' '\n' | wc -l) modules"
echo ""

# =============================================================================
# STEP 4: Create Optimized JRE (Linux)
# =============================================================================
echo -e "${BLUE}[4/7]${NC} Creating optimized Linux JRE..."
rm -rf target/jre-linux

MODULE_PATH="$JAVAFX_PATH:$JAVA_HOME/jmods"

jlink \
    --module-path "$MODULE_PATH" \
    --add-modules "$MODULES" \
    --output target/jre-linux \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    --bind-services

JRE_LINUX_SIZE=$(du -sh target/jre-linux | cut -f1)
echo -e "${GREEN}✓${NC} Linux JRE created (${YELLOW}${JRE_LINUX_SIZE}${NC})"
echo ""

# =============================================================================
# STEP 5: Build DEB Package
# =============================================================================
echo -e "${BLUE}[5/7]${NC} Creating DEB package..."
rm -rf target/dist-linux

jpackage \
    --name "$APP_NAME" \
    --app-version "$APP_VERSION" \
    --vendor "Doruk" \
    --description "DNotes - Modern note-taking application" \
    --copyright "Copyright © 2025 Doruk" \
    --runtime-image target/jre-linux \
    --input target \
    --main-jar DNotes-${APP_VERSION}-shaded.jar \
    --main-class com.doruk.dnotes.Main \
    --dest target/dist-linux \
    --type deb \
    --linux-shortcut \
    --linux-menu-group "Office" \
    --linux-app-category "Office" \
    --linux-package-name "dnotes" \
    --linux-deb-maintainer "doruk@example.com" \
    --java-options "-Dfile.encoding=UTF-8" \
    --java-options "-Xms64m" \
    --java-options "-Xmx512m"

DEB_FILE=$(ls target/dist-linux/*.deb 2>/dev/null | head -n1)
if [ -f "$DEB_FILE" ]; then
    DEB_SIZE=$(du -sh "$DEB_FILE" | cut -f1)
    echo -e "${GREEN}✓${NC} DEB package created (${YELLOW}${DEB_SIZE}${NC})"
else
    echo -e "${YELLOW}⚠${NC} DEB package creation failed or skipped"
fi
echo ""

# =============================================================================
# STEP 6: Create Portable Linux Bundle (tar.xz)
# =============================================================================
echo -e "${BLUE}[6/7]${NC} Creating portable Linux bundle..."
rm -rf target/portable-linux
mkdir -p target/portable-linux

cp -r target/jre-linux target/portable-linux/jre
cp target/DNotes-${APP_VERSION}-shaded.jar target/portable-linux/DNotes.jar

# Create launcher script
cat > target/portable-linux/dnotes << 'EOF'
#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
exec "$SCRIPT_DIR/jre/bin/java" \
    -Dfile.encoding=UTF-8 \
    -Xms64m \
    -Xmx512m \
    -jar "$SCRIPT_DIR/DNotes.jar" \
    "$@"
EOF
chmod +x target/portable-linux/dnotes

# Create README
cat > target/portable-linux/README.txt << 'EOF'
═══════════════════════════════════════
  DNotes - Portable Linux Bundle
═══════════════════════════════════════

QUICK START
───────────
  ./dnotes

CONTENTS
────────
  • DNotes.jar  - Application
  • jre/        - Optimized Java Runtime
  • dnotes      - Launch script

REQUIREMENTS
────────────
None! Self-contained package.

INSTALLATION
────────────
1. Extract anywhere
2. Run: ./dnotes

FEATURES
────────
✓ No Java installation required
✓ Run from USB/external drive
✓ Optimized JRE (60-80MB vs 300MB)
✓ Works on any Linux distribution

EOF

# Create tar.xz archive
cd target
echo "  Compressing with xz (this may take a moment)..."
tar -cJf DNotes-${APP_VERSION}-linux-portable.tar.xz portable-linux/
PORTABLE_SIZE=$(du -sh DNotes-${APP_VERSION}-linux-portable.tar.xz | cut -f1)
cd ..

echo -e "${GREEN}✓${NC} Portable bundle created (${YELLOW}${PORTABLE_SIZE}${NC})"
echo ""

# =============================================================================
# STEP 7: Create Windows MSI (if on Windows or cross-compile possible)
# =============================================================================
echo -e "${BLUE}[7/7]${NC} Creating Windows MSI..."

if [ "$CURRENT_OS" != "Linux" ]; then
    echo -e "${YELLOW}⚠${NC} Windows MSI can only be built on Windows"
    echo "  Copy this project to Windows and run:"
    echo "  build-windows-msi.bat"
    echo ""
    echo -e "${YELLOW}→${NC} Creating build-windows-msi.bat script..."

    # Create Windows build script
    cat > build-windows-msi.bat << 'EOFWIN'
@echo off
echo ========================================
echo   DNotes Windows MSI Builder
echo ========================================
echo.

set APP_VERSION=1.0.0
set APP_NAME=DNotes
set JAVAFX_PATH=C:\javafx-sdk-25\lib

echo [1/5] Building application...
call mvn clean package -q
if not exist "target\DNotes-%APP_VERSION%-shaded.jar" (
    echo Error: Build failed
    exit /b 1
)
echo Done.
echo.

echo [2/5] Analyzing modules...
for /f "delims=" %%i in ('jdeps --module-path "%JAVAFX_PATH%" --multi-release 25 --ignore-missing-deps --print-module-deps target\DNotes-%APP_VERSION%-shaded.jar 2^>nul') do set DETECTED_MODULES=%%i

set JAVAFX_MODULES=javafx.controls,javafx.graphics,javafx.base,javafx.fxml
set EXTRA_MODULES=jdk.crypto.ec

if defined DETECTED_MODULES (
    set ALL_MODULES=%DETECTED_MODULES%,%JAVAFX_MODULES%,%EXTRA_MODULES%
) else (
    set ALL_MODULES=java.base,java.desktop,java.sql,java.logging,java.xml,%JAVAFX_MODULES%,%EXTRA_MODULES%
)

echo Done.
echo.

echo [3/5] Creating optimized Windows JRE...
if exist "target\jre-windows" rmdir /s /q target\jre-windows

jlink --module-path "%JAVAFX_PATH%;%JAVA_HOME%\jmods" --add-modules %ALL_MODULES% --output target\jre-windows --strip-debug --no-header-files --no-man-pages --compress=2 --bind-services

echo Done.
echo.

echo [4/5] Creating MSI installer...
if exist "target\dist-windows" rmdir /s /q target\dist-windows

jpackage --name "%APP_NAME%" --app-version "%APP_VERSION%" --vendor "Doruk" --description "DNotes - Modern note-taking application" --copyright "Copyright 2025 Doruk" --runtime-image target\jre-windows --input target --main-jar DNotes-%APP_VERSION%-shaded.jar --main-class com.doruk.dnotes.Main --dest target\dist-windows --type msi --win-dir-chooser --win-menu --win-shortcut --win-menu-group "DNotes" --java-options "-Dfile.encoding=UTF-8" --java-options "-Xms64m" --java-options "-Xmx512m"

echo Done.
echo.

echo [5/5] Creating portable Windows bundle...
if exist "target\portable-windows" rmdir /s /q target\portable-windows
mkdir target\portable-windows

xcopy /E /I /Q target\jre-windows target\portable-windows\jre
copy target\DNotes-%APP_VERSION%-shaded.jar target\portable-windows\DNotes.jar

echo @echo off > target\portable-windows\dnotes.bat
echo set SCRIPT_DIR=%%~dp0 >> target\portable-windows\dnotes.bat
echo "%%SCRIPT_DIR%%jre\bin\java.exe" -Dfile.encoding=UTF-8 -Xms64m -Xmx512m -jar "%%SCRIPT_DIR%%DNotes.jar" %%* >> target\portable-windows\dnotes.bat

cd target
powershell Compress-Archive -Path portable-windows -DestinationPath DNotes-%APP_VERSION%-windows-portable.zip -Force
cd ..

echo.
echo ========================================
echo   Build Complete!
echo ========================================
echo.
echo MSI Installer: target\dist-windows\DNotes-%APP_VERSION%.msi
echo Portable ZIP:  target\DNotes-%APP_VERSION%-windows-portable.zip
echo.
pause
EOFWIN

    echo -e "${GREEN}✓${NC} Created build-windows-msi.bat"
    echo -e "${BLUE}  →${NC} Run this on Windows to create MSI installer"
else
    echo -e "${YELLOW}⚠${NC} Currently on Linux - cannot build Windows MSI"
    echo -e "${BLUE}  →${NC} Created build-windows-msi.bat for use on Windows"
fi
echo ""

# =============================================================================
# SUMMARY
# =============================================================================
echo "╔════════════════════════════════════════════════════════╗"
echo "║              BUILD SUMMARY                              ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""
echo -e "${YELLOW}📦 Linux Distributions:${NC}"
echo ""

if [ -f "$DEB_FILE" ]; then
    echo -e "  ${GREEN}✓ DEB Package (Ubuntu/Debian)${NC}"
    echo "    File: $DEB_FILE"
    echo "    Size: $DEB_SIZE"
    echo "    Install: sudo dpkg -i $DEB_FILE"
    echo ""
fi

echo -e "  ${GREEN}✓ Portable Bundle (Any Linux)${NC}"
echo "    File: target/DNotes-${APP_VERSION}-linux-portable.tar.xz"
echo "    Size: $PORTABLE_SIZE"
echo "    Usage: tar -xJf DNotes-${APP_VERSION}-linux-portable.tar.xz"
echo "           cd portable-linux && ./dnotes"
echo ""

echo -e "${YELLOW}📦 Windows Distribution:${NC}"
echo ""
echo -e "  ${BLUE}→ Run on Windows:${NC} build-windows-msi.bat"
echo "    Will create:"
echo "    • target\\dist-windows\\DNotes-${APP_VERSION}.msi"
echo "    • target\\DNotes-${APP_VERSION}-windows-portable.zip"
echo ""

echo "╔════════════════════════════════════════════════════════╗"
echo "║              SIZE COMPARISON                            ║"
echo "╠════════════════════════════════════════════════════════╣"
echo -e "║  Full JDK:           ~300-400 MB                       ║"
echo -e "║  Custom JRE:         ${YELLOW}${JRE_LINUX_SIZE}${NC}                          ║"
if [ -f "$DEB_FILE" ]; then
echo -e "║  DEB Package:        ${YELLOW}${DEB_SIZE}${NC}                          ║"
fi
echo -e "║  Portable (tar.xz):  ${YELLOW}${PORTABLE_SIZE}${NC}                          ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

echo -e "${GREEN}✓ Linux builds complete!${NC}"
echo ""
echo "Next steps:"
echo "  1. Test DEB: sudo dpkg -i $DEB_FILE"
echo "  2. Test portable: cd target/portable-linux && ./dnotes"
echo "  3. For Windows MSI: Copy project to Windows and run build-windows-msi.bat"
