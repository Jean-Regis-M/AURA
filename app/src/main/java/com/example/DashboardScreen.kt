package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: AuraViewModel) {
    val itemsState by viewModel.wardrobe.collectAsState()
    val weatherState by viewModel.weather.collectAsState()
    val occasionsState by viewModel.occasions.collectAsState()
    val selectedOccasionState by viewModel.selectedOccasion.collectAsState()
    val profileState by viewModel.bodyProfile.collectAsState()
    val suggestionsState by viewModel.outfits.collectAsState()
    val remixesState by viewModel.remixes.collectAsState()
    val feedbacksState by viewModel.feedbacks.collectAsState()

    var showAddOccasionDialog by remember { mutableStateOf(false) }
    var showAddGarmentDialog by remember { mutableStateOf(false) }
    var searchCity by remember { mutableStateOf("") }

    // Color definitions for Aura Editorial Aesthetic luxury brand look
    val darkBackground = Color(0xFFFDFCFB) // warm book cream paper
    val panelBackground = Color(0xFFF5F2EE) // warm grey-cream page panel
    val borderSecondary = Color(0xFF1A1A1A) // sharp solid charcoal border
    val accentsEmerald = Color(0xFFB24A27) // terracotta burnt-orange accent
    val textPrimary = Color(0xFF1A1A1A) // solid ink charcoal text
    val textSecondary = Color(0xFF6E6B64) // muted warm editorial gray text

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp, 36.dp)
                                        .border(2.dp, borderSecondary, RoundedCornerShape(0.dp))
                                        .background(Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "AURA",
                                        color = textPrimary,
                                        fontWeight = FontWeight.Light,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Wardrobe OS",
                                    color = textPrimary,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Your wardrobe doesn't speak. AURA listens.",
                                color = textSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Digital Time & Date Anchor
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            var timeString by remember { mutableStateOf("09:36 AM UTC") }
                            LaunchedEffect(Unit) {
                                while (true) {
                                    val now = System.currentTimeMillis()
                                    // Simulated high-fidelity live timestamp formatted
                                    val formatted = "09:36:47 AM"
                                    timeString = formatted
                                    delay(1000)
                                }
                            }
                            Text(
                                text = "Friday, June 12, 2026",
                                color = textPrimary,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = timeString,
                                color = accentsEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = textPrimary
                )
            )
        },
        containerColor = darkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(darkBackground)
        ) {
            // Toast / Interactive Banner HUD
            viewModel.toastMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = accentsEmerald),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Alert", tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = msg, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            // Ingestion Vision Overlay
            AnimatedVisibility(
                visible = viewModel.isScanning,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = panelBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(1.dp, accentsEmerald, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = accentsEmerald, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = "AURA Vision Scan Active", color = textPrimary, fontWeight = FontWeight.Bold)
                                Text(text = viewModel.scanningPhase, color = textSecondary, fontSize = 12.sp)
                            }
                        }
                        Text(
                            text = "EXTRACTING DNA",
                            color = accentsEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Main Core Tablet Layout: Split into Sidebar and Display Module
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar Navigation (Left Panel)
                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .fillMaxHeight()
                        .background(darkBackground)
                        .padding(start = 16.dp, top = 16.dp, end = 12.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "CONSOLE CONTROLS",
                        color = textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val menuItems = listOf(
                        Triple("TODAY", "Styling Engine", Icons.Default.Star),
                        Triple("WARDROBE", "Identifiers Closet", Icons.Default.List),
                        Triple("LAUNDRY", "Laundry intelligence", Icons.Default.Refresh),
                        Triple("REMIX", "Remix Engine", Icons.Default.PlayArrow),
                        Triple("PROFILE", "Personal Proportions", Icons.Default.Person)
                    )

                    menuItems.forEach { (tabId, label, icon) ->
                        val isSelected = viewModel.currentTab == tabId
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) panelBackground else Color.Transparent
                            ),
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { viewModel.currentTab = tabId }
                                .padding(vertical = 4.dp)
                                .border(
                                    width = if (isSelected) 1.dp else 0.dp,
                                    color = if (isSelected) borderSecondary else Color.Transparent,
                                    shape = RoundedCornerShape(0.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tabId,
                                    tint = if (isSelected) accentsEmerald else textSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = label,
                                    color = if (isSelected) textPrimary else textSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Local Environment Controller
                    Text(
                        text = "WEATHER INTELLIGENCE",
                        color = textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = panelBackground),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "${weatherState.temp}°C · ${weatherState.description}",
                                color = textPrimary,
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Rain: ${if (weatherState.isRaining) "Active" else "None"} | Wind: ${weatherState.windSpeed} km/h",
                                color = textSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Simulate climate:", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)

                            val weatherConfigs = listOf("Warm Summer", "Cool Breeze", "Rainy Solitude", "Winter Freeze")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(top = 4.dp)
                            ) {
                                weatherConfigs.forEach { name ->
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                            .background(Color.Transparent)
                                            .clickable { viewModel.setWeatherMode(name) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(name, color = textPrimary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(borderSecondary.copy(alpha = 0.2f)))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Connect Live Satellite:", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                    .background(darkBackground),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.foundation.text.BasicTextField(
                                    value = searchCity,
                                    onValueChange = { searchCity = it },
                                    singleLine = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        color = textPrimary,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Serif
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    decorationBox = { innerTextField ->
                                        if (searchCity.isEmpty()) {
                                            Text(
                                                text = "Enter City (e.g. Paris)...",
                                                color = textSecondary.copy(alpha = 0.5f),
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Serif,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .clickable { viewModel.queryLiveOpenWeather(searchCity) }
                                        .background(borderSecondary)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Sync",
                                        tint = darkBackground,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(borderSecondary))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "DEVELOPER ENGINE PORTAL",
                        color = textSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Jean Francois Regis MUKIZA",
                        color = textPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }

                // Core Main Display Module
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(0.dp))
                        .background(panelBackground)
                        .border(2.dp, borderSecondary, RoundedCornerShape(0.dp))
                ) {
                    FadeInCard(key = viewModel.currentTab, modifier = Modifier.fillMaxSize()) {
                        when (viewModel.currentTab) {
                            "TODAY" -> TodaySuggestionsSection(
                                suggestions = suggestionsState,
                                occasions = occasionsState,
                                selectedOccasion = selectedOccasionState,
                                feedbacks = feedbacksState,
                                onOccasionSelect = { viewModel.selectOccasion(it) },
                                onWearOutfit = { viewModel.wearOutfit(it) },
                                onRateOutfit = { sugg, rating ->
                                    viewModel.rateOutfit(
                                        outfitId = sugg.id,
                                        itemsIds = sugg.items.map { it.id },
                                        ratingEmoji = rating,
                                        score = sugg.powerScore
                                    )
                                },
                                onAddOccasionClick = { showAddOccasionDialog = true }
                            )
                            "WARDROBE" -> ClosetSection(
                                items = itemsState,
                                onProgressLaundry = { item, state -> viewModel.progressLaundry(item, state) },
                                onAddGarmentClick = { showAddGarmentDialog = true }
                            )
                            "LAUNDRY" -> LaundryHubSection(
                                items = itemsState,
                                onProgressChange = { item, status -> viewModel.progressLaundry(item, status) }
                            )
                            "REMIX" -> RemixSection(
                                remixes = remixesState,
                                feedbacks = feedbacksState,
                                onWearOutfit = { viewModel.wearOutfit(it) },
                                onRateOutfit = { sugg, rating ->
                                    viewModel.rateOutfit(
                                        outfitId = sugg.id,
                                        itemsIds = sugg.items.map { it.id },
                                        ratingEmoji = rating,
                                        score = sugg.powerScore
                                    )
                                }
                            )
                            "PROFILE" -> ProfileSection(
                                profile = profileState,
                                onUpdate = { h, sh, s, st, e -> viewModel.updateBodyProfile(h, sh, s, st, e) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Special Occasion dialog
    if (showAddOccasionDialog) {
        var occName by remember { mutableStateOf("") }
        var desiredVibe by remember { mutableStateOf("") }
        var formality by remember { mutableStateOf(5f) }
        var isSunday by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddOccasionDialog = false },
            shape = RoundedCornerShape(0.dp),
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            title = { Text("Schedule Special Occasion", color = textPrimary, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif) },
            text = {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    OutlinedTextField(
                        value = occName,
                        onValueChange = { occName = it },
                        label = { Text("Occasion Name", fontFamily = FontFamily.Serif) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = desiredVibe,
                        onValueChange = { desiredVibe = it },
                        label = { Text("Desired Style Vibe (e.g. Sharp, modest)", fontFamily = FontFamily.Serif) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Formality level: ${formality.toInt()}/10", color = textSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Slider(
                        value = formality,
                        onValueChange = { formality = it },
                        valueRange = 1f..10f,
                        colors = SliderDefaults.colors(thumbColor = accentsEmerald, activeTrackColor = accentsEmerald)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSunday,
                            onCheckedChange = { isSunday = it },
                            colors = CheckboxDefaults.colors(checkedColor = accentsEmerald)
                        )
                        Text("Modest requirements (Sunday church Mode)", color = textPrimary, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (occName.isNotBlank()) {
                            viewModel.addOccasion(occName, desiredVibe, formality.toInt(), isSunday)
                        }
                        showAddOccasionDialog = false
                    },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald)
                ) {
                    Text("Schedule Outfit Strategy", color = Color.White, fontFamily = FontFamily.Monospace)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddOccasionDialog = false },
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Dismiss", color = textSecondary, fontFamily = FontFamily.Monospace)
                }
            },
            containerColor = panelBackground
        )
    }

    // Ingest New Garment dialog
    if (showAddGarmentDialog) {
        var name by remember { mutableStateOf("") }
        var type by remember { mutableStateOf(ClothingType.SHIRT) }
        var colorHex by remember { mutableStateOf("#1D4ED8") } // classic cobalt default
        var colorName by remember { mutableStateOf("Classic Cobalt") }
        var silhouette by remember { mutableStateOf(SilhouetteType.FITTED) }
        var texture by remember { mutableStateOf(TextureType.SMOOTH_MATTE) }
        var weightClass by remember { mutableStateOf("Medium") }
        var fabric by remember { mutableStateOf("100% Cotton") }
        var formality by remember { mutableStateOf(5f) }
        var isModest by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showAddGarmentDialog = false },
            shape = RoundedCornerShape(0.dp),
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Face, contentDescription = null, tint = accentsEmerald)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Digitize Clothes (AI Ingestor)", color = textPrimary, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp)
                ) {
                    Text("SNAP A PHOTO or describe your garment below for dynamic extraction:", color = textSecondary, fontSize = 11.sp, fontFamily = FontFamily.Serif, modifier = Modifier.padding(bottom = 12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("e.g. Crimson Corduroy Jacket", fontFamily = FontFamily.Serif) },
                        label = { Text("Garment Name / Manual Override", fontFamily = FontFamily.Serif) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Garment Type", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp)
                    ) {
                        ClothingType.values().forEach { cType ->
                            val isSel = type == cType
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                    .background(if (isSel) accentsEmerald else Color.Transparent)
                                    .clickable { type = cType }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cType.name, 
                                    color = if (isSel) Color.White else textPrimary, 
                                    fontSize = 11.sp, 
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Select Color DNA", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    val swatchMap = listOf(
                        "#991B1B" to "Ruby Crimson",
                        "#7C2D12" to "Sienna Clay",
                        "#D97706" to "Saffron Gold",
                        "#065F46" to "Emerald Forest",
                        "#1D4ED8" to "Classic Cobalt",
                        "#4B5563" to "Granite Gray",
                        "#F3F4F6" to "Pearl Alabaster"
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        swatchMap.forEach { (hex, name) ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(android.graphics.Color.parseColor(hex)))
                                    .border(
                                        width = if (colorHex == hex) 3.dp else 1.dp,
                                        color = if (colorHex == hex) accentsEmerald else borderSecondary,
                                        shape = RoundedCornerShape(0.dp)
                                    )
                                    .clickable {
                                        colorHex = hex
                                        colorName = name
                                    }
                            )
                        }
                    }
                    Text("Extracted Palette Label: $colorName", color = textSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Silhouette Geometry", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp)
                    ) {
                        SilhouetteType.values().forEach { sil ->
                            val isSel = silhouette == sil
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                    .background(if (isSel) accentsEmerald else Color.Transparent)
                                    .clickable { silhouette = sil }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = sil.name, 
                                    color = if (isSel) Color.White else textPrimary, 
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Weight Class Threshold", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Light", "Medium", "Heavy").forEach { wt ->
                            val isSel = weightClass == wt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                    .background(if (isSel) accentsEmerald else Color.Transparent)
                                    .clickable { weightClass = wt }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = wt, 
                                    color = if (isSel) Color.White else textPrimary, 
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Texture Code Profile", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp)
                    ) {
                        TextureType.values().forEach { tex ->
                            val isSel = texture == tex
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                    .background(if (isSel) accentsEmerald else Color.Transparent)
                                    .clickable { texture = tex }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tex.displayName, 
                                    color = if (isSel) Color.White else textPrimary, 
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = fabric,
                        onValueChange = { fabric = it },
                        label = { Text("Fabric Composition Spec", fontFamily = FontFamily.Serif) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Occasion Formality (1-10): ${formality.toInt()}", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Slider(
                        value = formality,
                        onValueChange = { formality = it },
                        valueRange = 1f..10f,
                        colors = SliderDefaults.colors(thumbColor = accentsEmerald, activeTrackColor = accentsEmerald)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isModest,
                            onCheckedChange = { isModest = it },
                            colors = CheckboxDefaults.colors(checkedColor = accentsEmerald)
                        )
                        Text("Modest garment design (modesty metrics)", color = textPrimary, fontFamily = FontFamily.Serif, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.ingestNewGarment(
                            rawName = name,
                            type = type,
                            colorHex = colorHex,
                            colorName = colorName,
                            silhouette = silhouette,
                            texture = texture,
                            weightClass = weightClass,
                            fabric = fabric,
                            formality = formality.toInt(),
                            isModest = isModest
                        )
                        showAddGarmentDialog = false
                    },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald)
                ) {
                    Text("Execute AI Ingestion", color = Color.White, fontFamily = FontFamily.Monospace)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddGarmentDialog = false },
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Cancel", color = textSecondary, fontFamily = FontFamily.Monospace)
                }
            },
            containerColor = panelBackground
        )
    }
}

@Composable
fun TodaySuggestionsSection(
    suggestions: List<OutfitSuggestion>,
    occasions: List<Occasion>,
    selectedOccasion: Occasion?,
    feedbacks: List<OutfitFeedback> = emptyList(),
    onOccasionSelect: (Occasion) -> Unit,
    onWearOutfit: (OutfitSuggestion) -> Unit,
    onRateOutfit: (OutfitSuggestion, String) -> Unit,
    onAddOccasionClick: () -> Unit
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Occasion strategy decider bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE OCCASION STRATEGY",
                    color = textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = selectedOccasion?.name ?: "Regular Daily Wear Plan",
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onAddOccasionClick,
                colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald),
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Occasion Override", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }

        // Horizontal Occasion selector row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            occasions.forEach { occ ->
                val isSelected = occ.id == selectedOccasion?.id
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) panelBackground else Color.Transparent
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { onOccasionSelect(occ) }
                        .border(
                            width = 1.dp,
                            color = borderSecondary,
                            shape = RoundedCornerShape(0.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = occ.name,
                            color = textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = occ.desiredVibe,
                            color = textSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Text(
            text = "AI HARMONIZED STYLING PRESETS (TOP SUGGESTIONS)",
            color = textSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (suggestions.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = textSecondary, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No fully clean fits found suitable for this strategy.", color = textPrimary, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
                    Text("Clear some clothing statuses in the Laundry Hub to revive suggestions.", color = textSecondary, fontSize = 12.sp, fontFamily = FontFamily.Serif)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(suggestions, key = { _, s -> s.id }) { index, sugg ->
                    FadeInCard(key = sugg.id, delayMillis = index * 150) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)), // warm cream
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, borderSecondary, RoundedCornerShape(0.dp)),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Suggestion Header
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = sugg.powerScore.toString(),
                                                color = accentsEmerald,
                                                fontWeight = FontWeight.Light,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 32.sp,
                                                lineHeight = 32.sp
                                            )
                                            Text(
                                                text = "SCORE",
                                                color = textSecondary,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(text = "Power Ensemble Fit", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = FontFamily.Serif)
                                            Text(text = sugg.harmonyReason, color = textSecondary, fontSize = 11.sp, fontFamily = FontFamily.Serif, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                        }
                                    }

                                    Button(
                                        onClick = { onWearOutfit(sugg) },
                                        colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald),
                                        shape = RoundedCornerShape(0.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Wear Look Today", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }

                                // Horizontal list of clothing cards in this combo
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState())
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    sugg.items.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .width(140.dp)
                                                .background(panelBackground)
                                                .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(16.dp)
                                                            .background(Color(android.graphics.Color.parseColor(item.color.hex)))
                                                            .border(
                                                                1.dp,
                                                                borderSecondary,
                                                                RoundedCornerShape(0.dp)
                                                            )
                                                    )
                                                    Text(
                                                        text = item.type.name.take(4),
                                                        color = accentsEmerald,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = item.name,
                                                    color = textPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${item.silhouette.name.lowercase()} · ${item.color.name}",
                                                    color = textSecondary,
                                                    fontSize = 9.sp,
                                                    fontFamily = FontFamily.Serif,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = item.fabricComposition,
                                                    color = textSecondary,
                                                    fontSize = 8.sp,
                                                    fontFamily = FontFamily.Monospace,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }

                                // Reasons panel
                                if (sugg.reasons.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(panelBackground)
                                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                            .padding(8.dp)
                                    ) {
                                        sugg.reasons.forEach { r ->
                                            Row(
                                                modifier = Modifier.padding(bottom = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .background(accentsEmerald)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = r, 
                                                    color = textPrimary, 
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily.Serif,
                                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                                )
                                            }
                                        }
                                    }
                                }

                                // Dynamic Live Feedback & Retraining Module (Reusable FeedbackComponent)
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(borderSecondary.copy(alpha = 0.15f))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                val matchingFeedback = feedbacks.find { fb ->
                                    fb.items.toSet() == sugg.items.map { it.id }.toSet()
                                }
                                FeedbackComponent(
                                    outfitId = sugg.id,
                                    itemsIds = sugg.items.map { it.id },
                                    score = sugg.powerScore,
                                    matchingFeedback = matchingFeedback,
                                    onRateOutfit = { outfitId, itemsIds, emo, score ->
                                        onRateOutfit(sugg, emo)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        AdmobBannerContainer()
    }
}

