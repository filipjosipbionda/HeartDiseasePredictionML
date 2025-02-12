package com.example.ruap.ui.form

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.example.ruap.ui.form.enums.ChestPainType
import com.example.ruap.ui.form.enums.ExerciseAngina
import com.example.ruap.ui.form.enums.RestingECG
import com.example.ruap.ui.form.enums.Sex
import com.example.ruap.ui.form.enums.StSlope

data class FormScreenState(
    val age: String,
    val sex: Sex?,
    val chestPainType: ChestPainType?,
    val restingBp: String,
    val cholesterol: String,
    val fastingBs: String,
    val restingECG: RestingECG?,
    val maxHR: String,
    val exerciseAngina: ExerciseAngina?,
    val oldPeak: String,
    val stSlope: StSlope?,
)


val FormScreenStateSaver: Saver<MutableState<FormScreenState>, Any> = listSaver(
    save = {
        listOf(
            it.value.age,
            it.value.sex?.name,
            it.value.chestPainType?.name,
            it.value.restingBp,
            it.value.cholesterol,
            it.value.fastingBs,
            it.value.restingECG?.name,
            it.value.maxHR,
            it.value.exerciseAngina?.name,
            it.value.oldPeak,
            it.value.stSlope?.name
        )
           },
    restore = {
        mutableStateOf(
            FormScreenState(
                age = it[0] as String,
                sex = (it[1])?.let { sexName -> Sex.valueOf(sexName) },
                chestPainType = (it[2])?.let { chestPainType -> ChestPainType.valueOf(chestPainType) },
                restingBp = it[3] as String,
                cholesterol = it[4] as String,
                fastingBs = it[5] as String,
                restingECG = (it[6])?.let { restingECG -> RestingECG.valueOf(restingECG)},
                maxHR = it[7] as String,
                exerciseAngina = (it[8])?.let { exerciseAngina -> ExerciseAngina.valueOf(exerciseAngina)},
                oldPeak = it[9] as String,
                stSlope = it[10]?.let { stSlope -> StSlope.valueOf(stSlope)}
                )
        )
    }
)