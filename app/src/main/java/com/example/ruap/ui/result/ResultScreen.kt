package com.example.ruap.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ruap.R
import com.example.ruap.ui.result.model.ResultScreenData
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navigateOnHomeScreen: () -> Unit,
    navigateBack: () -> Unit,
    resultScreenData: ResultScreenData,
    modifier: Modifier = Modifier
) {
    val viewModel: ResultViewModel = koinViewModel(parameters = { parametersOf(resultScreenData) })
    val resultState by viewModel.resultState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Result") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = navigateOnHomeScreen) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (resultState) {
                is ResultScreenState.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultScreenState.Success -> {
                    val data = (resultState as ResultScreenState.Success).data
                    val insights = (resultState as ResultScreenState.Success).insights
                    val probabilityPercentage = data.scoredProbabilities * 100
                    val riskCategory = getHeartDiseaseRiskCategory(probabilityPercentage)
                    val recommendation = getRecommendationMessage(probabilityPercentage)

                    ResultScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        probabilityPercentage = probabilityPercentage,
                        riskCategory = riskCategory,
                        recommendation = recommendation,
                        insights = insights
                    )
                }
                is ResultScreenState.Error -> {
                    val error = (resultState as ResultScreenState.Error).message
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

}

@Composable
fun ResultScreenContent(
    probabilityPercentage: Double,
    riskCategory: String,
    recommendation: String,
    insights: ResultInsights,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.heart_disease_risk_assessment),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.probability, probabilityPercentage),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.risk_level, riskCategory),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.recommendation),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = recommendation,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.additional_insights),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.age_group_percentage, insights.ageGroupPercentage.toInt()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.average_cholesterol, insights.averageCholesterol),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.heart_disease_cases, insights.heartDiseaseInAgeGroup),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun getHeartDiseaseRiskCategory(probabilityPercentage: Double): String {
    return when {
        probabilityPercentage < 20 -> stringResource(R.string.low_risk)
        probabilityPercentage in 20.0..50.0 -> stringResource(R.string.moderate_risk)
        probabilityPercentage in 50.1..75.0 -> stringResource(R.string.high_risk)
        else -> stringResource(R.string.very_high_risk)
    }
}

@Composable
private fun getRecommendationMessage(probabilityPercentage: Double): String {
    return when {
        probabilityPercentage < 20 -> stringResource(R.string.low_risk_text)
        probabilityPercentage in 20.0..50.0 -> stringResource(R.string.moderate_risk_text)
        probabilityPercentage in 50.1..75.0 -> stringResource(R.string.high_risk_text)
        else -> stringResource(R.string.very_high_risk_text)
    }
}


