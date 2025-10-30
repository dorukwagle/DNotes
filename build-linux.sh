#!/bin/bash
set -e

APP_NAME="dNotes"
APP_VERSION="1.0.0"
JAR_FILE="target/dNotes-${APP_VERSION}-shaded.jar"
JAVAFX_PATH="$HOME/.Programs/javafx-sdk-25/lib"
IMAGE_DIR="target/jre-linux"

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}Building Linux minimal JRE for ${APP_NAME}...${NC}"

# Remove previous JRE
rm -rf "$IMAGE_DIR"

# Create minimal JRE
jlink \
    --module-path "$JAVA_HOME/jmods" \
    --add-modules java.base,java.desktop,java.logging,java.sql,java.naming,java.xml,java.scripting,jdk.crypto.ec \
    --compress=zip-9 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --bind-services \
    --output "$IMAGE_DIR"


echo -e "${GREEN}✓ Minimal JRE created at $IMAGE_DIR${NC}"

# Build DEB using jpackage
rm -rf target/dist-linux
jpackage \
    --name "$APP_NAME" \
    --app-version "$APP_VERSION" \
    --vendor "Doruk" \
    --description "dNotes - Modern note-taking application" \
    --copyright "Copyright © 2025 Doruk" \
    --runtime-image "$IMAGE_DIR" \
    --input target \
    --main-jar dNotes-${APP_VERSION}-shaded.jar \
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
    --java-options "-Xmx256m" \
    --icon src/main/resources/icon.png

DEB_FILE=$(ls target/dist-linux/*.deb | head -n1)
DEB_SIZE=$(du -sh "$DEB_FILE" | cut -f1)
echo -e "${GREEN}✓ DEB created: $DEB_FILE (${DEB_SIZE})${NC}"

PORTABLE_TAR="target/dNotes-${APP_VERSION}-linux-portable.tar.xz"
echo "Creating portable tar.xz: $PORTABLE_TAR"

rm -rf target/portable-linux
mkdir -p target/portable-linux

cp -r target/jre-linux target/portable-linux/jre
cp target/dNotes-${APP_VERSION}-shaded.jar target/portable-linux/dNotes.jar

# Use -C inside the portable-linux folder
tar -cJf "$PORTABLE_TAR" -C target portable-linux

# Leave the IMAGE_DIR intact for portable bundle
echo -e "${GREEN}✓ Portable tar.xz created: $PORTABLE_TAR${NC}"
