package com.example.ruap.ui

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.ruap.ui.form.FormScreen
import com.example.ruap.ui.result.ResultScreen
import com.example.ruap.ui.routes.FormScreen
import androidx.navigation.toRoute
import androidx.navigation.compose.NavHost
import com.example.ruap.ui.result.model.toResultScreenData
import com.example.ruap.ui.routes.Home
import com.example.ruap.ui.routes.ResultScreen
import com.example.ruap.ui.routes.toResultScreen

@Composable
internal fun NavHost(
    navController: NavHostController,
) {
    NavHost (
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                openFormScreen = {
                    navController.navigate(FormScreen)
                }
            )
        }

        composable<FormScreen> {
            FormScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                openResultScreen = { formState ->
                    val resultScreenData = formState.toResultScreen()

                    navController.navigate(resultScreenData)
                }
            )
        }

        composable<ResultScreen> { backStackEntry ->
            val resultScreenData = backStackEntry.toRoute<ResultScreen>()

            ResultScreen(
                navigateBack = { navController.popBackStack() },
                resultScreenData = resultScreenData.toResultScreenData(),
                navigateOnHomeScreen = {
                    navController.navigate(Home)
                }
            )
        }
    }
}
