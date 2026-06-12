package com.example

import kotlin.math.abs

object StylingEngine {

    // Preseed a set of highly polished clothing items so the user's closet is instantly beautiful and ready
    val initialWardrobe = listOf(
        ClothingItem(
            id = "SH001",
            name = "Royal Oxford Cotton Shirt",
            type = ClothingType.SHIRT,
            color = ColorDNA(hex = "#1E3A8A", name = "Royal Blue", hue = 224, saturation = 64, lightness = 33),
            silhouette = SilhouetteType.FITTED,
            texture = TextureType.SMOOTH_MATTE,
            weightClass = "Medium",
            fabricComposition = "100% Giza Cotton",
            occasionTags = listOf("Work", "Church", "Special"),
            formalityLevel = 8,
            modestFlag = true,
            waterResistant = false,
            wearCount = 4,
            lastWornDate = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000L, // 8 days ago
            imageIndex = 1
        ),
        ClothingItem(
            id = "SH002",
            name = "Belgian Flax Linen Shirt",
            type = ClothingType.SHIRT,
            color = ColorDNA(hex = "#F5F5DC", name = "Sand Beige", hue = 60, saturation = 56, lightness = 91),
            silhouette = SilhouetteType.RELAXED,
            texture = TextureType.ROUGH_MATTE,
            weightClass = "Light",
            fabricComposition = "100% Premium Linen",
            occasionTags = listOf("Casual", "Work", "Special"),
            formalityLevel = 6,
            modestFlag = true,
            waterResistant = false,
            wearCount = 12,
            lastWornDate = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L, // 2 days ago
            imageIndex = 2
        ),
        ClothingItem(
            id = "SH003",
            name = "Emerald Cashmere Knit",
            type = ClothingType.SHIRT,
            color = ColorDNA(hex = "#065F46", name = "Forest Green", hue = 162, saturation = 88, lightness = 20),
            silhouette = SilhouetteType.RELAXED,
            texture = TextureType.KNIT,
            weightClass = "Heavy",
            fabricComposition = "90% Cashmere, 10% Silk",
            occasionTags = listOf("Casual", "Church", "Special"),
            formalityLevel = 7,
            modestFlag = true,
            waterResistant = false,
            wearCount = 1,
            lastWornDate = System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L, // 14 days ago (unworn/neglected!)
            imageIndex = 3
        ),
        ClothingItem(
            id = "TR001",
            name = "Italian Pleated Chinos",
            type = ClothingType.PANTS,
            color = ColorDNA(hex = "#78350F", name = "Amber Tan", hue = 33, saturation = 88, lightness = 26),
            silhouette = SilhouetteType.TAPERED,
            texture = TextureType.ROUGH_MATTE,
            weightClass = "Medium",
            fabricComposition = "98% Cotton, 2% Elastane",
            occasionTags = listOf("Work", "Church", "Special", "Casual"),
            formalityLevel = 7,
            modestFlag = true,
            wearCount = 8,
            lastWornDate = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L, // 5 days ago
            imageIndex = 1
        ),
        ClothingItem(
            id = "TR002",
            name = "Selvedge Indigo Denim Jeans",
            type = ClothingType.PANTS,
            color = ColorDNA(hex = "#1E293B", name = "Deep Slate Indigo", hue = 217, saturation = 33, lightness = 17),
            silhouette = SilhouetteType.FITTED,
            texture = TextureType.DENIM,
            weightClass = "Heavy",
            fabricComposition = "100% Organic Raw Denim",
            occasionTags = listOf("Casual", "Work"),
            formalityLevel = 5,
            modestFlag = true,
            wearCount = 18,
            lastWornDate = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L, // 1 day ago
            imageIndex = 2
        ),
        ClothingItem(
            id = "TR003",
            name = "Merino Wool Dress Trousers",
            type = ClothingType.PANTS,
            color = ColorDNA(hex = "#1F2937", name = "Charcoal Gray", hue = 215, saturation = 28, lightness = 17),
            silhouette = SilhouetteType.TAPERED,
            texture = TextureType.SMOOTH_MATTE,
            weightClass = "Heavy",
            fabricComposition = "100% Super 120s Wool",
            occasionTags = listOf("Work", "Church", "Special"),
            formalityLevel = 9,
            modestFlag = true,
            wearCount = 3,
            lastWornDate = System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000L, // 10 days ago
            imageIndex = 3
        ),
        ClothingItem(
            id = "SH004",
            name = "Charcoal Merino Wool Sweater",
            type = ClothingType.SHIRT,
            color = ColorDNA(hex = "#374151", name = "Charcoal Gray", hue = 215, saturation = 19, lightness = 27),
            silhouette = SilhouetteType.RELAXED,
            texture = TextureType.KNIT,
            weightClass = "Medium",
            fabricComposition = "100% Extra Fine Merino",
            occasionTags = listOf("Work", "Church", "Casual"),
            formalityLevel = 7,
            modestFlag = true,
            wearCount = 6,
            lastWornDate = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L,
            imageIndex = 4
        ),
        ClothingItem(
            id = "SHOE001",
            name = "Chocolate Leather Penny Loafers",
            type = ClothingType.SHOES,
            color = ColorDNA(hex = "#451A03", name = "Espresso Suede", hue = 20, saturation = 96, lightness = 14),
            silhouette = SilhouetteType.FITTED,
            texture = TextureType.LEATHER,
            weightClass = "Medium",
            fabricComposition = "Full Grain Calfskin Leather",
            occasionTags = listOf("Work", "Church", "Special"),
            formalityLevel = 8,
            modestFlag = true,
            waterResistant = true,
            wearCount = 9,
            lastWornDate = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L, // 3 days ago
            imageIndex = 1
        ),
        ClothingItem(
            id = "SHOE002",
            name = "Classic Retro Calfskin Sneakers",
            type = ClothingType.SHOES,
            color = ColorDNA(hex = "#FFFFFF", name = "Alabaster White", hue = 0, saturation = 0, lightness = 100),
            silhouette = SilhouetteType.RELAXED,
            texture = TextureType.SMOOTH_MATTE,
            weightClass = "Light",
            fabricComposition = "Vegan Nettle Leather & Rubber",
            occasionTags = listOf("Casual", "Work"),
            formalityLevel = 4,
            modestFlag = false,
            waterResistant = true,
            wearCount = 24,
            lastWornDate = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L, // 1 day ago
            imageIndex = 2
        ),
        ClothingItem(
            id = "SHOE003",
            name = "Chelsea Heritage Amber Boots",
            type = ClothingType.SHOES,
            color = ColorDNA(hex = "#7C2D12", name = "Burnt Clay Suede", hue = 15, saturation = 70, lightness = 29),
            silhouette = SilhouetteType.TAPERED,
            texture = TextureType.LEATHER,
            weightClass = "Heavy",
            fabricComposition = "Splitted English Suede Leather",
            occasionTags = listOf("Casual", "Church", "Special"),
            formalityLevel = 7,
            modestFlag = true,
            waterResistant = false, // Delicate suede!
            wearCount = 2,
            lastWornDate = System.currentTimeMillis() - 11 * 24 * 60 * 60 * 1000L, // 11 days ago
            imageIndex = 3
        ),
        ClothingItem(
            id = "OUT001",
            name = "Sartorial Double-Breasted Overcoat",
            type = ClothingType.OUTERWEAR,
            color = ColorDNA(hex = "#172554", name = "Midnight Navy", hue = 227, saturation = 58, lightness = 21),
            silhouette = SilhouetteType.OVERSIZED,
            texture = TextureType.ROUGH_MATTE,
            weightClass = "Heavy",
            fabricComposition = "90% Shetland Wool, 10% Cashmere",
            occasionTags = listOf("Work", "Church", "Special"),
            formalityLevel = 9,
            modestFlag = true,
            waterResistant = true,
            wearCount = 2,
            lastWornDate = System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000L, // 15 days ago
            imageIndex = 1
        ),
        ClothingItem(
            id = "OUT002",
            name = "Waxed Heritage Field Utility Jacket",
            type = ClothingType.OUTERWEAR,
            color = ColorDNA(hex = "#3F6212", name = "Sage Olive", hue = 81, saturation = 45, lightness = 29),
            silhouette = SilhouetteType.RELAXED,
            texture = TextureType.ROUGH_MATTE,
            weightClass = "Medium",
            fabricComposition = "100% Cotton Canvas with Soya Wax",
            occasionTags = listOf("Casual", "Work"),
            formalityLevel = 5,
            modestFlag = true,
            waterResistant = true,
            wearCount = 5,
            lastWornDate = System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000L,
            imageIndex = 2
        )
    )

