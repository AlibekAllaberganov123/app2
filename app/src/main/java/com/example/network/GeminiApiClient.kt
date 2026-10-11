package com.example.network

import com.example.BuildConfig
import com.example.data.dict.OfflineDictionary
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent?
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

data class SentenceAnalysisResult(
    val translation: String,
    val explanation: String,
    val isFromAi: Boolean
)

object GeminiApiClient {
    private const val MODEL = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val requestAdapter = moshi.adapter(GeminiRequest::class.java)
    private val responseAdapter = moshi.adapter(GeminiResponse::class.java)

    /**
     * Translates and explains a sentence or selected text in Uzbek.
     */
    suspend fun analyzeSentence(sentence: String, contextTitle: String = ""): SentenceAnalysisResult =
        withContext(Dispatchers.IO) {
            val apiKey = try {
                BuildConfig.GEMINI_API_KEY
            } catch (e: Throwable) {
                ""
            }

            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                val (trans, expl) = OfflineDictionary.analyzeSentenceOffline(sentence)
                return@withContext SentenceAnalysisResult(
                    translation = trans,
                    explanation = expl,
                    isFromAi = false
                )
            }

            try {
                val prompt = """
Siz til o'rganuvchi va kitobxonlar uchun mutaxassis tarjimon va ustozsiz.
Quyidagi matnni o'rganayotgan foydalanuvchiga yordam bering:

Matn: "$sentence"
Kontekst: "$contextTitle"

Vazifa:
1. Matnni aniq, adabiy va tushunarli o'zbek tiliga tarjima qiling.
2. Matndagi grammatik tuzilma, qiziqarli iboralar yoki murakkab so'zlarni qisqa va aniq izohlab bering.

Iltimos, javobni quyidagi formatda bering:
TARJIMA:
[O'zbekcha tarjimasi]

IZOH:
[Grammatika va so'zlar bo'yicha tushuntirish]
""".trimIndent()

                val requestObj = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = prompt))
                        )
                    )
                )

                val jsonBody = requestAdapter.toJson(requestObj)
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonBody.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url("$BASE_URL?key=$apiKey")
                    .post(requestBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    val (trans, expl) = OfflineDictionary.analyzeSentenceOffline(sentence)
                    return@withContext SentenceAnalysisResult(
                        translation = trans,
                        explanation = expl + "\n\n(Eslatma: Tarmoq holati sababli ichki lug'atdan foydalanildi)",
                        isFromAi = false
                    )
                }

                val responseBodyStr = response.body?.string() ?: ""
                val geminiResponse = responseAdapter.fromJson(responseBodyStr)
                val aiText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

                if (aiText.isNotBlank()) {
                    val parts = parseAiResult(aiText, sentence)
                    SentenceAnalysisResult(
                        translation = parts.first,
                        explanation = parts.second,
                        isFromAi = true
                    )
                } else {
                    val (trans, expl) = OfflineDictionary.analyzeSentenceOffline(sentence)
                    SentenceAnalysisResult(trans, expl, false)
                }
            } catch (e: Exception) {
                val (trans, expl) = OfflineDictionary.analyzeSentenceOffline(sentence)
                SentenceAnalysisResult(
                    translation = trans,
                    explanation = "$expl\n\n(Ichki rejim: ${e.localizedMessage ?: "Oflayn tahlil"})",
                    isFromAi = false
                )
            }
        }

    private fun parseAiResult(rawText: String, fallbackOriginal: String): Pair<String, String> {
        val lower = rawText.lowercase()
        val tarjimaIdx = lower.indexOf("tarjima:")
        val izohIdx = lower.indexOf("izoh:")

        return if (tarjimaIdx != -1 && izohIdx != -1 && izohIdx > tarjimaIdx) {
            val tarjima = rawText.substring(tarjimaIdx + "tarjima:".length, izohIdx).trim()
            val izoh = rawText.substring(izohIdx + "izoh:".length).trim()
            Pair(tarjima, izoh)
        } else {
            Pair(rawText.take(150), rawText)
        }
    }
}
