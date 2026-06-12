# 🍂 SARTORIAL AURA
### *A High-Fashion, Boutique Wardrobe Resonance, Live Atmosphere & Adaptive AI Logistics Planner*

---

Designed, engineered, and beautifully curated for the modern slow-fashion collector. **Sartorial Aura** is an offline-capable, high-contrast, boutique-inspired digital wardrobe companion. Built with elegant Jetpack Compose typography pairings, sophisticated slow-fade transition aesthetics, and a tactile book-paper-and-terracotta visual layout, it elevates clothing curation from a daily chore into an immersive geometric ritual.

**Crafted & Constructed by:**  
✨ **Jean Francois Regis MUKIZA** ✨

---

## 🏗️ 1. The AURA System Architecture

Sartorial Aura is built on a resilient, modern, event-driven client-to-cloud architecture designed to support fluid UI interactions, robust offline capabilities, and cross-device model synchronization.

```
       +---------------------------------------------+
       |           SARTORIAL AURA CLIENT             |
       |  (Kotlin / Jetpack Compose / MVVM / Flow)   |
       +------▲------------------------------│-------+
              │                              │
     Synchronizes state                Writes lookbacks
       under offline-                    and feedback
       resilient cache                   real-time
              │                              │
       +------┴------------------------------▼-------+
       |             CLOUD FIRESTORE                 |
       |   Collections: 'closet', 'wearFeedback'     |
       |  Preferences Doc: 'user_preferences/...'    |
       +------▲------------------------------│-------+
              │                              │
     Retrieves retrained             Triggers document
     harmony guidelines              written actions
              │                              │
       +------┴------------------------------▼-------+
       |          FIREBASE CLOUD FUNCTIONS           |
       |    TypeScript / Node.js Admin SDK Runtime   |
       +---------------------------------------------+
```

### Architectural Pillars:
1. **The Native Frontend Client (Android):** Built in modern Kotlin utilizing Jetpack Compose, structured under a strict MVVM (Model-View-ViewModel) design paradigm. State propagation is managed through unified Kotlin Co-routine `StateFlow` streams, ensuring rendering loops are detached from processing states.
2. **The Cloud Storage Engine (Cloud Firestore):** Outfitted with offline-persistence caching layers. All active wardrobe records, laundry queues, and styling logs sync instantly.
3. **The Distributed Cloud Compute (Firebase Cloud Functions):** Executed on a production-grade TypeScript Node.js runtime. Whenever a lookback contains ratings, distributed cloud functions reconstruct the history pattern, calculate preference centroids, and rebalance styling suggestions.
4. **Atmospheric Satellite Core (OpenWeatherMap API):** Connects to meteorological APIs, parsing local climatic parameters (temp, rains, winds) to dynamically gate matching garments. Supported by an advanced memory caching system and error-buffering mechanics to handle API key limits or offline use gracefully.

---

## 📂 2. Core Functionality & The Identifier System

The foundation of Sartorial Aura is its **Garment Identifier System**, which models garments not as simple text items, but as rich data objects tracking real physical parameters.

```kotlin
data class ClothingItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String, // Top, Bottom, Footwear, Outerwear
    val colorName: String, // e.g., "Sienna Clay", "Indigo Ink"
    val colorHex: String,
    val fabric: String, // Cotton, Wool, Linen, Silk, Leather, Synth
    val weight: String, // Light, Medium, Heavy
    val silhouette: String, // Slim, Straight, Oversized, Tailored
    val modestyLevel: Int, // 1 to 5 (Modesty Coverage Scale)
    val laundryStatus: String, // AVAILABLE, DIRTY, IN_LAUNDRY
    val wearCount: Int = 0,
    val lastWornTimestamp: Long? = null
)
```