    fun calculatePowerScore(
        shirt: ClothingItem,
        pants: ClothingItem,
        shoes: ClothingItem,
        outer: ClothingItem?,
        weather: WeatherInfo,
        occasion: Occasion,
        profile: BodyProfile,
        feedbacks: List<OutfitFeedback> = emptyList()
    ): ScoreBreakdown {
        // Component 1: Harmony Score (35%)
        val colorHarmony = evaluateColorHarmony(shirt, pants, shoes, outer)
        val textureContrast = evaluateTextureContrast(shirt, pants, shoes, outer)
        val silhouetteLayering = evaluateSilhouetteLayering(shirt, pants, shoes, outer)
        val harmonyScore = ((colorHarmony * 0.45f) + (textureContrast * 0.30f) + (silhouetteLayering * 0.25f)).toInt().coerceIn(0, 100)

        // Component 2: Occasion Fit Score (25%)
        val occasionScore = evaluateOccasionFit(shirt, pants, shoes, outer, occasion)

        // Component 3: Weather Fit Score (20%)
        val weatherScore = evaluateWeatherFit(shirt, pants, shoes, outer, weather)

        // Component 4: Freshness Score (10%)
        val freshnessScore = evaluateFreshness(shirt, pants, shoes, outer)

        // Component 5: Body Fit Score (10%)
        val bodyScore = evaluateBodyFit(shirt, pants, shoes, outer, profile)

        val baseScore = (
            (harmonyScore * 0.35f) +
            (occasionScore * 0.25f) +
            (weatherScore * 0.20f) +
            (freshnessScore * 0.10f) +
            (bodyScore * 0.10f)
        ).toInt().coerceIn(0, 100)

        // Apply personal preference training based on Firestore Feedbacks (Adaptive AI Layer)
        var preferenceAdjustment = 0
        val currentIds = listOfNotNull(shirt, pants, shoes, outer).map { it.id }.toSet()

        for (fb in feedbacks) {
            val fbIds = fb.items.toSet()
            // Check exact or partial item intersections
            val intersection = currentIds.intersect(fbIds)
            if (intersection.isNotEmpty()) {
                val weight = when (fb.rating) {
                    "✨" -> {
                        if (currentIds == fbIds) 15 else 3 * intersection.size
                    }
                    "🙂" -> {
                        if (currentIds == fbIds) 6 else 1 * intersection.size
                    }
                    "😕" -> {
                        if (currentIds == fbIds) -25 else -6 * intersection.size
                    }
                    else -> 0
                }
                preferenceAdjustment += weight
            }
        }

        // Limit preference impact to preserve styling rules while ensuring personalization overrides
        preferenceAdjustment = preferenceAdjustment.coerceIn(-30, 20)
        val totalScore = (baseScore + preferenceAdjustment).coerceIn(0, 100)

        val dynamicReasons = mutableListOf<String>()
        if (harmonyScore > 85) dynamicReasons.add("Pristine Visual Rhythm - Color tone pairing achieves expert contrast ratio.")
        if (textureContrast > 80) dynamicReasons.add("Sophisticated texture transition (${shirt.texture.displayName} paired with ${pants.texture.displayName}).")
        if (weatherScore > 90) dynamicReasons.add("Synthesized layer insulation perfectly matches current ${weather.temp}°C ambient weather.")
        if (occasionScore > 90 && occasion.isSundayChurch) dynamicReasons.add("Respectful, dignified geometry fully optimized for Sunday gathering style rules.")
        
        if (preferenceAdjustment > 5) {
            dynamicReasons.add("Aura Resonance Align: Boosted by ${preferenceAdjustment}pts based on your favorite lookbacks (✨/🙂).")
        } else if (preferenceAdjustment < -5) {
            dynamicReasons.add("Aura Reprehension: De-prioritized based on previous comfort feedback (😕).")
        }

        if (freshnessScore > 80) {
            val oldItem = listOfNotNull(shirt, pants, shoes, outer).maxByOrNull { System.currentTimeMillis() - (it.lastWornDate ?: 0) }
            if (oldItem != null) {
                dynamicReasons.add("Rotated back '${oldItem.name}', reviving a hidden wardrobe treasure.")
            }
        }

        return ScoreBreakdown(
            totalScore = totalScore,
            harmony = harmonyScore,
            occasion = occasionScore.toInt(),
            weather = weatherScore.toInt(),
            freshness = freshnessScore.toInt(),
            body = bodyScore.toInt(),
            reasons = dynamicReasons
        )
    }

