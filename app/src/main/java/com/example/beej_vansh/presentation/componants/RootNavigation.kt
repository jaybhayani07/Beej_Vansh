package com.example.beej_vansh.presentation.componants

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beej_vansh.data.ViewModel.HomeViewModel
import com.example.beej_vansh.domain.repository.SeedRepository
import com.example.beej_vansh.presentation.screens.BeejVanshApp
import com.example.beej_vansh.presentation.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.example.beej_vansh.presentation.screens.BeejVanshApp

@Composable
fun RootNavigation(homeViewModel: HomeViewModel,seedRepository: SeedRepository){
    val navController = rememberNavController()

    var startDestination = if(FirebaseAuth.getInstance().currentUser != null){
        "main_app"
    } else {
        "login"
    }

    NavHost(navController,startDestination = startDestination){
        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("main_app"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("main_app") {
            BeejVanshApp(
                homeViewModel,
                seedRepository
            )
        }
    }
}