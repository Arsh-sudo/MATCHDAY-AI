# 🏟️ Matchday AI: The Unified Operations Brain for FIFA World Cup 2026

**Matchday AI** is an enterprise-grade, high-performance tactical control center and operations dashboard designed for the upcoming FIFA World Cup 2026. This Android application aggregates real-time hardware telemetry, live camera streams, predictive stadium metrics, and tactical emergency notifications into a single, cohesive, premium-glassmorphic workspace.

Built strictly using modern Android development practices (**Jetpack Compose** and **Kotlin**), the application is optimized for low latency, high legibility, and critical situational awareness.

---

## 🎨 Premium Visual & Architectural Identity

Matchday AI features a meticulously designed visual style that prioritizes readability in high-stress, real-time command-and-control scenarios:

### 🌊 **Liquid Glassmorphism Core**
The entire UI leverages a high-fidelity custom glassmorphism implementation (`liquidGlass`). Elements float over dynamic ambient backdrops with:
*   High-contrast translucent colors.
*   Precise directional light-border highlights.
*   Frosted overlays that guarantee text legibility under any background visual conditions.

### 📐 **Balanced & Centered Geometry**
All core readings, telemetry widgets, and tactical cards have been precision-aligned:
*   **Fully Centered Metrics:** All key numbers, titles, custom micro-graphs, and status indicators are perfectly centered, eliminating cognitive visual load during rapid scans.
*   **Beautiful Hero Stadium Section:** The main world cup match header is nested inside a premium, rounded green glass container, showcasing live minute trackers and team pairings over a stunning stadium panorama.

---

## ⚡ Core Operational Features

### 1. 📊 Centralized Command Telemetry
*   **Attendance & Logistics Tracking:** Real-time crowd count with dynamic percentage changes.
*   **Live Queue Management:** Automated crowd wait-times for major stadium services (Stands, Entries, Restrooms).
*   **Hardware Sensor Integration:** Directly integrates with Android's physical device hardware:
    *   *Fan Cheering Index* (via raw Accelerometer signals mapped to dB values).
    *   *Illumination Metrics* (via Ambient Light sensor in lx).
    *   *Atmospheric Barometer* (via Pressure sensor in hPa).
    *   *Simulated Fallback Engine:* Provides fluid telemetry simulation if hardware sensors are absent.

### 2. 🚨 Tactical Action & Alert Center
A highly refined, urgency-tiered warning deck matching cutting-edge command interfaces:
*   **Crowd Surge Emergency Control (Gate 7):** Highlighted with critical red glass, providing rapid execution triggers ("Deploy & Execute") and direct camera links.
*   **Transit Alerts (Metro Delay - Line 3):** Warning card detailing operational delays and suggested mitigation steps.
*   **Weather Intelligence (Light Rain Expected):** Real-time meteorological telemetry prompting structural stadium modifications (e.g., "Close Roof").

### 3. 🌱 Sustainability Operations
*   Monitors eco-initiatives with high-precision metrics including:
    *   **Renewable Energy Percentage** (Solar Active tracking).
    *   **Waste Diversion Ratios** (Compost & Recycle volume).
    *   **Carbon Offset Equivalents** (Tons of $CO_2$ saved).

---

## 🛠️ Technological Architecture

Matchday AI utilizes the absolute best of modern Android engineering:

*   **Jetpack Compose:** Fully declarative UI using modern Material 3 design tokens.
*   **Kotlin Coroutines & Flow:** Asynchronous data streaming for real-time dashboard updates.
*   **ViewModel (MVVM):** High-reliability state lifecycle management, binding live metrics directly to UI states with complete state preservation.
*   **Hardware Sensors API:** Real-time physical device sensor registration, tracking, and normalization.

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio (Ladybug or newer recommended)
*   JDK 17+
*   Gradle 8.0+
*   Android SDK 34 (Target SDK) / SDK 26 (Min SDK)

### Building the Project
Clone the repository and compile using your terminal:

```bash
# Run unit tests
gradle :app:testDebugUnitTest

# Assemble Debug APK
gradle assembleDebug
```

---

## 📱 Developer Tools & Live Demo Mode
Matchday AI includes a dedicated **Live Demo** control dashboard. Developers and stadium operators can:
*   Toggle between physical device sensors and granular manual overrides.
*   Trigger instant high-priority alerts to test screen-level visual states and tactical layout responses.
*   Simulate high-density crowd conditions to test the responsive UI fluid-layout behaviors.
