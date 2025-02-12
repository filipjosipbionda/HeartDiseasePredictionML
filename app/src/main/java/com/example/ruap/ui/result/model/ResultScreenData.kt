package com.example.ruap.ui.result.model

import com.example.ruap.ui.routes.ResultScreen
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultScreenData(
    @SerialName("Age") val age: Int,
    @SerialName("Sex") val sex: String,
    @SerialName("ChestPainType") val chestPainType: String,
    @SerialName("RestingBP") val restingBp: Int,
    @SerialName("Cholesterol") val cholesterol: Int,
    @SerialName("FastingBS") val fastingBs: Int,
    @SerialName("RestingECG") val restingECG: String,
    @SerialName("MaxHR") val maxHR: Int,
    @SerialName("ExerciseAngina") val exerciseAngina: Boolean,
    @SerialName("Oldpeak") val oldPeak: Double,
    @SerialName("ST_Slope") val stSlope: String
)

internal fun ResultScreen.toResultScreenData(): ResultScreenData {
    return ResultScreenData(
        age = this.age,
        cholesterol = this.cholesterol,
        maxHR = this.maxHR,
        restingECG = this.restingECG,
        restingBp = this.restingBp,
        oldPeak = this.oldPeak,
        sex = this.sex,
        fastingBs = this.fastingBs,
        chestPainType = this.chestPainType,
        exerciseAngina = this.exerciseAngina,
        stSlope = this.stSlope
    )
}