### Inside the Identifier System:
*   **Color DNA:** Every garment maps to a specific hex representation and human-labeled color category (e.g., Earthy, Monochromatic, Jewel, Pastel). The Styling Engine uses these values to calculate contrast indices and color complementarity scores.
*   **Textile Fingerprint:** Evaluates the garment's raw tactile fabric properties (`Cotton`, `Wool`, `Linen`, `Silk`, `Suede`, `Leather`, `Denim`). This prevents styling clashing and regulates breathability or thermal retention.
*   **Design Silhouette:** Classifies the overall structural shape (`Slim`, `Straight`, `Oversized`, `Tailored`). This is crossed with layering algorithms to ensure outfits adhere to elegant proportion rules (e.g., pairing slim bases with relaxed outerwear).
*   **Modesty Metrics:** Users can assign structured modesty parameters (scale of 1-5). Activating strategies such as **Sunday Church** or **Business Confidential** filters out any items falling below designated threshold values.
*   **Laundry Logistics & Hub:** Tracks if items are clean (`AVAILABLE`), dirty (`DIRTY`), or being washed (`IN_LAUNDRY`). SARTORIAL AURA warns of missing layers in suggestions if essential garments are pending laundry processing.

---

## 🧠 3. The Outfit Scoring Engine (Styling Logistics)

The core styling intelligence is written in `StylingEngine.kt`. This algorithm scores every possible clothing combination through a multi-dimensional matrix, producing a comprehensive **Power Score**:

$$\text{Power Score} = (\text{Harmony} \times 0.35) + (\text{Occasion} \times 0.25) + (\text{Weather} \times 0.20) + (\text{Wear Balance} \times 0.20)$$

```
                           +----------------------+
                           |   CLOTHING COMBOS    |
                           +----------│-----------+
                                      │
                 +────────────────────┼────────────────────+
                 ▼                    ▼                    ▼
       Color Harmony (45%)    Occasion Fit (25%)    Climate Match (20%)
       Textile Flow  (30%)    Silhouettes  (20%)    Wear Balance  (15%)
                 │                    │                    │
                 +────────────────────┼────────────────────+
                                      │
                                      ▼
                           +----------------------+
                           | MAXIMUM POWER SCORE  |
                           +----------------------+
```

### Component Breakdown:

### 1. Harmony Score (Weight: 35%)
The overall Harmony is derived from three sub-parameters:
*   **Color Harmony (45% of component):** Uses expert color-pairing ratios. Complimentary, monochromatic, or neutral-contrast pairings score higher. Low-contrast combinations are flagged as dull, whereas discordances trigger warnings.
*   **Texture Contrast (30% of component):** Scores how fabrics pair. Heavy materials (Leather, Denim) are paired with lighter textures (Cotton, Silk) to establish elegant structural contrast, while flat combinations (all synthetic polyester blends) receive penalties.
*   **Silhouette Layering (25% of component):** Validates proportions. Oversized items are balanced with slim companion pieces. Heavy outerwear is strictly layered on top of light-to-medium base garments.

### 2. Occasion Relevance (Weight: 25%)
Scores how closely an outfit adheres to the style rules of selected events:
*   `Sunday Church`: Prioritizes structural layering, formal tailoring, and mandates high clothing modesty settings (minimum level 4/5).
*   `Museum Wanderings`: Promotes unique silhouettes, oversized linen structures, and high texture-contrast parameters.
*   `Business Confidential`: Demands precise tailoring, minimal neutral color palettes, medium weight garments, and high-modesty cuts.
*   `Leisure Lounge`: Favors comfort fabrics (Cotton), relaxed oversized structures, and low-contrast color models.

### 3. Climate Compatibility (Weight: 20%)
Cross-checks meteorological conditions (temperature, precipitation indices, wind-chill values) against clothing types to calculate thermal compatibility:
*   Heavy wool knits and thick leather layers score exceptionally high in temperatures below $12^\circ\text{C}$ but are excluded in warm weather to keep you comfortable.
*   Breathable crinkled linen tops score maximum layout points when the temperature exceeds $24^\circ\text{C}$ but are restricted during frosted winters.