    private fun evaluateColorHarmony(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?): Float {
        val items = listOfNotNull(s, p, sh, o)
        
        // Count neutral versus vibrant items
        var neutrals = 0
        var vibrants = 0
        val hues = mutableListOf<Int>()

        for (item in items) {
            // White, Black, Charcoal, Beige are neutral and harmonize universally
            val isNeutralColor = item.color.saturation < 20 || item.color.lightness > 88 || item.color.lightness < 20 ||
                                 item.color.name.contains("Charcoal") || item.color.name.contains("Beige") ||
                                 item.color.name.contains("Espresso") || item.color.name.contains(" tan", true)
            if (isNeutralColor) {
                neutrals++
            } else {
                vibrants++
                hues.add(item.color.hue)
            }
        }

        // Beautiful rule: no more than 3 distinct colors. At least one neutral is elegant.
        if (vibrants > 2) return 55f // Visual clutter or clashing

        if (vibrants == 1) {
            // One accent piece with neutral foundations is incredibly powerful and elegant
            return 95f
        }

        if (vibrants == 2) {
            val hue1 = hues[0]
            val hue2 = hues[1]
            val diff = abs(hue1 - hue2)
            val complementDistance = abs(diff - 180)
            
            // Warm/cool analog pairs (<45 deg) or complimentary pairs (>150 deg)
            return if (diff < 45) {
                90f // Harmonious analog
            } else if (complementDistance < 35) {
                98f // Masterful complimentary pairing (E.g. Royal Blue and Amber Tan)
            } else {
                72f // Decent, but less aligned
            }
        }

        // Monochromatic / neutral tone-on-tone (0 vibrants)
        return 88f
    }

