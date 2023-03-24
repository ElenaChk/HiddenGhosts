package com.example.hiddenghosts.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.hiddenghosts.MainViewModel
import com.example.hiddenghosts.ui.screens.GameScreen
import com.example.hiddenghosts.ui.screens.ResultScreen
import com.example.hiddenghosts.ui.screens.StartScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@Composable
fun NavHost(
    navController: NavHostController,
) {
    val viewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
    AnimatedNavHost(
        navController = navController,
        startDestination = Route.StartScreen.value
    ) {
        composable(route = Route.StartScreen.value) {
            StartScreen(
                onStartButtonClick = {
                    navController.navigate(Route.GameScreen.value)
                }
            )
        }
        composable(route = Route.GameScreen.value) {
            GameScreen(
                viewModel = viewModel,
                onGameFinished = {
                    navController.navigate(
                        route = Route.ResultScreen.value,
                        navOptions = navOptions { popUpTo(Route.StartScreen.value) }
                    )
                }
            )
        }
        composable(route = Route.ResultScreen.value) {
            ResultScreen(
                viewModel = viewModel,
                onNextLevelClick = {
                    navController.navigate(
                        route = Route.GameScreen.value,
                        navOptions = navOptions { popUpTo(Route.StartScreen.value) }
                    )
                }
            )
        }
    }
}