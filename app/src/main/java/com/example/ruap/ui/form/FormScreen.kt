package com.example.ruap.ui.form

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ruap.ui.theme.AppTheme
import com.example.ruap.R
import com.example.ruap.ui.form.enums.ChestPainType
import com.example.ruap.ui.form.enums.ExerciseAngina
import com.example.ruap.ui.form.enums.RestingECG
import com.example.ruap.ui.form.enums.Sex
import com.example.ruap.ui.form.enums.StSlope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navigateBack: () -> Unit,
    openResultScreen: (FormScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.form_screen_enter_data),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    )
    { paddingValues ->
        FormScreenContent(
            openResultScreen = {
                openResultScreen(it)
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
internal fun FormScreenContent(
    openResultScreen: (FormScreenState) -> Unit,
    modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.scrollable(
            state = scrollState,
            orientation = Orientation.Vertical
        )
    ) {
        Form(
            onSubmit = {
                openResultScreen(it)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun Form(
    onSubmit: (FormScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenState = rememberSaveable(
        saver = FormScreenStateSaver
    ) {
        mutableStateOf(
            FormScreenState(
                age = "",
                sex = null,
                chestPainType = null,
                restingBp = "",
                cholesterol = "",
                fastingBs = "",
                restingECG = null,
                maxHR = "",
                exerciseAngina = null,
                oldPeak = "",
                stSlope = null
            )
        )
    }

    val isFormValid by remember {
        derivedStateOf {
            with(screenState.value) {
                age.isNotBlank() &&
                        sex != null &&
                        chestPainType != null &&
                        restingBp.isNotBlank() &&
                        cholesterol.isNotBlank() &&
                        fastingBs.isNotBlank() &&
                        restingECG != null &&
                        maxHR.isNotBlank() &&
                        exerciseAngina != null &&
                        oldPeak.isNotBlank() &&
                        stSlope != null
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.value.age,
            onValueChange = {
                screenState.value = screenState.value.copy(age = it)
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = stringResource(R.string.form_screen_age)) },
            colors = TextFieldDefaults.colors(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormSexDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                screenState.value = screenState.value.copy(
                    sex = it
                )
            },
            screenState = screenState.value,
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormChestPainTypeDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                screenState.value = screenState.value.copy(
                    chestPainType = it
                )
            },
            screenState = screenState.value,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = screenState.value.restingBp,
            onValueChange = {
                screenState.value = screenState.value.copy(restingBp = it)
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = stringResource(R.string.form_screen_restingbp)) },
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = screenState.value.cholesterol,
            onValueChange = {
                screenState.value = screenState.value.copy(cholesterol = it)
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Cholesterol") },
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormFastingBSDropdownMenu(
            onClick = {
                screenState.value = screenState.value.copy(
                    fastingBs = it
                )
            },
            screenState = screenState.value,
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormRestingECGDropdownMenu(
            onClick = {
                screenState.value = screenState.value.copy(
                    restingECG = it
                )
            },
            screenState = screenState.value
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = screenState.value.maxHR,
            onValueChange = {
                screenState.value = screenState.value.copy(maxHR = it)
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Max HR") },
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormExerciseAnginaDropdownMenu(
            onClick = {
                screenState.value = screenState.value.copy(
                    exerciseAngina = it
                )
            },
            screenState = screenState.value,
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormOldPeakDropdownMenu(
            onClick = {
                screenState.value = screenState.value.copy(
                    oldPeak = it
                )
            },
            screenState = screenState.value
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormStSlopeDropdownMenu(
            onClick = {
                screenState.value = screenState.value.copy(
                    stSlope = it
                )
            },
            screenState = screenState.value
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSubmit(screenState.value)
            },
            enabled = isFormValid
        ) {
            Text(
                text = stringResource(R.string.form_screen_check_button)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormSexDropdownMenu(
    onClick: (Sex) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.sex?.name ?: stringResource(R.string.form_screen_sex),
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_sex)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {},
            ) {
                Sex.entries.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormChestPainTypeDropdownMenu(
    onClick: (ChestPainType) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.chestPainType?.name ?: stringResource(R.string.form_screen_chest_pain),
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_chest_pain)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                ChestPainType.entries.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormFastingBSDropdownMenu(
    onClick: (String) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    val fastingBS = listOf("0", "1")

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.fastingBs,
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_fastingbs)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                fastingBS.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormRestingECGDropdownMenu(
    onClick: (RestingECG) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.restingECG?.name ?: stringResource(R.string.form_screen_resting_ecg) ,
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_resting_ecg)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                RestingECG.entries.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormExerciseAnginaDropdownMenu(
    onClick: (ExerciseAngina) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.exerciseAngina?.name ?: stringResource(R.string.form_screen_exercise_angina) ,
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_exercise_angina)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                ExerciseAngina.entries.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormOldPeakDropdownMenu(
    onClick: (String) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    val fastingBS = listOf("0", "0.5", "1", "1.5", "2", "2.5", "3")

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.oldPeak,
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_old_peak)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                fastingBS.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormStSlopeDropdownMenu(
    onClick: (StSlope) -> Unit,
    screenState: FormScreenState,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = screenState.stSlope?.name ?: stringResource(R.string.form_screen_st_slope) ,
                onValueChange = {
                },
                maxLines = 1,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = stringResource(R.string.form_screen_st_slope)) },
                colors = TextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
            ) {
                StSlope.entries.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            expanded = false
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun FormScreenPreview() {
    AppTheme {
        FormScreen(
            navigateBack = {},
            openResultScreen = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}