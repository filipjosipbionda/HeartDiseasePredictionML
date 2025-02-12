package com.example.ruap.ui.result

import com.example.ruap.data.repository.ResultRepository
import androidx.lifecycle.viewModelScope
import com.example.ruap.data.model.HeartDiseaseOutput
import com.example.ruap.ui.result.model.ResultScreenData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.opencsv.CSVReader
import java.io.InputStreamReader

sealed class ResultScreenState {
    data object Loading : ResultScreenState()
    data class Success(val data: HeartDiseaseOutput, val insights: ResultInsights) : ResultScreenState()
    data class Error(val message: String) : ResultScreenState()
}

data class ResultInsights(
    val ageGroupPercentage: Double,
    val averageCholesterol: Double,
    val heartDiseaseInAgeGroup: Double
)

class ResultViewModel(
    private val resultScreenData: ResultScreenData,
    private val repository: ResultRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _resultState = MutableStateFlow<ResultScreenState>(ResultScreenState.Loading)
    val resultState: StateFlow<ResultScreenState> get() = _resultState

    private var csvData: List<List<String>> = emptyList()

    private val fileName = "heart.csv"

    init {
        loadCSVData()
        fetchResult()
    }

    private fun loadCSVData() {
        val context = getApplication<Application>()
        csvData = readCSVFromAssets(context)
    }

    private fun fetchResult() {
        viewModelScope.launch {
            _resultState.value = ResultScreenState.Loading
            val result = repository.sendDataToAzure(resultScreenData)
            val insights = calculateInsights(resultScreenData)

            _resultState.value = result.fold(
                onSuccess = { ResultScreenState.Success(it, insights) },
                onFailure = { ResultScreenState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    private fun readCSVFromAssets(context: Context): List<List<String>> {
        val dataList = mutableListOf<List<String>>()
        try {
            context.assets.open(fileName).use { inputStream ->
                val reader = CSVReader(InputStreamReader(inputStream))
                var nextLine: Array<String>?
                while (reader.readNext().also { nextLine = it } != null) {
                    dataList.add(nextLine!!.toList())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataList
    }

    private fun calculateInsights(userData: ResultScreenData): ResultInsights {
        val ageGroup = (userData.age / 10) * 10

        val totalPatients = csvData.size - 1
        val sameAgeGroup = csvData.drop(1).filter { (it[0].toIntOrNull() ?: -1) / 10 * 10 == ageGroup }

        val ageGroupPercentage = (sameAgeGroup.size.toDouble() / totalPatients) * 100
        val averageCholesterol = sameAgeGroup.mapNotNull { it[3].toIntOrNull() }.average()
        val heartDiseaseCases = sameAgeGroup.count { it[11] == "1" }
        val heartDiseaseInAgeGroup = (heartDiseaseCases.toDouble() / sameAgeGroup.size) * 100

        return ResultInsights(
            ageGroupPercentage = ageGroupPercentage,
            averageCholesterol = averageCholesterol,
            heartDiseaseInAgeGroup = heartDiseaseInAgeGroup
        )
    }
}
