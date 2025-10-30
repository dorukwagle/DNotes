#!/bin/bash
APP_NAME="dNotes"
INSTALL_DIR="$HOME/.local/share/${APP_NAME}"
DESKTOP_FILE="$HOME/.local/share/applications/${APP_NAME}.desktop"

echo "Uninstalling $APP_NAME..."

rm -rf "$INSTALL_DIR"
rm -f "$DESKTOP_FILE"

echo "✓ Uninstalled $APP_NAME"
