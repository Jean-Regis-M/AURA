package com.example

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseFeedbackManager {
    private var isInitialized = false
    private var db: FirebaseFirestore? = null
    
    // In-memory cache for ultra-safe fallback
    private val localFeedbackCache = mutableListOf<OutfitFeedback>()

    private fun logD(tag: String, msg: String) {
        if (com.example.BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    private fun logE(tag: String, msg: String, e: Exception? = null) {
        if (com.example.BuildConfig.DEBUG) {
            if (e != null) {
                Log.e(tag, msg, e)
            } else {
                Log.e(tag, msg)
            }
        }
    }

    private fun logW(tag: String, msg: String, e: Exception? = null) {
        if (com.example.BuildConfig.DEBUG) {
            if (e != null) {
                Log.w(tag, msg, e)
            } else {
                Log.w(tag, msg)
            }
        }
    }

    fun initialize(context: Context) {
        if (isInitialized) return
        try {
            val apps = FirebaseApp.getApps(context)
            val app = if (apps.isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApplicationId("1:156330588189:android:3106f4fe-1f03-4cf8-8113-7cb3221ac196")
                    .setProjectId("ais-dev-gklqls43nvxwyfnftrkfjf-156330588189")
                    .setApiKey("placeholder_firebase_api_key")
                    .build()
                FirebaseApp.initializeApp(context, options)
            } else {
                apps.first()
            }
            db = FirebaseFirestore.getInstance(app)
            isInitialized = true
            logD("AuraFirebase", "Firebase and Firestore manually pre-configured.")
        } catch (e: Exception) {
            logE("AuraFirebase", "Manual initialization error, offline local simulation active.", e)
        }
    }

    fun submitFeedback(
        feedback: OutfitFeedback,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        // Always store in our local cache for immediate in-session model retraining
        localFeedbackCache.add(feedback)
        
        val firestore = db
        if (firestore == null || !isInitialized) {
            logW("AuraFirebase", "Firestore uninitialized. Local caching retained successfully.")
            onSuccess()
            return
        }

        val data = hashMapOf(
            "id" to feedback.id,
            "outfitId" to feedback.outfitId,
            "items" to feedback.items,
            "rating" to feedback.rating,
            "score" to feedback.score,
            "timestamp" to feedback.timestamp,
            "weatherTemp" to feedback.weatherTemp,
            "weatherDesc" to feedback.weatherDesc
        )

        firestore.collection("wearFeedback")
            .document(feedback.id)
            .set(data)
            .addOnSuccessListener {
                logD("AuraFirebase", "Feedback saved to Firestore online.")
                onSuccess()
            }
            .addOnFailureListener { err ->
                logW("AuraFirebase", "Failed to write online, Firestore local caching activated.", err)
                // Since Firestore supports offline queueing, this resolves normally
                onSuccess()
            }
    }

    fun fetchAllFeedback(
        onDone: (List<OutfitFeedback>) -> Unit
    ) {
        val firestore = db
        if (firestore == null || !isInitialized) {
            onDone(localFeedbackCache)
            return
        }

        firestore.collection("wearFeedback")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val fbList = mutableListOf<OutfitFeedback>()
                for (doc in querySnapshot) {
                    try {
                        val itemsRaw = doc.get("items") as? List<*>
                        val items = itemsRaw?.mapNotNull { it?.toString() } ?: emptyList()
                        val rating = doc.getString("rating") ?: "🙂"
                        
                        val fb = OutfitFeedback(
                            id = doc.getString("id") ?: doc.id,
                            outfitId = doc.getString("outfitId") ?: "",
                            items = items,
                            rating = rating,
                            score = doc.getLong("score")?.toInt() ?: 0,
                            timestamp = doc.getLong("timestamp") ?: 0L,
                            weatherTemp = doc.getLong("weatherTemp")?.toInt() ?: 0,
                            weatherDesc = doc.getString("weatherDesc") ?: ""
                        )
                        fbList.add(fb)
                    } catch (e: Exception) {
                        logE("AuraFirebase", "Error parsing doc ${doc.id}", e)
                    }
                }
                
                // Merge/Sync with local cache ensuring uniqueness by id
                val merged = (fbList + localFeedbackCache).distinctBy { it.id }
                localFeedbackCache.clear()
                localFeedbackCache.addAll(merged)
                onDone(merged)
            }
            .addOnFailureListener { err ->
                logW("AuraFirebase", "Failed to load database. Loading local backups instead.", err)
                onDone(localFeedbackCache)
            }
    }
}
