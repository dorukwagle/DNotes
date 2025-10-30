#!/bin/bash

echo "╔════════════════════════════════════════╗"
echo "║   DNotes Size Estimator                ║"
echo "╚════════════════════════════════════════╝"
echo ""

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Build if needed
if [ ! -f "target/DNotes-1.0.0-shaded.jar" ]; then
    echo "Building application first..."
    mvn clean package -q
    echo ""
fi

# Check JAR size
JAR_SIZE=$(du -sh target/DNotes-1.0.0-shaded.jar | cut -f1)
echo -e "${BLUE}Application JAR:${NC} $JAR_SIZE"

# Analyze modules
echo ""
echo "Analyzing required modules..."
JAVAFX_PATH="$HOME/.Programs/javafx-sdk-25/lib"

mvn dependency:copy-dependencies -DoutputDirectory=target/libs -q 2>/dev/null

DETECTED_MODULES=$(jdeps \
    --module-path "$JAVAFX_PATH" \
    --multi-release 25 \
    --ignore-missing-deps \
    --print-module-deps \
    --class-path 'target/libs/*' \
    target/DNotes-1.0.0-shaded.jar 2>/dev/null || echo "")

JAVAFX_MODULES="javafx.controls,javafx.graphics,javafx.base,javafx.fxml"
EXTRA_MODULES="jdk.crypto.ec"

if [ -n "$DETECTED_MODULES" ]; then
    ALL_MODULES="$DETECTED_MODULES,$JAVAFX_MODULES,$EXTRA_MODULES"
else
    ALL_MODULES="java.base,java.desktop,java.sql,java.logging,java.xml,$JAVAFX_MODULES,$EXTRA_MODULES"
fi

MODULES=$(echo "$ALL_MODULES" | tr ',' '\n' | sort -u | tr '\n' ',' | sed 's/,$//')
MODULE_COUNT=$(echo $MODULES | tr ',' '\n' | wc -l)

echo -e "${GREEN}✓${NC} Found $MODULE_COUNT required modules"
echo ""

# Estimate JRE size by modules
echo "Module breakdown:"
echo "$MODULES" | tr ',' '\n' | while read module; do
    echo "  • $module"
done
echo ""

# Create a test JRE to get actual size
echo "Creating test JRE to calculate size..."
rm -rf target/test-jre

jlink \
    --module-path "$JAVAFX_PATH:$JAVA_HOME/jmods" \
    --add-modules "$MODULES" \
    --output target/test-jre \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    --bind-services \
    2>/dev/null

JRE_SIZE=$(du -sh target/test-jre | cut -f1)
JRE_SIZE_MB=$(du -sm target/test-jre | cut -f1)

echo ""
echo "╔════════════════════════════════════════╗"
echo "║        SIZE ESTIMATES                  ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo -e "${BLUE}Components:${NC}"
echo "  Application JAR:    $JAR_SIZE"
echo "  Optimized JRE:      $JRE_SIZE"
echo ""

# Estimate final package sizes
DEB_ESTIMATE=$((JRE_SIZE_MB + 10))
PORTABLE_ESTIMATE=$((JRE_SIZE_MB + 5))

echo -e "${YELLOW}Estimated final sizes:${NC}"
echo "  DEB Package:        ~${DEB_ESTIMATE}MB"
echo "  Portable Bundle:    ~${PORTABLE_ESTIMATE}MB (compressed)"
echo ""

# Compare with full JDK
FULL_JDK=350
SAVINGS=$((FULL_JDK - JRE_SIZE_MB))
PERCENTAGE=$((SAVINGS * 100 / FULL_JDK))

echo -e "${GREEN}Space savings:${NC}"
echo "  Full JDK:           ~${FULL_JDK}MB"
echo "  Your JRE:           ${JRE_SIZE_MB}MB"
echo "  Saved:              ${SAVINGS}MB (${PERCENTAGE}% reduction)"
echo ""

# Cleanup
rm -rf target/test-jre

echo "To build actual distributions, run:"
echo "  ./build-distribution.sh       (DEB only)"
echo "  ./build-all-distributions.sh  (DEB + Portable)"
