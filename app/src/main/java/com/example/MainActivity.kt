package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Initialize Offline-Supported Cloud Firestore Configuration
    FirebaseFeedbackManager.initialize(applicationContext)

    // Initialize Google Mobile Ads SDK (AdMob Support)
    try {
      MobileAds.initialize(this) {}
    } catch (e: Exception) {
      // Graceful fallback for virtualization/environment compatibility
    }

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val auraViewModel: AuraViewModel = viewModel()
        DashboardScreen(viewModel = auraViewModel)
      }
    }
  }
}

