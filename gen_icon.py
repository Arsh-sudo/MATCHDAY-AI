import math

cx = 54
cy = 54
R = 34
r = R * 0.35

angles = [-math.pi/2, -math.pi/2 + 2*math.pi/5, -math.pi/2 + 4*math.pi/5, -math.pi/2 + 6*math.pi/5, -math.pi/2 + 8*math.pi/5]

pentagon_points = []
outer_points = []

for a in angles:
    px = cx + r * math.cos(a)
    py = cy + r * math.sin(a)
    pentagon_points.append((px, py))
    
    ox = cx + R * math.cos(a)
    oy = cy + R * math.sin(a)
    outer_points.append((ox, oy))

pentagon_path = f"M {pentagon_points[0][0]:.3f} {pentagon_points[0][1]:.3f} "
for i in range(1, 5):
    pentagon_path += f"L {pentagon_points[i][0]:.3f} {pentagon_points[i][1]:.3f} "
pentagon_path += "Z"

lines_xml = ""
for i in range(5):
    px, py = pentagon_points[i]
    ox, oy = outer_points[i]
    lines_xml += f"""
    <path
        android:pathData="M {px:.3f} {py:.3f} L {ox:.3f} {oy:.3f}"
        android:strokeColor="#FBBF24"
        android:strokeWidth="4" />
"""

xml = f"""<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    
    <!-- Outer Green Circle -->
    <path
        android:pathData="M 54 20 A 34 34 0 1 0 54 88 A 34 34 0 1 0 54 20 Z"
        android:strokeColor="#4ADE80"
        android:strokeWidth="4" />
        
    <!-- Lines connecting pentagon to circle -->
{lines_xml}
    <!-- Center White Pentagon -->
    <path
        android:pathData="{pentagon_path}"
        android:fillColor="#FFFFFF" />
</vector>
"""

with open("app/src/main/res/drawable/ic_launcher_foreground.xml", "w") as f:
    f.write(xml)

print("Icon generated successfully.")
