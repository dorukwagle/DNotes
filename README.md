# DNotes Distribution Guide

This guide explains how to create optimized, distributable packages for DNotes.

## 🎯 Quick Start

```bash
# Make scripts executable
chmod +x *.sh

# Estimate final size before building
./estimate-size.sh

# Build all distribution formats
./build-all-distributions.sh
```

## 📦 Available Distribution Formats

### 1. DEB Package (Recommended for Ubuntu/Debian users)
- **Best for:** End users on Ubuntu/Debian/Mint
- **Size:** ~60-90MB (vs 300-400MB with full JDK)
- **Installation:** `sudo dpkg -i target/dist/dnotes_1.0.0_amd64.deb`
- **Creates:** Menu entry, file associations, uninstaller

### 2. Portable Bundle
- **Best for:** Users who want no installation, or multiple Linux distros
- **Size:** ~50-80MB compressed
- **Usage:** Extract and run `./dnotes`
- **Benefits:** Run from USB, no admin rights needed

## 🛠️ Build Scripts

### `estimate-size.sh`
Preview the final package size before building.
```bash
./estimate-size.sh
```
Shows:
- Application JAR size
- Required Java modules
- Estimated JRE size
- Final package size estimate
- Space savings vs full JDK

### `build-distribution.sh`
Creates a DEB package with optimized JRE.
```bash
./build-distribution.sh
```
Output: `target/dist/dnotes_1.0.0_amd64.deb`

### `build-all-distributions.sh`
Creates all distribution formats at once.
```bash
./build-all-distributions.sh
```
Outputs:
- `target/dist/dnotes_1.0.0_amd64.deb` - DEB package
- `target/DNotes-1.0.0-portable-linux.tar.gz` - Linux portable
- `target/DNotes-1.0.0-portable.zip` - Cross-platform portable

## 📊 Size Comparison

| Package Type | Size | Notes |
|-------------|------|-------|
| Full JDK Bundle | ~300-400MB | Includes entire JDK |
| **Optimized JRE** | **~60-80MB** | Only required modules |
| DEB Package | ~70-90MB | JRE + app + metadata |
| Portable (compressed) | ~50-70MB | JRE + app, compressed |

## 🔧 How It Works

### The Optimization Process:

1. **Analysis (jdeps)**
   ```bash
   jdeps --print-module-deps your-app.jar
   ```
   Analyzes which Java modules your app actually uses.

2. **JRE Creation (jlink)**
   ```bash
   jlink --add-modules <detected-modules> --output custom-jre
   ```
   Creates a minimal JRE with only those modules.
   
   Options used:
   - `--strip-debug` - Remove debugging symbols
   - `--no-header-files` - No C headers
   - `--no-man-pages` - No manual pages
   - `--compress=2` - Maximum compression

3. **Packaging (jpackage)**
   ```bash
   jpackage --runtime-image custom-jre --input . --main-jar app.jar
   ```
   Bundles the custom JRE (not full JDK) with your app.

### What Gets Included:

✅ **Included:**
- Your application JAR
- Required Java modules (java.base, java.desktop, etc.)
- JavaFX modules (javafx.controls, javafx.graphics, etc.)
- Native libraries (.so files)
- SQLite JDBC native libraries

❌ **Excluded (from full JDK):**
- javac compiler
- jdb debugger
- javadoc
- Development headers
- Man pages
- Source files
- Unused Java modules

## 📝 Development vs Distribution

### For Development (daily work):
```bash
mvn javafx:run
```
Uses your system JDK, fast startup, easy debugging.

### For Distribution (release builds):
```bash
./build-all-distributions.sh
```
Creates optimized packages for end users.

## 🐛 Troubleshooting

### "Module not found" errors
If jlink fails with module errors, edit the script and add missing modules:
```bash
EXTRA_MODULES="jdk.crypto.ec,your.missing.module"
```

### JavaFX not found
Make sure `JAVAFX_PATH` in the script points to your JavaFX SDK:
```bash
JAVAFX_PATH="$HOME/.Programs/javafx-sdk-25/lib"
```

### Package too large
Check which modules are included:
```bash
./estimate-size.sh
```
Remove unnecessary modules if any were added by mistake.

### Non-modular dependencies
These are fine! The scripts handle them automatically by:
- Building a fat JAR with maven-shade-plugin
- Including all dependencies in the JAR
- Only the JRE needs to be modular

## 📤 Distribution Checklist

Before releasing:

- [ ] Test the DEB package: `sudo dpkg -i target/dist/*.deb`
- [ ] Test portable version: `cd target/portable && ./dnotes`
- [ ] Verify size: `./estimate-size.sh`
- [ ] Test on clean system without Java installed
- [ ] Check menu entry appears (for DEB)
- [ ] Test uninstall: `sudo apt remove dnotes`

## 🚀 Publishing

### For GitHub Releases:
```bash
./build-all-distributions.sh

# Upload these files:
# - target/dist/dnotes_1.0.0_amd64.deb
# - target/DNotes-1.0.0-portable-linux.tar.gz
# - target/DNotes-1.0.0-portable.zip
```

### Installation Instructions for Users:

**DEB (Ubuntu/Debian):**
```bash
sudo dpkg -i dnotes_1.0.0_amd64.deb
```

**Portable (Any Linux):**
```bash
tar -xzf DNotes-1.0.0-portable-linux.tar.gz
cd portable
./dnotes
```

## 💡 Tips

1. **Keep JRE minimal:** Only add modules you actually need
2. **Test on clean VM:** Verify it works without system Java
3. **Version consistently:** Keep version in sync across pom.xml and scripts
4. **Icon matters:** Add a proper PNG icon in `src/main/resources/icon.png`
5. **README in portable:** Users appreciate clear instructions

## 🔗 Additional Resources

- [jpackage documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jpackage.html)
- [jlink documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jlink.html)
- [jdeps documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jdeps.html)
