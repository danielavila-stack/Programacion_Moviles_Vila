package com.daniela.navlab_05.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home/{userName}") {
        fun createRoute(userName: String): String = "home/$userName"
    }
    object Directory : Screen("directory")
    object Detail : Screen("detail/{studentId}") {
        fun createRoute(studentId: Int): String = "detail/$studentId"
    }
    object Profile : Screen("profile/{userName}") {
        fun createRoute(userName: String): String = "profile/$userName"
    }
}
