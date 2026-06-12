package com.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuraViewModel : ViewModel() {

    private val _wardrobe = MutableStateFlow(StylingEngine.initialWardrobe)
    val wardrobe = _wardrobe.asStateFlow()

    private val _weather = MutableStateFlow(WeatherInfo(temp = 18, isRaining = false, humidity = 45, windSpeed = 12, description = "Gentle Autumn Breeze"))
    val weather = _weather.asStateFlow()

    private val _occasions = MutableStateFlow(listOf(
        Occasion("occ_1", "University Lecture Series", System.currentTimeMillis(), 6, "Focused, smart but casual"),
        Occasion("occ_2", "Sunday Morning Divine Service", System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L, 8, "Respectful & Modest, Church Sunday vibe", isSundayChurch = true),
        Occasion("occ_3", "Internship Presentation", System.currentTimeMillis() + 4 * 24 * 60 * 60 * 1000L, 9, "Elegant, commanding, professional")
    ))
    val occasions = _occasions.asStateFlow()

    private val _selectedOccasion = MutableStateFlow<Occasion?>(null)
    val selectedOccasion = _selectedOccasion.asStateFlow()

    private val _bodyProfile = MutableStateFlow(BodyProfile())
    val bodyProfile = _bodyProfile.asStateFlow()

    private val _outfits = MutableStateFlow<List<OutfitSuggestion>>(emptyList())
    val outfits = _outfits.asStateFlow()

    private val _remixes = MutableStateFlow<List<OutfitSuggestion>>(emptyList())
    val remixes = _remixes.asStateFlow()

    private val _feedbacks = MutableStateFlow<List<OutfitFeedback>>(emptyList())
    val feedbacks = _feedbacks.asStateFlow()

    // Interactive scanner state
    var isScanning by mutableStateOf(false)
        private set
    var scanningPhase by mutableStateOf("")
        private set

    // Selected navigation tab
    var currentTab by mutableStateOf("TODAY")

    // UI Toast indicators
    var toastMessage by mutableStateOf<String?>(null)

    init {
        _selectedOccasion.value = _occasions.value.first()
        loadFeedbacks()
    }

    fun loadFeedbacks() {
        FirebaseFeedbackManager.fetchAllFeedback { list ->
            _feedbacks.value = list
            recalculateOutfits()
        }
    }

    fun showToast(msg: String) {
        toastMessage = msg
        viewModelScope.launch {
            delay(2500)
            if (toastMessage == msg) {
                toastMessage = null
            }
        }
    }

    fun recalculateOutfits() {
        _outfits.value = StylingEngine.generateTopSuggestions(
            items = _wardrobe.value,
            weather = _weather.value,
            occasion = _selectedOccasion.value ?: Occasion("default", "Regular Day", System.currentTimeMillis(), 5, "Elegance as standard"),
            profile = _bodyProfile.value,
            feedbacks = _feedbacks.value
        )
        _remixes.value = StylingEngine.generateRemixSuggestions(
            items = _wardrobe.value,
            weather = _weather.value,
            profile = _bodyProfile.value,
            feedbacks = _feedbacks.value
        )
    }

    fun rateOutfit(outfitId: String, itemsIds: List<String>, ratingEmoji: String, score: Int) {
        val fb = OutfitFeedback(
            id = "FB_${System.currentTimeMillis()}",
            outfitId = outfitId,
            items = itemsIds,
            rating = ratingEmoji,
            score = score,
            timestamp = System.currentTimeMillis(),
            weatherTemp = _weather.value.temp,
            weatherDesc = _weather.value.description
        )
        FirebaseFeedbackManager.submitFeedback(fb, onSuccess = {
            loadFeedbacks() // Real-time reload retrains the AI harmony model immediately
            showToast("Aura realigned with rating '$ratingEmoji'!")
        }, onFailure = {
            showToast("Offline local realignment established.")
        })
    }

    fun selectOccasion(occ: Occasion) {
        _selectedOccasion.value = occ
        recalculateOutfits()
    }

    fun addOccasion(name: String, desiredVibe: String, formality: Int, isSunday: Boolean) {
        val newOcc = Occasion(
            id = "occ_${System.currentTimeMillis()}",
            name = name,
            date = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
            formalityLevel = formality,
            desiredVibe = desiredVibe,
            isSundayChurch = isSunday
        )
        _occasions.value = _occasions.value + newOcc
        _selectedOccasion.value = newOcc
        recalculateOutfits()
        showToast("Occasion '$name' scheduled - outfit reserves allocated.")
    }

    fun setWeatherMode(presetName: String) {
        _weather.value = when (presetName) {
            "Warm Summer" -> WeatherInfo(temp = 28, isRaining = false, humidity = 40, windSpeed = 8, description = "Vibrant Sunshine")
            "Cool Breeze" -> WeatherInfo(temp = 17, isRaining = false, humidity = 50, windSpeed = 15, description = "Crisp Overcast")
            "Rainy Solitude" -> WeatherInfo(temp = 11, isRaining = true, humidity = 85, windSpeed = 22, description = "Windswept Showers")
            "Winter Freeze" -> WeatherInfo(temp = 5, isRaining = false, humidity = 30, windSpeed = 18, description = "Brisk Frosted Dusk")
            else -> WeatherInfo(temp = 20, isRaining = false, humidity = 50, windSpeed = 10, description = "Gentle Serenity")
        }
        recalculateOutfits()
        showToast("Weather synchronized: ${_weather.value.temp}°C, ${_weather.value.description}")
    }

    fun queryLiveOpenWeather(city: String) {
        if (city.isBlank()) {
            showToast("Sartorial scan aborted: City name is blank.")
            return
        }
        viewModelScope.launch {
            val apiKey = com.example.BuildConfig.OPENWEATHER_API_KEY
            if (apiKey.isBlank() || apiKey == "PLACEHOLDER_OPENWEATHER_KEY") {
                showToast("AURA Key Missing: Please assign OPENWEATHER_API_KEY in Secrets.")
                return@launch
            }
            isScanning = true
            scanningPhase = "Interrogating satellite streams for $city..."
            delay(1200)
            
            val result = WeatherService.fetchWeather(city, apiKey)
            isScanning = false
            scanningPhase = ""
            
            result.onSuccess { info ->
                _weather.value = info
                recalculateOutfits()
                showToast("Atmosphere synchronized: $city is ${info.temp}°C · ${info.description}")
            }.onFailure { err ->
                showToast("Atmospheric scan failed: ${err.message}")
            }
        }
    }

    fun progressLaundry(item: ClothingItem, targetStatus: CleaningStatus) {
        _wardrobe.value = _wardrobe.value.map {
            if (it.id == item.id) {
                it.copy(cleaningStatus = targetStatus)
            } else {
                it
            }
        }
        recalculateOutfits()
        showToast("'${item.name}' moved to ${targetStatus.name.lowercase().replaceFirstChar { it.uppercase() }}")
    }

    fun wearOutfit(outfit: OutfitSuggestion) {
        // Wear the outfit: mark all items worn as DIRTY, increment wearCount, update date
        val wornIds = outfit.items.map { it.id }.toSet()
        _wardrobe.value = _wardrobe.value.map {
            if (wornIds.contains(it.id)) {
                it.copy(
                    wearCount = it.wearCount + 1,
                    lastWornDate = System.currentTimeMillis(),
                    cleaningStatus = CleaningStatus.DIRTY // Laundry intelligence: moves worn shirts, pants & socks to DIRTY!
                )
            } else {
                it
            }
        }
        recalculateOutfits()
        showToast("Look registered! Clothes transitioned to Laundry Queue.")
    }

    fun updateBodyProfile(height: Int, shoulders: String, shape: String, skinTone: String, energy: String) {
        _bodyProfile.value = BodyProfile(
            height = height,
            shoulderWidth = shoulders,
            bodyShape = shape,
            skinTone = skinTone,
            energyStyle = energy
        )
        recalculateOutfits()
        showToast("Body Proportions updated. AI Styling coordinates realigned.")
    }

    fun ingestNewGarment(
        rawName: String,
        type: ClothingType,
        colorHex: String,
        colorName: String,
        silhouette: SilhouetteType,
        texture: TextureType,
        weightClass: String,
        fabric: String,
        formality: Int,
        isModest: Boolean
    ) {
        viewModelScope.launch {
            isScanning = true
            scanningPhase = "Interrogating Garment Vision Spectrums..."
            delay(1000)
            scanningPhase = "Decomposing Color DNA Undertones..."
            delay(800)
            scanningPhase = "Synthesizing Silhouette Geometries..."
            delay(800)
            scanningPhase = "Evaluating Fabric Thermal Coefficiency ($weightClass)..."
            delay(600)

            val colors = mapOf(
                "#991B1B" to ColorDNA(hex = "#991B1B", "Ruby Crimson", 0, 70, 40),
                "#7C2D12" to ColorDNA(hex = "#7C2D12", "Sienna Clay", 15, 60, 30),
                "#D97706" to ColorDNA(hex = "#D97706", "Saffron Gold", 42, 85, 50),
                "#065F46" to ColorDNA(hex = "#065F46", "Emerald Forest", 162, 80, 25),
                "#1D4ED8" to ColorDNA(hex = "#1D4ED8", "Classic Cobalt", 224, 75, 48),
                "#4B5563" to ColorDNA(hex = "#4B5563", "Granite Gray", 215, 15, 35),
                "#F3F4F6" to ColorDNA(hex = "#F3F4F6", "Pearl Alabaster", 0, 0, 96)
            )

            val computedColor = colors[colorHex] ?: ColorDNA(hex = colorHex, colorName, 200, 50, 50)

            val newItem = ClothingItem(
                id = "CL_${System.currentTimeMillis()}",
                name = rawName.ifBlank { "${computedColor.name} ${type.name.lowercase().replaceFirstChar { it.uppercase() }}" },
                type = type,
                color = computedColor,
                silhouette = silhouette,
                texture = texture,
                weightClass = weightClass,
                fabricComposition = fabric.ifBlank { "100% Cotton" },
                occasionTags = listOf("Casual", "Work", "Special", "Church"),
                formalityLevel = formality,
                modestFlag = isModest,
                waterResistant = (type == ClothingType.SHOES || type == ClothingType.OUTERWEAR),
                wearCount = 0,
                lastWornDate = null,
                cleaningStatus = CleaningStatus.AVAILABLE,
                imageIndex = (1..6).random()
            )

            _wardrobe.value = _wardrobe.value + newItem
            isScanning = false
            scanningPhase = ""
            recalculateOutfits()
            showToast("'${newItem.name}' successfully cataloged in Cloak.")
        }
    }
}
