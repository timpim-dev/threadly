from PIL import Image
import os

img = Image.open("applogo.png").convert("RGBA")

# Resize down to fit inside the adaptive icon safe zone (which is 66% of the canvas)
# 512 * 0.6 = 307
new_size = 307
img = img.resize((new_size, new_size), Image.Resampling.LANCZOS)

# Create a new 512x512 transparent foreground canvas
out = Image.new("RGBA", (512, 512), (0, 0, 0, 0))
offset = ((512 - new_size) // 2, (512 - new_size) // 2)
out.paste(img, offset, img)

os.makedirs("app/src/main/res/drawable-nodpi", exist_ok=True)
out.save("app/src/main/res/drawable-nodpi/ic_launcher_foreground.png")

# For legacy icons, let's create a black background version
legacy = Image.new("RGBA", (512, 512), (0, 0, 0, 255))
legacy.paste(img, offset, img)

sizes = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192
}

for name, size in sizes.items():
    d = f"app/src/main/res/mipmap-{name}"
    os.makedirs(d, exist_ok=True)
    leg = legacy.resize((size, size), Image.Resampling.LANCZOS)
    leg.save(f"{d}/ic_launcher.png")
    leg.save(f"{d}/ic_launcher_round.png")

print("Done")
