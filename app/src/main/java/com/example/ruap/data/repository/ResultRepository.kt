package com.example.ruap.data.repository

import com.example.ruap.data.model.AzureRequest
import com.example.ruap.data.model.InputData
import com.example.ruap.data.model.Inputs
import com.example.ruap.data.model.AzureResult
import com.example.ruap.data.model.HeartDiseaseOutput
import com.example.ruap.ui.result.model.ResultScreenData
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class ResultRepository(private val client: HttpClient) {

    private val apiKey = "aU9BIuYzrV3gGFyP2k20khVusZ3UYA0l"
    private val baseUrl = "http://27de6d23-abd6-4de6-957e-ee520aeb4bd5.westeurope.azurecontainer.io/score"

    suspend fun sendDataToAzure(resultScreenData: ResultScreenData): Result<HeartDiseaseOutput> {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = client.post(baseUrl) {
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    contentType(ContentType.Application.Json)
                    setBody(AzureRequest(Inputs(listOf(resultScreenData.toAzureInputData()))))
                }

                if (response.status == HttpStatusCode.OK) {
                    val result: AzureResult = response.body()
                    val heartDiseaseOutput = result.results.webServiceOutput0.firstOrNull()
                    if (heartDiseaseOutput != null) {
                        Result.success(heartDiseaseOutput)
                    } else {
                        Result.failure(Exception("No HeartDisease data in response"))
                    }
                } else {
                    Result.failure(Exception("API error: ${response.status}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

fun ResultScreenData.toAzureInputData(): InputData {
    return InputData(
        age = age,
        sex = sex,
        chestPainType = chestPainType,
        restingBP = restingBp,
        cholesterol = cholesterol,
        fastingBS = fastingBs,
        restingECG = restingECG,
        maxHR = maxHR,
        exerciseAngina = exerciseAngina,
        oldPeak = oldPeak,
        stSlope = stSlope,
        heartDisease = 1
    )
}