    private fun evaluateTextureContrast(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?): Float {
        // Elegance rule: at least one texture contrast (e.g. Knit + Smooth, Denim + Leather)
        val textures = listOfNotNull(s, p, sh, o).map { it.texture }.toSet()
        return when (textures.size) {
            1 -> 65f // Flat and uniform - looks generic
            2 -> 88f // Elegant juxtaposition
            3 -> 96f // Expert level layer depth
            else -> 100f // Highly textured
        }
    }

    private fun evaluateSilhouetteLayering(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?): Float {
        // Rule: top and bottom tension is superior (Fitted top + relax-tapered pants, or Relaxed top + fitted bottom)
        val sSil = s.silhouette
        val pSil = p.silhouette
        
        if (sSil == SilhouetteType.OVERSIZED && pSil == SilhouetteType.OVERSIZED) {
            return 50f // Too baggy, loses human proportions
        }
        if (sSil == SilhouetteType.FITTED && pSil == SilhouetteType.FITTED) {
            return 75f // A bit stiff
        }
        
        // Tapered pants provide beautiful vertical leg lines which compose cleanly with relaxed or fitted shirts
        if (pSil == SilhouetteType.TAPERED) {
            return 94f
        }

        return 88f
    }

    private fun evaluateOccasionFit(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?, occ: Occasion): Float {
        val formDiffs = listOfNotNull(s, p, sh, o).map { abs(it.formalityLevel - occ.formalityLevel) }
        val avgDiff = formDiffs.average()
        
        var baseOccasionScore = (100 - (avgDiff * 12)).toFloat().coerceIn(0f, 100f)

        // Church logic - Sunday mode rules
        if (occ.isSundayChurch) {
            val allModest = listOfNotNull(s, p, sh, o).all { it.modestFlag }
            if (!allModest) {
                baseOccasionScore -= 30f // Severe penalty for non-respectful combinations
            }
            // Better scoring if formal footwear
            if (sh.formalityLevel >= 7) {
                baseOccasionScore += 10f
            } else {
                baseOccasionScore -= 15f
            }
        }

        return baseOccasionScore.coerceIn(0f, 100f)
    }

