import os
from PIL import Image

src_img = "/Users/lilly/.gemini/antigravity-ide/brain/f2130007-ef25-4fc4-854e-b6e32a2cac11/professional_logo_1789758810997.jpg"
img = Image.open(src_img).convert("RGBA")

densities = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192
}

base_path = "/Users/lilly/Codes/Threadly/app/src/main/res"

for density, size in densities.items():
    # We want adaptive icons to be 108dp, but for the foreground we just resize to size * (108/48)
    # Wait, Android standard: 108dp * density_multiplier.
    # mdpi: 108x108
    # hdpi: 162x162
    # xhdpi: 216x216
    # xxhdpi: 324x324
    # xxxhdpi: 432x432
    multiplier = size / 48.0
    adaptive_size = int(108 * multiplier)
    
    resized = img.resize((adaptive_size, adaptive_size), Image.Resampling.LANCZOS)
    
    dir_path = os.path.join(base_path, f"mipmap-{density}")
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)
    
    out_path = os.path.join(dir_path, "ic_launcher_foreground.png")
    resized.save(out_path, "PNG")
    print(f"Saved {out_path}")
