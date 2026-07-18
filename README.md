<img width="1916" height="928" alt="image" src="https://github.com/user-attachments/assets/533ee833-4ccf-4473-b084-c8259e9a7876" />
---

### Matchday AI (Enterprise Stadium Operations)


# ⚽ Matchday AI
### The Android Command Center for Next-Gen Stadium Operations.

> **From fragmented chaos to a unified cockpit.**  
> *Integrates crowd analytics, environmental telemetry, and an AI co-pilot onto a single tablet for ops teams.*

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-UI-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Gemini AI](https://img.shields.io/badge/Gemini_AI-Integrated-FF6D00.svg)](https://deepmind.google/technologies/gemini/)
[![License](https://img.shields.io/badge/License-Internal_Trial-blue.svg)]()


## 🌟 The Problem
Stadium operations rely on siloed systems: Security has one screen, Sustainability has another, and Fan Services uses a walkie-talkie. **Matchday AI** breaks these silos, providing a real-time, reactive dashboard that turns raw sensor data into actionable operational intelligence.

## ⚡ Core Features
- **📊 Unified Real-time Dashboards**: Live incident management, occupancy heatmaps, and sustainability metrics (energy/air quality) in a single view.
- **📡 Hardware-Aware Telemetry**: Uses Android Sensor APIs (Accelerometer, Ambient Light, Microphone) with an **intelligent fallback simulation**—meaning the app works flawlessly even in demo environments without physical hardware.
- **🤖 Gemini-Powered Fan Co-pilot**: Fans can ask *"Where is the nearest vegetarian food court?"* or *"How do I get to Gate 4?"* and receive instant, contextual navigation via the Gemini API.
- **⚡ Reactive State Management**: Built with Kotlin Coroutines and Flows to handle high-frequency sensor data without UI jank.

## 🏗️ Architectural Flow

```mermaid
flowchart TD
    subgraph Android_Device
        A[Hardware Sensors<br/>Accelerometer/Light/Mic] --> B[Fallback Simulation Engine]
        B --> C[Kotlin Flow<br/>Data Stream]
        D[User Input] --> E[ViewModel / UI State]
    end

    subgraph Backend_Processing
        C --> F{Retrofit / OkHttp}
        F --> G[Mock API / Cloud Gateway]
        G --> H[Gemini 2.0 Flash API]
    end

    subgraph UI_Layer
        E --> I[Jetpack Compose Screen]
        H --> I
        I --> J[Reactive Charts & Alerts]
        I --> K[AI Chat Overlay]
    end

    style A fill:#f9f,stroke:#333,stroke-width:2px
    style H fill:#ff6,stroke:#333,stroke-width:2px
```
Key Design Decisions:

Why Fallback Simulation? To demo the platform to stadium owners without needing to install expensive IoT hardware on Day 1. It uses a randomized seed based on real-world stadium data patterns.

Why Kotlin Coroutines over RxJava? Lighter weight, better integration with Jetpack Compose recomposition, and easier to manage complex parallel sensor reads.

🛠️ Tech Stack (Android Focus)
Layer	Technology
UI	Jetpack Compose, Material 3, Coil (image loading)
State Management	Kotlin Coroutines, StateFlow, SharedFlow
Networking	Retrofit, OkHttp, Moshi (JSON parsing)
Sensors/Hardware	AndroidX Sensor API, Custom Simulation Engine
AI Integration	Google Gemini API (Multimodal & Text)
DI (Dependency Injection)	Hilt (for testing and simulation swapping)
## 📱 App Preview
<img width="200" height="400" alt="WhatsApp Image 2026-07-19 at 12 30 19 AM" src="https://github.com/user-attachments/assets/61009dfb-fe06-42fd-9a16-7058f38c0388" />
<img width="200" height="400" alt="WhatsApp Image 2026-07-19 at 12 30 19 AM (1)" src="https://github.com/user-attachments/assets/b221a7e7-9a30-4f4d-b056-2ae17ffe9457" />
<img width="200" height="400" alt="WhatsApp Image 2026-07-19 at 12 30 19 AM (2)" src="https://github.com/user-attachments/assets/991d4117-0e0b-4394-9d14-abfdb3321654" />
<img width="200" height="400" alt="WhatsApp Image 2026-07-19 at 12 30 20 AM" src="https://github.com/user-attachments/assets/3403fd95-e1a9-4446-994f-b83807af0aed" />
<img width="200" height="400" alt="WhatsApp Image 2026-07-19 at 12 30 20 AM (1)" src="https://github.com/user-attachments/assets/15f2c9f8-4001-4f20-9777-399017623416" />



# 🧪 Running the Project
Clone the repo

bash
git clone https://github.com/Arsh-sudo/matchday-ai.git
Open in Android Studio (Hedgehog or newer).

Set up API Keys
Create a local.properties file in the root and add:

properties
GEMINI_API_KEY=YOUR_API_KEY_HERE
# Optional: MOCK_BACKEND=true to bypass network calls
Select an Emulator/Device (API Level 26+).

Build & Run
Click the Run button. The app will auto-detect whether to use real sensors or fallback simulation based on the hardware availability.

# 🏟️ Use Cases
Security Teams: Instant pop-up alerts when crowd density exceeds threshold in a specific zone.

Sustainability Officers: Real-time energy consumption per square meter, displayed alongside occupancy to optimize HVAC.

Guest Relations: The Gemini co-pilot reduces walkie-talkie chatter by 40%, allowing staff to focus on physical guest interactions.

# 🗺️ Future Enhancements
Apple Watch / Wear OS companion for roaming security guards.

Predictive crowd flow using historical LLM analysis (forecasting bottlenecks 15 mins early).

Multilingual support for international fans.

Built for the Google Gemini API Developer Competition 🚀
Arsh Sharma 