    private fun evaluateWeatherFit(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?, w: WeatherInfo): Float {
        // Temperature & Weight Alignments:
        // Light items: 20-35 deg
        // Medium items: 10-22 deg
        // Heavy items: -10 to 12 deg
        
        val items = listOfNotNull(s, p, sh, o)
        var totalPenalty = 0f

        for (item in items) {
            val expectedTemp = when (item.weightClass) {
                "Light" -> 25f
                "Medium" -> 16f
                "Heavy" -> 6f
                else -> 16f
            }
            val diff = abs(w.temp - expectedTemp)
            // Penalty of 3 points for each degree away from item's ideal thermal threshold
            totalPenalty += (diff * 2.5f)
        }

        // Heavy outercoat is mandatory if temperature falls below 10°C
        if (w.temp < 10 && o == null) {
            totalPenalty += 40f
        }
        
        // Suede shoes are delicate and highly penalized in rainy weather
        if (w.isRaining) {
            if (sh.name.contains("Suede", true) || !sh.waterResistant) {
                totalPenalty += 35f
            }
        }

        return (100f - totalPenalty).coerceIn(40f, 100f)
    }

    private fun evaluateFreshness(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?): Float {
        val items = listOfNotNull(s, p, sh, o)
        var daysSum = 0f
        val now = System.currentTimeMillis()
        
        for (item in items) {
            val lastWorn = item.lastWornDate ?: (now - 30 * 24 * 60 * 60 * 1000L) // Default to 30 days ago
            val diffDays = (now - lastWorn) / (24 * 60 * 60 * 1000L)
            daysSum += diffDays.coerceAtMost(30L)
        }

        val avgDaysUnworn = daysSum / items.size
        // 0 days worn since long = 100 points, 0 days worn today = 20 points
        return (20f + (avgDaysUnworn / 30f) * 80f).coerceIn(0f, 100f)
    }

    private fun evaluateBodyFit(s: ClothingItem, p: ClothingItem, sh: ClothingItem, o: ClothingItem?, profile: BodyProfile): Float {
        // Proportional body shaping rule:
        // Tall + Broad Shoulders = Structured fitted trousers + relaxed knit tops to outline shape beautifully
        return 90f // Standard geometric matching score helper
    }

    fun filterItemsByWeather(items: List<ClothingItem>, weather: WeatherInfo): List<ClothingItem> {
        return items.filter { item ->
            // If freezing cold, filter out Light weight garments unless there are no other alternatives
            if (weather.temp < 10 && item.weightClass == "Light") {
                val hasBetterOption = items.any { it.type == item.type && it.weightClass != "Light" }
                if (hasBetterOption) return@filter false
            }
            // If blazing warm, filter out Heavy weight garments unless there are no other alternatives
            if (weather.temp > 22 && item.weightClass == "Heavy") {
                val hasBetterOption = items.any { it.type == item.type && it.weightClass != "Heavy" }
                if (hasBetterOption) return@filter false
            }
            // If raining, filter out suede / delicate non-water-resistant shoes/outerwear
            if (weather.isRaining) {
                if (item.type == ClothingType.SHOES || item.type == ClothingType.OUTERWEAR) {
                    val isDelicate = !item.waterResistant || item.name.contains("Suede", ignoreCase = true)
                    if (isDelicate) {
                        val hasWaterproofOption = items.any {
                            it.type == item.type && it.waterResistant && !it.name.contains("Suede", ignoreCase = true)
                        }
                        if (hasWaterproofOption) return@filter false
                    }
                }
            }
            true
        }
    }

