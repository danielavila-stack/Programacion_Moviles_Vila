package com.daniela.navlab_05.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniela.navlab_05.screens.DetailScreen
import com.daniela.navlab_05.screens.DirectoryScreen
import com.daniela.navlab_05.screens.HomeScreen
import com.daniela.navlab_05.screens.LoginScreen
import com.daniela.navlab_05.screens.ProfileScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { inputName ->
                    val nameToPass = if (inputName.isBlank()) "Daniela Vila Ramos" else inputName
                    navController.navigate(Screen.Home.createRoute(nameToPass)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "Daniela Vila Ramos"
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Daniela Vila Ramos"
            HomeScreen(
                userName = userName,
                onNavigateToDirectory = {
                    navController.navigate(Screen.Directory.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.createRoute(userName))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Directory.route) {
            DirectoryScreen(
                onStudentClick = { studentId ->
                    navController.navigate(Screen.Detail.createRoute(studentId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 1
            DetailScreen(
                studentId = studentId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "Daniela Vila Ramos"
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Daniela Vila Ramos"
            ProfileScreen(
                userName = userName,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
