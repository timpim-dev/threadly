package com.threadly.felixx.dev.mail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter
import java.io.InputStreamReader

object AiAssistant {
    private const val API_URL = "https://openrouter.ai/api/v1/chat/completions"

    suspend fun sendPrompt(apiKey: String, model: String, systemPrompt: String, userPrompt: String): String = withContext(Dispatchers.IO) {
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Bearer $apiKey")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("HTTP-Referer", "https://threadly.app")
        connection.setRequestProperty("X-Title", "Threadly App")
        connection.doOutput = true

        val messages = JSONArray().apply {
            put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })
            put(JSONObject().apply {
                put("role", "user")
                put("content", userPrompt)
            })
        }

        val jsonBody = JSONObject().apply {
            put("model", model)
            put("messages", messages)
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(jsonBody.toString())
            writer.flush()
        }

        val responseCode = connection.responseCode
        if (responseCode in 200..299) {
            val responseStr = InputStreamReader(connection.inputStream).readText()
            val jsonResponse = JSONObject(responseStr)
            val choices = jsonResponse.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val message = choices.getJSONObject(0).optJSONObject("message")
                return@withContext message?.optString("content") ?: "No content generated."
            }
            return@withContext "Empty response."
        } else {
            val errorStr = kotlin.runCatching { InputStreamReader(connection.errorStream).readText() }.getOrDefault("")
            throw Exception("OpenRouter API error: $responseCode $errorStr")
        }
    }
}
