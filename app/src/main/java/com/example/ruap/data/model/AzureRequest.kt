package com.example.ruap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureRequest(
    @SerialName("Inputs") val inputs: Inputs
)

@Serializable
data class Inputs(
    @SerialName("input1") val input1: List<InputData>
)

@Serializable
data class InputData (
    @SerialName("Age") val age: Int,
    @SerialName("Sex") val sex: String,
    @SerialName("ChestPainType") val chestPainType: String,
    @SerialName("RestingBP") val restingBP: Int,
    @SerialName("Cholesterol") val cholesterol: Int,
    @SerialName("FastingBS") val fastingBS: Int,
    @SerialName("RestingECG") val restingECG: String,
    @SerialName("MaxHR") val maxHR: Int,
    @SerialName("ExerciseAngina") val exerciseAngina: Boolean,
    @SerialName("Oldpeak") val oldPeak: Double,
    @SerialName("ST_Slope") val stSlope: String,
    @SerialName("HeartDisease") val heartDisease: Int? = null
)

