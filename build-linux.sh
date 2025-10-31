#!/bin/bash
set -e

APP_NAME="dNotes"
APP_VERSION="1.0.0"
MAIN_CLASS="com.doruk.dnotes.Main"

JAR_FILE="target/dNotes-${APP_VERSION}-shaded.jar"
IMAGE_DIR="target/dNotes-linux/jre"
APP_DIR="target/dNotes-linux"
PORTABLE_TAR="target/${APP_NAME}-${APP_VERSION}-linux.tar.xz"

GREEN='\033[0;32m'
NC='\033[0m'

echo -e "${GREEN}Building minimal JRE for ${APP_NAME}...${NC}"

# 1️⃣ Clean up previous builds
rm -rf "$APP_DIR" "$PORTABLE_TAR"

mkdir -p "$APP_DIR"

# 2️⃣ Build minimal runtime
$HOME/.Programs/jdk-25/bin/jlink \
  --module-path "$HOME/.Programs/jdk-25/jmods" \
  --add-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql \
  --compress=zip-9 \
  --strip-debug \
  --no-header-files \
  --no-man-pages \
  --bind-services \
  --output "$IMAGE_DIR" \
  --limit-modules java.base,java.desktop,java.logging,jdk.crypto.ec,java.sql


echo -e "${GREEN}✓ Minimal JRE created at $IMAGE_DIR${NC}"

# use minimal jvm
rm -r "$IMAGE_DIR/lib/server"
rm -r "$IMAGE_DIR/lib/client"
mv "$IMAGE_DIR/lib/minimal" "$IMAGE_DIR/lib/server"

cp "$JAR_FILE" "$APP_DIR/dNotes.jar"
cp scripts/* "$APP_DIR/"
cp src/main/resources/icon.png "$APP_DIR/icon.png"

# 4️⃣ Make launcher executable
chmod +x "$APP_DIR/install.sh"

# 5️⃣ Package everything
tar -cJf "$PORTABLE_TAR" -C target dNotes-linux

echo -e "${GREEN}✓ Portable tar.xz created: $PORTABLE_TAR${NC}"
echo -e "${GREEN}Done.${NC}"
