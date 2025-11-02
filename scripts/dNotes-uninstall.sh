#!/bin/bash
APP_NAME="dNotes"
INSTALL_DIR="$HOME/.local/share/${APP_NAME}"
DESKTOP_FILE="$HOME/.local/share/applications/${APP_NAME}.desktop"

echo "Uninstalling $APP_NAME..."

rm -rf "$INSTALL_DIR"
rm -f "$DESKTOP_FILE"
rm -f ~/.local/share/icons/hicolor/512x512/apps/dNotes.png
rm -f ~/.local/bin/dNotes

update-desktop-database ~/.local/share/applications/
gtk-update-icon-cache

echo "✓ Uninstalled $APP_NAME"

rm -f ~/.local/bin/dNotes-uninstall

