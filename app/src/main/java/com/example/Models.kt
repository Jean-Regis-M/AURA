package com.example

enum class ClothingType {
    SHIRT, PANTS, SHOES, OUTERWEAR, ACCESSORY
}

enum class CleaningStatus {
    AVAILABLE, DIRTY, WASHING, DRYING, IRONING
}

enum class SilhouetteType {
    FITTED, RELAXED, OVERSIZED, TAPERED, FLARED
}

enum class TextureType(val displayName: String) {
    SMOOTH_MATTE("Matte Smooth"),
    ROUGH_MATTE("Matte Rough"),
    GLOSSY("Glossy"),
    RIBBED("Ribbed"),
    KNIT("Knit"),
    DENIM("Denim"),
    LEATHER("Leather")
}

data class ColorDNA(
    val hex: String,
    val name: String,
    val hue: Int, // 0-360
    val saturation: Int, // 0-100
    val lightness: Int // 0-100
)

data class ClothingItem(
    val id: String,
    val name: String,
    val type: ClothingType,
    val color: ColorDNA,
    val silhouette: SilhouetteType,
    val texture: TextureType,
    val weightClass: String, // Light, Medium, Heavy
    val fabricComposition: String, // e.g. "100% Cotton"
    val occasionTags: List<String>, // e.g., "Work", "Casual", "Church", "Special"
    val formalityLevel: Int, // 1-10
    val modestFlag: Boolean = false,
    val waterResistant: Boolean = false,
    val wearCount: Int = 0,
    val lastWornDate: Long? = null, // timestamp
    val cleaningStatus: CleaningStatus = CleaningStatus.AVAILABLE,
    val imageIndex: Int = 0 // Mock visual asset coloring index
)

data class Occasion(
    val id: String,
    val name: String,
    val date: Long,
    val formalityLevel: Int,
    val desiredVibe: String,
    val isSundayChurch: Boolean = false,
    val selectedOutfitIds: List<String> = emptyList()
)

data class WeatherInfo(
    val temp: Int,
    val isRaining: Boolean = false,
    val humidity: Int = 50,
    val windSpeed: Int = 10,
    val description: String = "Clear Sunset"
)

data class BodyProfile(
    val height: Int = 180, // cm
    val shoulderWidth: String = "Medium",
    val bodyShape: String = "Athletic",
    val skinTone: String = "Warm Sand",
    val energyStyle: String = "Joyful & Energetic"
)

data class OutfitSuggestion(
    val id: String,
    val items: List<ClothingItem>,
    val powerScore: Int,
    val harmonyReason: String,
    val reasons: List<String>
)

data class OutfitFeedback(
    val id: String = "",
    val outfitId: String = "",
    val items: List<String> = emptyList(),
    val rating: String = "",
    val score: Int = 0,
    val timestamp: Long = 0L,
    val weatherTemp: Int = 0,
    val weatherDesc: String = ""
)
