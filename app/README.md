# 🍂 SARTORIAL AURA
### *A High-Fashion, Boutique Wardrobe Resonance, Live Atmosphere & Adaptive AI Logistics Planner*

---

Designed, engineered, and beautifully curated for the modern slow-fashion collector. **Sartorial Aura** is an offline-capable, high-contrast, boutique-inspired digital wardrobe companion. Built with elegant Jetpack Compose typography pairings, sophisticated slow-fade transition aesthetics, and a tactile book-paper-and-terracotta visual layout, it elevates clothing curation from a daily chore into an immersive geometric ritual.

**Crafted & Constructed by:**  
✨ **Jean Francois Regis MUKIZA** ✨

---

## 🎨 Design Theme & Aesthetics

The application rejects generic, sterile UI patterns in favor of a warm, editorial, tactile print-like composition:
*   **The Palette**: A bespoke high-fashion color harmony comprising warm book-paper whites (`#FDFCFB`), raw cement/sand panel backdrops (`#F5F2EE`), deep coal borders/text (`#1A1A1A`), and striking rich Terracotta brand accents (`#B24A27`).
*   **Boutique Typography**: Elegant Serif display headings paired with high-contrast, technical monospaced structural labels, evoking the feel of catalog layouts or vintage editorial lookbooks.
*   **Slow-Fade Entrances**: Built with premium, staggered, slow-fade entrance animation sequences to emulate the high-end boutique browsing experience, ensuring every piece of clothing enters the stage with absolute poise.

---

## ⚡ Core Architecture & Advanced Features

### 1. 📂 My Digital Closet (AI-Ingestion Closet)
*   **AI Clothes Digitation**: Capture and ingest detailed technical specs of your clothing.
*   **Color & Textile Fingerprint**: Store individual Color DNA swatches (e.g., Ruby Crimson, Sienna Clay, Sand Dunes), weight class thresholds (Light, Medium, Heavy), tactile texture codes (Smooth Satin, Rough Tweed, Heavy Knit), and exact fabric composition percentages.
*   **Design Silhouettes**: Classify garments with clean physical geometry templates (e.g., Slim, Straight, Oversized, Boxy, Tailored).
*   **Modesty Metrics**: Toggle specialized modesty constraints directly on individual clothes to seamlessly cross-reference with conservative environments or personal values.

### 2. 🎡 The Remix Engine™ (Zero-Waste Pairings)
*   Unlock underutilized garments with an algorithm designed for creative, eco-conscious style rotation.
*   Suggests unconventional but highly harmonious color-locked or texture-contrasting combos.
*   Renders specific worn frequency metrics (e.g., *"Worn frequency: 4 times"*) to actively combat over-consumption and bring forgotten clothes back into the highlight.

### 3. 🎯 Harmonious Outfit Suggestions (Strategy Decider)
*   **Occasion Strategy Deciders**: Craft specific style strategies with custom formality scales (1–10) and targeted style vibes (e.g., *"Sharp, modest"*).
*   **Special Overrides**: Instantly activate unique filters like **Sunday Church Mode** to strictly require modesty-compliant garments.
*   **Ensemble Power Scoring**: Renders real-time power metrics representing visual proportion compatibility, texture contrast, and weight harmony.

### 4. 🌦️ Live Climate Satellite Core (OpenWeatherMap API Integration)
*   **Atmospheric Synchronization**: Interrogate live satellites for any global city in real-time using the **OpenWeatherMap API**.
*   **Microclimate Layer Filter**: Automatically filters incoming available items based on real-world environment readings (temperature, rain, etc.):
    *   Avoids feather-light garments during freezing conditions.
    *   Excludes heavy materials in blazing summer heat.
    *   Avoids delicate materials such as suede shoes or non-water-resistant outerwear in rainy or snowy conditions.
*   **Simulation Toggles**: Offers offline fallback weather modes—**Warm Summer**, **Cool Breeze**, **Rainy Solitude**, and **Winter Freeze**—to preview recommendations dynamically.

### 5. 📸 “The Lookback” – AI-Powered Adaptive Feedback (Firestore Integration)
*   **Discreet Emotional Capture**: Log daily sentiment ratings for your worn outfits using subtle and high-contrast styling icons:
    *   ✨ **Sublime (Extraordinary)**: Elegant harmony matches perfectly.
    *   🙂 **Balanced (Good)**: Clean, comfortable composure.
    *   😕 **Recalibrate (Not Right)**: Silhouette or climate pairing fell short of expectations.
*   **Online/Offline Firestore Synchronization**: Powered by **Cloud Firestore** with an instant local fallback mechanism to queue modifications and sync when online.
*   **Self-Retraining Neural Model**: Retrains the Harmony Model dynamically. Recorded feedback inputs apply active positive resonance boosts (+15 points) or protective penalties (-25 points) to matching clothing templates, perfecting future styling recommendations automatically!

### 6. 🧼 Logistics & Laundry Hub
*   A physical Kanban-like status buffer designed for garment logistics: track whether pieces are **Available / Clean**, **Dirty**, or in the **Laundering Pipeline**.
*   Offers bulk-actions like *"Send All to Clean"* or *"Mark Dirty"* to maintain pure, real-time suggestion hygiene. If too many essential layers are dirty, the recommendation strategist elegantly advises cleaning clothes before proposing incomplete looks.

### 7. 🔮 Body Geometry & Aura Resonance Info
*   Configure your exact physical geometry dimensions: body height (cm), shoulder width proportion layouts (Narrow, Medium, Broad), and overall body shape profiles (Athletic, Lean, Stocky, Rectangle, Oval).
*   Align lookbook suggestions with your Skin Tone Palette classification and core Energy Vibe signatures (e.g., *"Joyful & Energetic"*, *"Serene & Tailored"*).

---

## 🌐 Connected Capacity

Under the hood, Sartorial Aura is built to expand. Configured with internet-ready connectivity parameters:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
This enables the application to integrate real-time API integrations, synchronize database configurations, or read dynamic meteorological services to match recommendations with real-world local weather metrics.

---

## 🛠️ How it Works & Compilation

The application is structured inside a native Android development space running modern **Jetpack Compose UI** with standard **Kotlin/Gradle**:

1.  **State Management**: Orchestrated via `AuraViewModel`, which manages complex state lists of clothes, custom planned occasions, seasonal active climates, real-time Firestore feedbacks, and body geometry profiles with offline-first persistence.
2.  **Modular Section Views**: Switch tabs between **Suggestions**, **My Closet**, **Laundry Hub**, **The Remix Engine**, and **Body Geometry Profiles** instantly.
3.  **UI Layout**: Standard compile-ready file imports on `@Composable` templates utilizing dynamic indices to handle the delayed `FadeInCard` transitions.

To verify compiling integrity, use the development gradle commands inside the working environment folder:
*   `gradle compileDebugKotlin`
*   `gradle assembleDebug`

---
*Sartorial Aura — Curating sustainable wardrobes through visual geometry, conscious wear-history, and absolute aesthetic rhythm.*