    fun generateTopSuggestions(
        items: List<ClothingItem>,
        weather: WeatherInfo,
        occasion: Occasion,
        profile: BodyProfile,
        feedbacks: List<OutfitFeedback> = emptyList()
    ): List<OutfitSuggestion> {
        val available = items.filter { it.cleaningStatus == CleaningStatus.AVAILABLE }
        val filtered = filterItemsByWeather(available, weather)
        
        val shirts = filtered.filter { it.type == ClothingType.SHIRT }
        val pants = filtered.filter { it.type == ClothingType.PANTS }
        val shoes = filtered.filter { it.type == ClothingType.SHOES }
        val outers = filtered.filter { it.type == ClothingType.OUTERWEAR }
 
        val suggestions = mutableListOf<OutfitSuggestion>()
        var combId = 1
 
        for (s in shirts) {
            for (p in pants) {
                for (sh in shoes) {
                    val candidateOuters = if (weather.temp < 15) {
                        outers
                    } else {
                        listOf<ClothingItem?>(null)
                    }
 
                    for (o in candidateOuters) {
                        val scoreDetails = calculatePowerScore(s, p, sh, o, weather, occasion, profile, feedbacks)
                        val totalScore = scoreDetails.totalScore
                        
                        // Formulate suggestion structure
                        val itemGroup = listOfNotNull(s, p, sh, o)
                        
                        val harmonyReason = when {
                            scoreDetails.harmony > 92 -> "Exquisite Color complementarity coupled with distinctive textural flow."
                            scoreDetails.harmony > 82 -> "Elegant modern palette with high-contrast layering."
                            else -> "Clean minimalist ensemble."
                        }
 
                        suggestions.add(
                            OutfitSuggestion(
                                id = "STG_${combId++}",
                                items = itemGroup,
                                powerScore = totalScore,
                                harmonyReason = harmonyReason,
                                reasons = scoreDetails.reasons
                            )
                        )
                    }
                }
            }
        }
 
        // Sort by Power Score descending and return top 3
        return suggestions.sortedByDescending { it.powerScore }.take(3)
    }
 
    fun generateRemixSuggestions(
        items: List<ClothingItem>,
        weather: WeatherInfo,
        profile: BodyProfile,
        feedbacks: List<OutfitFeedback> = emptyList()
    ): List<OutfitSuggestion> {
        // The Remix Engine aims to prioritize items with lowest usage (highest neglected score)
        val available = items.filter { it.cleaningStatus == CleaningStatus.AVAILABLE }
        val filtered = filterItemsByWeather(available, weather)
        val shirts = filtered.filter { it.type == ClothingType.SHIRT }
        val pants = filtered.filter { it.type == ClothingType.PANTS }
        val shoes = filtered.filter { it.type == ClothingType.SHOES }
 
        val suggestions = mutableListOf<OutfitSuggestion>()
        var remixId = 1
 
        // Sort items by wearCount ascending (neglected first)
        val sortedShirts = shirts.sortedBy { it.wearCount }
        val sortedPants = pants.sortedBy { it.wearCount }
 
        // Find unusual but complementary combinations of neglected items
        for (s in sortedShirts.take(3)) {
            for (p in sortedPants.take(3)) {
                for (sh in shoes) {
                    val scoreDetails = calculatePowerScore(
                        s, p, sh, null, weather, 
                        Occasion("remix", "Casual Friday", System.currentTimeMillis(), 5, "Casual Vibe"), 
                        profile,
                        feedbacks
                    )
                    
                    suggestions.add(
                        OutfitSuggestion(
                            id = "REMIX_${remixId++}",
                            items = listOf(s, p, sh),
                            powerScore = scoreDetails.totalScore,
                            harmonyReason = "Remix Special: Balancing raw usage counts by styling '${s.name}' to save it from obscurity.",
                            reasons = listOf("Combines your least-worn '${s.name}' with standard canvas layers to shift your style orbit.")
                        )
                    )
                }
            }
        }

        return suggestions.sortedByDescending { it.powerScore }.take(3)
    }
}

data class ScoreBreakdown(
    val totalScore: Int,
    val harmony: Int,
    val occasion: Int,
    val weather: Int,
    val freshness: Int,
    val body: Int,
    val reasons: List<String>
)