### 4. Wear Frequency Balancing (Weight: 20%)
A core slow-fashion directive designed to prevent overusing identical outfits and elevate forgotten garments:
*   **Anti-Uniform Penalty:** Appreciably lowers the score of outfits containing items with high wear records.
*   **Remix Optimization Boost:** Offers significant point boosts to forgotten pieces (low wear counts) to encourage creative wardrobe reuse.

---

## 🌦️ 4. Microclimate Filters & Centralized Fault Resilience

Sartorial Aura prevents clothing damage and styling discomfort via a persistent climate intelligence layer powered by the **OpenWeatherMap API**:

```
                  +-----------------------------------+
                  |      OpenWeatherMap API Call      |
                  +-----------------│-----------------+
                                    ▼
                          Is API Key / Net OK?
                         /                    \
                       YES                     NO
                       /                         \
         +------------▼-------------+     +-------▼------------------+
         | Parse Remote Coordinates |     | Centralized Error Logger |
         |   & Cache Current City   |     +-----------│--------------+
         +------------│-------------+                 ▼
                      │                  Retrieve Cached Weather
                      │                  or Default Autumn Breeze
                      │                               │
                      +───────────────┬───────────────+
                                      ▼
                        +───────────────────────────+
                        |   CLIMATE FILTER STAGE    |
                        |   Gate Fabrics & Weights  |
                        +───────────────────────────+
```

### The Microclimate Gating System:
1.  **Thermodynamic Thresholds:**
    - If Temperature $> 22^\circ\text{C}$: Excludes heavy outerwear, heavy knits, and thick wool.
    - If Temperature $< 12^\circ\text{C}$: Excludes linen shorts, thin silks, or standalone short-sleeve tees. Outerwear is mandated.
2.  **Precipitation Protection Overrides:**
    - If `isRaining` starts: The system detects water risk and isolates water-vulnerable fabrics (such as Suede and Untreated Leather). Rain protectors (synthetic windbreakers) receive high suitability weights.

### Resilient Local Cache Fallback:
To protect the app's styling capabilities during network outages, api rate-limiting, or missing credentials:
*   **Centralized Error Logger:** `WeatherService.kt` registers errors, request timeouts, and exception traces locally for quick diagnostics.
*   **City Memory Caching:** Saves successfully fetched weather profiles in memory for queried cities.
*   **Persistent Global Fallback:** If internet is unavailable or limits are reached, the app falls back to the last known weather profile. If no historical cache exists, a comfortable "Gentle Autumn Breeze" weather profile is automatically applied, ensuring styling recommendations remain functional.
*   **Visual Status Indicators:** Fallback weather profiles are visually labeled in the UI (e.g., *“Gentle Autumn Breeze (Cache Fallback)”*) to alert the user of local cache usage.

---

## 🔄 5. Double-Loop Style Learning Engine

The styling algorithms adapt to your preferences over time through a high-performance **Double-Loop Style Learning Engine** that triggers on 'wearFeedback' writes.

```
       +---------------------------------------------+
       |             USER FEEDBACK ICON              |
       |  ✨ Sublime (+15) / 🙂 Balanced / 😕 Recalibrate |
       +----------------------│----------------------+
                              ▼
       +---------------------------------------------+
       |         FIRESTORE 'wearFeedback' WRITE      |
       +----------------------│----------------------+
                              ▼
                 +────────────┴────────────+
                 ▼                         ▼
       +───────────────────+     +───────────────────+
       |   LOOP 1: LOCAL   |     |   LOOP 2: CLOUD   |
       |  Real-time client |     | Firebase Cloud    |
       |  weight updates   |     | Transact-Weights  |
       +───────────────────+     +───────────────────+
```

