#!/bin/bash
set -e

APP_NAME="dNotes"
INSTALL_DIR="$HOME/.local/share/${APP_NAME}"
DESKTOP_FILE="$HOME/.local/share/applications/${APP_NAME}.desktop"

echo "Installing $APP_NAME..."

if command -v dNotes-uninstall >/dev/null 2>&1; then
    dNotes-uninstall >> /dev/null 2>&1
fi

# Copy the folder
mkdir -p "$INSTALL_DIR"
cp -r ./* "$INSTALL_DIR"

# create launcher script
cat > "$INSTALL_DIR/dNotes" <<EOF
#!/bin/bash
${INSTALL_DIR}/jre/bin/java \
  --enable-native-access=ALL-UNNAMED \
  -Xms128m -Xmx256m \
  -Dfile.encoding=UTF-8 \
  -jar ${INSTALL_DIR}/dNotes.jar

EOF

# Create .desktop entry
cat > "$DESKTOP_FILE" <<EOF
[Desktop Entry]
Name=dNotes
Exec=${INSTALL_DIR}/dNotes
Icon=dNotes
Type=Application
Categories=Office;Utility;
Terminal=false
StartupNotify=true
StartupWMClass=com.doruk.dnotes.App
EOF

# Create shell launch shortcut
cat > "$HOME/.local/bin/dNotes" <<EOF
#!/bin/bash
${INSTALL_DIR}/dNotes
EOF

cp "$INSTALL_DIR/dNotes-uninstall.sh" "$HOME/.local/bin/dNotes-uninstall"

# Copy icon to hicolor
mkdir -p ~/.local/share/icons/hicolor/512x512/apps
cp "$INSTALL_DIR/icon.png" ~/.local/share/icons/hicolor/512x512/apps/dNotes.png

chmod +x "$INSTALL_DIR/dNotes"
chmod +x "$DESKTOP_FILE"
chmod +x "$HOME/.local/bin/dNotes-uninstall"
chmod +x "$HOME/.local/bin/dNotes"

chmod 644 "$DESKTOP_FILE"
update-desktop-database ~/.local/share/applications/
gtk-update-icon-cache

echo "✓ dNotes installed successfully"
echo "✓ Desktop entry created"
echo "You can now find dNotes in your application menu."
echo "You can also run dNotes from the terminal by typing 'dNotes'"
echo "You can uninstall dNotes by typing 'dNotes-uninstall'"

rm "$INSTALL_DIR/dNotes-uninstall.sh"
rm "$INSTALL_DIR/install.sh"
