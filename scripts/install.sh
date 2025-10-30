#!/bin/bash
set -e

APP_NAME="dNotes"
INSTALL_DIR="$HOME/.local/share/${APP_NAME}"
DESKTOP_FILE="$HOME/.local/share/applications/${APP_NAME}.desktop"

echo "Installing $APP_NAME..."

# Copy the folder
mkdir -p "$INSTALL_DIR"
cp -r ./* "$INSTALL_DIR"

# Create .desktop entry
cat > "$DESKTOP_FILE" <<EOF
[Desktop Entry]
Name=dNotes
Exec=${INSTALL_DIR}/dNotes.sh
Icon=${INSTALL_DIR}/icon.png
Type=Application
Categories=Office;Utility;
StartupNotify=true
EOF

chmod +x "$DESKTOP_FILE"

echo "✓ Installed to $INSTALL_DIR"
echo "✓ Desktop entry created: $DESKTOP_FILE"
echo "You can now find dNotes in your application menu."