### Loop 1: Immediate Client Realignment (The Local Engine)
When an outfit lookback is logged, the styling engine immediately updates feedback factors inside the local runtime:
- **✨ Sublime (Extraordinary Resonance):** Applies a $+15$ point resonance boost to styled components in future scoring.
- **🙂 Balanced (High Composure):** Applies a $+5$ point alignment adjustment.
- **😕 Recalibrate (Correction Filter):** Applies a $-20$ or $-25$ point protective penalty to de-prioritize uncomfortable combinations or styles.

### Loop 2: Enterprise Cloud Model Retraining (Transaction Script)
Firestore Cloud Functions listen for new writes to the `wearFeedback` collection and execute background model updates:
1.  **`retrainHarmonyModel` (`functions/src/index.ts`):** 
    - Analyzes the full historical record of logged reviews.
    - Recomputes relative preference scores for clothing garments.
    - Persists normalized weights back to `/outfitModel/trained_weights` in Firestore.
2.  **`updateUserPreferencesHarmony` (`functions/src/index.ts`):**
    - Executes clean, ACID-compliant Transactions to update the `/user_preferences/harmony_weights` document.
    - Directly increments or decrements the style weights of worn item IDs by feedback-ratio variables (bounded safely within a stable $-50$ to $+50$ scoring envelope).
    - Guarantees seamless synchronization of personal style preferences across all user devices.

---

## ✨ 6. Enhancing Daily Life: User Guide & Rituals

Integrate SARTORIAL AURA into your daily routines to simplify logistics, express personal style, and reduce wardrobe fatigue.

### 🌅 The Morning Curation (Ritual 1)
1. **Open SARTORIAL AURA** as you wake. The background matches the local sun cycle, fading in serene typography.
2. **Select Your Strategy**: Tap on the **Occasions** list. Select your strategy (e.g., *Business Confidential*, *Museum Wanderings*, *Sunday Church*). 
   - *Note: Activating **Sunday Church** automatically filters out items that do not meet your customized modesty settings.*
3. **Witness Curation**: The system evaluates available items against local meteorological satellites and serves the top 3 high-contrast, color-DNA-matched ensembles with precise **Power Scores**.
4. **Confirm the Fit**: Select a suggestion and tap **Wear Outfit** to mark those specific garments as "Worn today", automatically updating their laundry and wear counters.

### 🌃 The Evening "Lookback" (Ritual 2)
1. **Reflect on the Fit**: Open the app before bed.
2. **Submit Feedback**: Rate your chosen outfit's feedback selector (**✨ Sublime / 🙂 Balanced / 😕 Recalibrate**).
3. **Watch the AI Adjust**: The styling engine immediately registers your preferences, subtly realigning the model so tomorrow's curated choices fit your comfort perfectly.

### 🔄 Sustainable Circle (Ritual 3)
1. **Weekly Remix**: If you feel you are wearing the same pieces repeatedly, head to the **The Remix Engine** screen.
2. **Unearth Hidden Gems**: The algorithm isolates complementary but neglected clothes (garments with low wear counts) and recommends unique, color-balanced configurations.

---

## 🛠️ 7. Technical Specifications & Compilation

SARTORIAL AURA represents a fully production-optimized native Android Jetpack Compose codebase.

### Requirements:
*   **SDK Target:** `targetSdk = 34` (Android 14) / `minSdk = 21` (Lollipop)
*   **Tablet Optimization:** Added `<supports-screens>` bounds inside `AndroidManifest.xml` targeting displays `requiresSmallestWidthDp="600"`.
*   **Production Safety:** Set `isDebuggable = false` for release gradle configs with custom ProGuard optimization rule sets to completely strip verbose Logging parameters (`android.util.Log.d`/`.v`) automatically.

### Commands to Build:
```bash
# Clean and compile Jetpack Compose classes
./gradlew compileDebugKotlin

# Assemble development debug APK
./gradlew assembleDebug
```

---
*Sartorial Aura — Curating sustainable wardrobes through visual geometry, conscious wear-history, and absolute aesthetic rhythm.*
