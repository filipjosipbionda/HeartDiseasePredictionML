package com.example.ruap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureResult(
    @SerialName("Results") val results: Results
)

@Serializable
data class Results(
    @SerialName("WebServiceOutput0") val webServiceOutput0: List<HeartDiseaseOutput>
)

@Serializable
data class HeartDiseaseOutput(
    @SerialName("Scored Labels") val scoredLabels: Int,
    @SerialName("Scored Probabilities") val scoredProbabilities: Double
)