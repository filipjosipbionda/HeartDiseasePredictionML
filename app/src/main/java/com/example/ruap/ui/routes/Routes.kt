package com.example.ruap.ui.routes

import com.example.ruap.ui.form.FormScreenState
import kotlinx.serialization.Serializable

@Serializable
object FormScreen

@Serializable
object Home

@Serializable
data class ResultScreen(
    val age: Int,
    val sex: String,
    val chestPainType: String,
    val restingBp: Int,
    val cholesterol: Int,
    val fastingBs: Int,
    val restingECG: String,
    val maxHR: Int,
    val exerciseAngina: Boolean,
    val oldPeak: Double,
    val stSlope: String
)


fun FormScreenState.toResultScreen(): ResultScreen {
    return ResultScreen(
        age = age.toInt(),
        sex = sex?.name?.firstOrNull()?.toString() ?: "",
        chestPainType = chestPainType?.name ?: "",
        restingBp = restingBp.toInt(),
        cholesterol = cholesterol.toInt(),
        fastingBs = fastingBs.toInt(),
        restingECG = restingECG?.name ?: "",
        maxHR = maxHR.toInt(),
        exerciseAngina = exerciseAngina?.name == "YES",
        oldPeak = oldPeak.toDouble(),
        stSlope = stSlope?.name ?: ""
    )
}