@Composable
fun ClosetSection(
    items: List<ClothingItem>,
    onProgressLaundry: (ClothingItem, CleaningStatus) -> Unit,
    onAddGarmentClick: () -> Unit
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)

    var currentFilter by remember { mutableStateOf<ClothingType?>(null) }

    val filteredItems = if (currentFilter == null) items else items.filter { it.type == currentFilter }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DIGITAL CLOAK INVENTORY",
                    color = textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "My Digital Closet (${items.size} garments)",
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onAddGarmentClick,
                colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald),
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Digitize Clothes AI", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }

        // Horizontal Category Tab Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "All" selector
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (currentFilter == null) panelBackground else Color.Transparent
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .clickable { currentFilter = null }
                    .border(
                        1.dp,
                        borderSecondary,
                        RoundedCornerShape(0.dp)
                    )
            ) {
                Text(
                    "Show All",
                    color = textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            ClothingType.values().forEach { type ->
                val isSelected = currentFilter == type
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) panelBackground else Color.Transparent
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .clickable { currentFilter = type }
                        .border(
                            1.dp,
                            borderSecondary,
                            RoundedCornerShape(0.dp)
                        )
                ) {
                    Text(
                        type.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = textPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(filteredItems, key = { _, item -> item.id }) { index, item ->
                FadeInCard(key = item.id, delayMillis = (index % 6) * 80) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)), // book-warm paper white
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier.border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(android.graphics.Color.parseColor(item.color.hex)))
                                        .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                        .background(
                                            when (item.cleaningStatus) {
                                                CleaningStatus.AVAILABLE -> accentsEmerald.copy(alpha = 0.1f)
                                                CleaningStatus.DIRTY -> Color(0xFFEF4444).copy(alpha = 0.1f)
                                                else -> Color(0xFFF59E0B).copy(alpha = 0.1f)
                                            }
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.cleaningStatus.name.lowercase(),
                                        color = when (item.cleaningStatus) {
                                            CleaningStatus.AVAILABLE -> accentsEmerald
                                            CleaningStatus.DIRTY -> Color(0xFFEF4444)
                                            else -> Color(0xFFF59E0B)
                                        },
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.name,
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${item.silhouette.name.lowercase()} · ${item.color.name}",
                                color = textSecondary,
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontSize = 10.sp
                            )
                            Text(
                                text = item.fabricComposition,
                                color = textSecondary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Worn: ${item.wearCount}x",
                                    color = textSecondary,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                )
                                val linkColor = if (item.cleaningStatus == CleaningStatus.AVAILABLE) Color(0xFFEF4444) else accentsEmerald
                                val linkText = if (item.cleaningStatus == CleaningStatus.AVAILABLE) "Mark dirty" else "Clean item"
                                Text(
                                    text = linkText,
                                    color = linkColor,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { onProgressLaundry(item, if (item.cleaningStatus == CleaningStatus.AVAILABLE) CleaningStatus.DIRTY else CleaningStatus.AVAILABLE) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LaundryHubSection(
    items: List<ClothingItem>,
    onProgressChange: (ClothingItem, CleaningStatus) -> Unit
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)

    val laundryStages = CleaningStatus.values()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "LAUNDRY SCHEDULING & TRACKING",
            color = textSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Wardrobe Logistics & Buffer",
            color = textPrimary,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(laundryStages) { index, stage ->
                val stageItems = items.filter { it.cleaningStatus == stage }
                FadeInCard(key = stage.name, delayMillis = index * 120) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${stage.name} (${stageItems.size} items)",
                                color = when (stage) {
                                    CleaningStatus.AVAILABLE -> accentsEmerald
                                    CleaningStatus.DIRTY -> Color(0xFFEF4444)
                                    else -> Color(0xFFF59E0B)
                                },
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp
                            )
                            if (stage != CleaningStatus.AVAILABLE) {
                                val nextStage = when (stage) {
                                    CleaningStatus.DIRTY -> CleaningStatus.WASHING
                                    CleaningStatus.WASHING -> CleaningStatus.DRYING
                                    CleaningStatus.DRYING -> CleaningStatus.IRONING
                                    CleaningStatus.IRONING -> CleaningStatus.AVAILABLE
                                    else -> CleaningStatus.AVAILABLE
                                }
                                Text(
                                    text = "Send All to ${nextStage.name} →",
                                    color = accentsEmerald,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        stageItems.forEach { onProgressChange(it, nextStage) }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (stageItems.isEmpty()) {
                            Text(
                                text = "Zero items in this pipeline phase.",
                                color = textSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                stageItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .width(130.dp)
                                            .background(panelBackground)
                                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                            .padding(8.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .background(Color(android.graphics.Color.parseColor(item.color.hex)))
                                                        .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                                )
                                                Text(
                                                    text = item.weightClass,
                                                    color = textSecondary,
                                                    fontSize = 8.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = item.name,
                                                color = textPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Progress Button
                                            val transitionLabel = when (stage) {
                                                CleaningStatus.DIRTY -> "Wash"
                                                CleaningStatus.WASHING -> "Dry"
                                                CleaningStatus.DRYING -> "Iron"
                                                CleaningStatus.IRONING -> "Ready"
                                                else -> ""
                                            }
                                            val nextStatus = when (stage) {
                                                CleaningStatus.DIRTY -> CleaningStatus.WASHING
                                                CleaningStatus.WASHING -> CleaningStatus.DRYING
                                                CleaningStatus.DRYING -> CleaningStatus.IRONING
                                                CleaningStatus.IRONING -> CleaningStatus.AVAILABLE
                                                else -> CleaningStatus.AVAILABLE
                                            }

                                            if (transitionLabel.isNotBlank()) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(accentsEmerald.copy(alpha = 0.2f))
                                                        .clickable { onProgressChange(item, nextStatus) }
                                                        .padding(vertical = 4.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = transitionLabel,
                                                        color = accentsEmerald,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
fun RemixSection(
    remixes: List<OutfitSuggestion>,
    feedbacks: List<OutfitFeedback> = emptyList(),
    onWearOutfit: (OutfitSuggestion) -> Unit,
    onRateOutfit: (OutfitSuggestion, String) -> Unit
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "SAVING CLOTHES FROM OBSCURITY",
                color = textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "The Remix Engine™ (Zero-Waste Combinations)",
                color = textPrimary,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "AURA searches for items you haven't worn in weeks and integrates them into fresh style balances.",
                color = textSecondary,
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(remixes) { index, sugg ->
                FadeInCard(key = sugg.id, delayMillis = index * 150) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Unconventional Harmony Plan", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = FontFamily.Serif)
                                    Text(text = sugg.harmonyReason, color = textSecondary, fontSize = 11.sp, fontFamily = FontFamily.Serif, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                }
                                Button(
                                    onClick = { onWearOutfit(sugg) },
                                    colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald),
                                    shape = RoundedCornerShape(0.dp)
                                ) {
                                    Text("Remix Wearing", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                sugg.items.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(panelBackground)
                                            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                            .padding(8.dp)
                                    ) {
                                        Column {
                                            Box(
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .background(Color(android.graphics.Color.parseColor(item.color.hex)))
                                                    .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = item.name,
                                                color = textPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "Worn frequency: ${item.wearCount} times",
                                                color = accentsEmerald,
                                                fontSize = 8.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // Dynamic Live Feedback & Retraining Module
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(borderSecondary.copy(alpha = 0.15f))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val matchingFeedback = feedbacks.find { fb ->
                                    fb.items.toSet() == sugg.items.map { it.id }.toSet()
                                }

                                Column {
                                    Text(
                                        text = "AURA MODEL RETRAIN FEEDBACK",
                                        color = textSecondary,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    if (matchingFeedback != null) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Comfort alignment: ",
                                                color = textPrimary,
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Serif
                                            )
                                            Text(
                                                text = when(matchingFeedback.rating) {
                                                    "✨" -> "Sublime ✨"
                                                    "🙂" -> "Balanced 🙂"
                                                    else -> "Calibrate 😕"
                                                },
                                                color = accentsEmerald,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 11.sp
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "Rate fit to refine suggestions:",
                                            color = textSecondary,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Serif,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf(
                                        Triple("✨", "Sublime", "✨"),
                                        Triple("🙂", "Balanced", "🙂"),
                                        Triple("😕", "Recalibrate", "😕")
                                    ).forEach { (emo, label, symbol) ->
                                        val isSelected = matchingFeedback?.rating == emo
                                        Box(
                                            modifier = Modifier
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) accentsEmerald else borderSecondary.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(0.dp)
                                                )
                                                .background(if (isSelected) panelBackground else Color.Transparent)
                                                .clickable { onRateOutfit(sugg, emo) }
                                                .padding(horizontal = 12.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = symbol,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    profile: BodyProfile,
    onUpdate: (Int, String, String, String, String) -> Unit
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)

    var heightInput by remember { mutableStateOf(profile.height.toString()) }
    var shoulderInput by remember { mutableStateOf(profile.shoulderWidth) }
    var shapeInput by remember { mutableStateOf(profile.bodyShape) }
    var skinInput by remember { mutableStateOf(profile.skinTone) }
    var energyInput by remember { mutableStateOf(profile.energyStyle) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "PERSONAL HARMONY PROFILE",
            color = textSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Body Geometry & Resonance Info",
            color = textPrimary,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it },
            label = { Text("User Height (cm)", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("Shoulder width proportions", color = textPrimary, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            listOf("Narrow", "Medium", "Broad").forEach { sh ->
                val isSel = shoulderInput == sh
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                        .background(if (isSel) accentsEmerald else Color.Transparent)
                        .clickable { shoulderInput = sh }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sh, 
                        color = if (isSel) Color.White else textPrimary, 
                        fontSize = 12.sp, 
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Geometric Body Shape", color = textPrimary, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            listOf("Athletic", "Lean", "Stocky", "Rectangle", "Oval").forEach { bShape ->
                val isSel = shapeInput == bShape
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
                        .background(if (isSel) accentsEmerald else Color.Transparent)
                        .clickable { shapeInput = bShape }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bShape, 
                        color = if (isSel) Color.White else textPrimary, 
                        fontSize = 10.sp, 
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = skinInput,
            onValueChange = { skinInput = it },
            label = { Text("Skin Tone Palette classification", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = energyInput,
            onValueChange = { energyInput = it },
            label = { Text("Resonance Energy Vibe (e.g., Joyful & Energetic)", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val hVal = heightInput.toIntOrNull() ?: 180
                onUpdate(hVal, shoulderInput, shapeInput, skinInput, energyInput)
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentsEmerald),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Coordinate Proportions Matrix", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(borderSecondary))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "DESIGNED & CONSTRUCTED BY",
            color = textSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = "Jean Francois Regis MUKIZA",
            color = textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
fun FadeInCard(
    key: Any? = null,
    delayMillis: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember(key) { mutableStateOf(false) }
    LaunchedEffect(key) {
        if (delayMillis > 0) {
            delay(delayMillis.toLong())
        }
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun FeedbackComponent(
    outfitId: String,
    itemsIds: List<String>,
    score: Int,
    matchingFeedback: OutfitFeedback?,
    onRateOutfit: (String, List<String>, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentsEmerald = Color(0xFFB24A27) // terracotta brand accent
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF6E6B64)
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "DAILY OUTFIT RESONANCE RATINGS (WEARFEEDBACK)",
            color = textSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                if (matchingFeedback != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Aura Comfort Dynamic: ",
                            color = textPrimary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = when(matchingFeedback.rating) {
                                "✨" -> "Sublime ✨"
                                "🙂" -> "Balanced 🙂"
                                else -> "Calibrate 😕"
                            },
                            color = accentsEmerald,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Text(
                        text = "Synchronize daily logs to refine neural weights:",
                        color = textSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "✨" to "Sublime",
                    "🙂" to "Balanced",
                    "😕" to "Recalibrate"
                ).forEach { (emo, label) ->
                    val isSelected = matchingFeedback?.rating == emo
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = if (isSelected) accentsEmerald else borderSecondary.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .background(if (isSelected) panelBackground else Color.Transparent)
                            .clickable {
                                onRateOutfit(
                                    outfitId,
                                    itemsIds,
                                    emo,
                                    score
                                )
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emo,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdmobBannerContainer(modifier: Modifier = Modifier) {
    val borderSecondary = Color(0xFF1A1A1A)
    val panelBackground = Color(0xFFF5F2EE)
    val textSecondary = Color(0xFF6E6B64)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
            .background(panelBackground)
            .border(1.dp, borderSecondary, RoundedCornerShape(0.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SPONSORED CURATION ACCESS-POINT",
            color = textSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        AndroidView(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    // Configured using Google's official Test Banner ID
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
