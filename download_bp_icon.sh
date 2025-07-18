#!/bin/bash

# BP Icon Download and Resize Script for Android
# This script downloads the BP logo and creates different sizes for Android densities

echo "🚀 Starting BP icon download and resize process..."

# Create temporary directory
TEMP_DIR="./icon_dir"
mkdir -p "$TEMP_DIR"

# Download the BP logo
echo "📥 Downloading BP logo..."
curl -L "$TEMP_DIR/bp_logo_original.png"

if [ ! -f "$TEMP_DIR/bp_logo_original.png" ]; then
    echo "❌ Failed to download BP logo. Please check your internet connection."
    exit 1
fi

echo "✅ BP logo downloaded successfully!"

# Check if ImageMagick is installed
if ! command -v magick &> /dev/null; then
    echo "❌ ImageMagick (magick) is not installed. Please install it first:"
    echo "   macOS: brew install imagemagick"
    echo "   Ubuntu/Debian: sudo apt-get install imagemagick"
    echo "   Windows: Download from https://imagemagick.org/script/download.php"
    exit 1
fi

# Define the densities and sizes
DENSITIES=(mdpi hdpi xhdpi xxhdpi xxxhdpi)
SIZES=(48 72 96 144 192)

# Create mipmap directories if they don't exist
for density in "${DENSITIES[@]}"; do
    mkdir -p "app/src/main/res/mipmap-${density}"
done

# Resize and copy to appropriate folders
echo "🔄 Resizing images for different densities..."
for i in ${!DENSITIES[@]}; do
    density="${DENSITIES[$i]}"
    size="${SIZES[$i]}"
    echo "   Creating ${size}x${size} for ${density}..."
    magick "$TEMP_DIR/bp_logo_original.png" -resize "${size}x${size}" "app/src/main/res/mipmap-${density}/ic_bp_logo.png"
done

# Clean up temporary directory
rm -rf "$TEMP_DIR"

echo "✅ BP icon setup completed!"
echo "📁 Icons have been placed in the following folders:"
for density in "${DENSITIES[@]}"; do
    echo "   - app/src/main/res/mipmap-${density}/ic_bp_logo.png"
done

echo ""
echo "🎉 You can now run your Android app with the BP icons!"
echo "💡 The icons will automatically be used based on the device's screen density." 