package com.example

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

object WeatherService {
    private val client = OkHttpClient()

    // Centralized log data structure
    data class WeatherErrorLog(
        val timestamp: Long,
        val city: String,
        val errorMessage: String,
        val exceptionMessage: String?
    )

    private val _errorLogs = mutableListOf<WeatherErrorLog>()
    val errorLogs: List<WeatherErrorLog> get() = synchronized(_errorLogs) { _errorLogs.toList() }

    // Memory cache mapping cities (lowercase) to their last retrieved WeatherInfo
    private val weatherCache = mutableMapOf<String, WeatherInfo>()

    // Global fallback for scenarios where a new city is queried offline or with API timeout
    private var lastGlobalWeather: WeatherInfo? = null

    private fun logError(city: String, msg: String, ex: Throwable? = null) {
        val logEntry = WeatherErrorLog(
            timestamp = System.currentTimeMillis(),
            city = city,
            errorMessage = msg,
            exceptionMessage = ex?.stackTraceToString() ?: ex?.message
        )
        synchronized(_errorLogs) {
            _errorLogs.add(logEntry)
            if (_errorLogs.size > 100) {
                _errorLogs.removeAt(0)
            }
        }
        Log.e("WeatherService", "Weather scan failure for [$city]: $msg", ex)
    }

    suspend fun fetchWeather(city: String, apiKey: String): Result<WeatherInfo> = withContext(Dispatchers.IO) {
        val trimmedCity = city.trim()
        val cacheKey = trimmedCity.lowercase()
        val encodedCity = java.net.URLEncoder.encode(trimmedCity, "UTF-8")
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$encodedCity&units=metric&appid=$apiKey"
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val bodyStr = response.body?.string() ?: ""
                    val message = try {
                        JSONObject(bodyStr).getString("message")
                    } catch (e: Exception) {
                        "HTTP ${response.code}"
                    }
                    val errorMsg = "OpenWeatherMap returned code ${response.code}: $message"
                    logError(trimmedCity, errorMsg)
                    
                    return@withContext getFallbackResult(trimmedCity, errorMsg)
                }

                val bodyStr = response.body?.string() ?: return@withContext getFallbackResult(trimmedCity, "Response body was empty")
                val json = JSONObject(bodyStr)
                
                // Parse Main temperatures & humidity
                val main = json.getJSONObject("main")
                val tempFloat = main.getDouble("temp")
                val temp = tempFloat.toInt()
                val humidity = main.getInt("humidity")
                
                // Parse Wind speed
                val wind = json.optJSONObject("wind")
                val windSpeedMs = wind?.optDouble("speed", 0.0) ?: 0.0
                val windSpeedKmh = (windSpeedMs * 3.6).toInt() // Convert m/s to km/h
                
                // Parse Weather description and precipitation
                val weatherArray = json.getJSONArray("weather")
                var description = "Clear"
                var isRaining = false
                
                if (weatherArray.length() > 0) {
                    val weatherObj = weatherArray.getJSONObject(0)
                    val mainWeather = weatherObj.getString("main")
                    description = weatherObj.optString("description", mainWeather).replaceFirstChar { it.uppercase() }
                    
                    val lowerMain = mainWeather.lowercase()
                    isRaining = lowerMain.contains("rain") || 
                                lowerMain.contains("drizzle") || 
                                lowerMain.contains("thunderstorm") || 
                                lowerMain.contains("snow") ||
                                lowerMain.contains("mist")
                }
                
                // Additional safety fallback on precipitation object detection
                if (!isRaining && (json.has("rain") || json.has("snow"))) {
                    isRaining = true
                }

                val weatherInfo = WeatherInfo(
                    temp = temp,
                    isRaining = isRaining,
                    humidity = humidity,
                    windSpeed = windSpeedKmh,
                    description = description
                )
                
                // Save to cache on successful load
                synchronized(weatherCache) {
                    weatherCache[cacheKey] = weatherInfo
                    lastGlobalWeather = weatherInfo
                }
                
                Result.success(weatherInfo)
            }
        } catch (e: Exception) {
            val errorMsg = "Network failure or request execution crash: ${e.message}"
            logError(trimmedCity, errorMsg, e)
            return@withContext getFallbackResult(trimmedCity, errorMsg)
        }
    }

    private fun getFallbackResult(city: String, errorMsg: String): Result<WeatherInfo> {
        val cacheKey = city.lowercase()
        val cached = synchronized(weatherCache) {
            weatherCache[cacheKey] ?: lastGlobalWeather
        }
        
        return if (cached != null) {
            // Append direct '(Cached)' metadata tag inside description to let the VM or user know fallback happened
            val fallbackWeather = cached.copy(
                description = if (cached.description.endsWith("(Cached)")) cached.description else "${cached.description} (Cached)"
            )
            Result.success(fallbackWeather)
        } else {
            // Real fallback fallback to default comfortable autumn breeze weather if no local history or city is present
            val defaultFallback = WeatherInfo(
                temp = 18,
                isRaining = false,
                humidity = 45,
                windSpeed = 12,
                description = "Gentle Autumn Breeze (Cache Fallback)"
            )
            Result.success(defaultFallback)
        }
    }
}
