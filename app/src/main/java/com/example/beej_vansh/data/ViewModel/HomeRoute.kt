package com.example.beej_vansh.data.ViewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.presentation.screens.HomeScreen
import com.example.beej_vansh.utils.AuthViewModel

@Composable
fun HomeRoute(navController: NavController,viewModel: HomeViewModel,authViewModel: AuthViewModel){
    val user by authViewModel.userData
    val seed by viewModel.seedList.collectAsState()

    LaunchedEffect(user) {
        if(user != null && user!!.lat != 0.0){
            Log.d("Jay", "User loaded with location: ${user!!.lat}, ${user!!.lon}")
            viewModel.listenForRealTimeSeeds(user!!.lat,user!!.lon, currentUserId = user!!.uid)
        }
        else {
            Log.d("Jay", "User is null or location is 0.0. Showing default list.")
            viewModel.listenForRealTimeSeeds(buyerLat = null, buyerLon = null,null)
        }
    }

    HomeScreen(seedList = seed,authViewModel = authViewModel, navController = navController)
}