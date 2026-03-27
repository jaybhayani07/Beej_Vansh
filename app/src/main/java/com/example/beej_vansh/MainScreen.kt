package com.example.beej_vansh.presentation.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.beej_vansh.data.ViewModel.HomeRoute
import com.example.beej_vansh.data.ViewModel.HomeViewModel
import com.example.beej_vansh.data.ViewModel.RequestViewModel
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.domain.repository.RequestRepository
import com.example.beej_vansh.domain.repository.SeedRepository
import com.example.beej_vansh.presentation.componants.CardDetail
import com.example.beej_vansh.utils.AuthViewModel
import com.google.gson.Gson

// 1. Define the 5 Routes
sealed class Screen(val route: String, val title: String, val iconSelected: ImageVector, val iconUnselected: ImageVector) {
    object Mandi : Screen("mandi", "Mandi", Icons.Filled.Home, Icons.Outlined.Home)
    object Add : Screen("add", "Add", Icons.Filled.Add, Icons.Default.Add)
    object Activity : Screen("activity", "Activity", Icons.Filled.Notifications, Icons.Outlined.Notifications)
    object Khata : Screen("khata", "Khata", Icons.Filled.Book, Icons.Outlined.Book)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

val repository = RequestRepository()
val authViewModel = AuthViewModel()
val requestViewModel = RequestViewModel(repository)

@Composable
fun BeejVanshApp(homeViewModel: HomeViewModel, seedRepository: SeedRepository) {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Mandi,
        Screen.Add,
        Screen.Activity,
        Screen.Khata,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // ✅ THE FIX: Check if the current route is the detail screen
            val showBottomBar = currentRoute?.startsWith("cardDetail") != true

            // ✅ Wrap the entire Row in the if statement so it only draws when showBottomBar is true
            if (showBottomBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { screen ->
                        val isSelected = currentRoute == screen.route

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF2E7D32) else Color.Transparent)
                                .clickable {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) screen.iconSelected else screen.iconUnselected,
                                    contentDescription = screen.title,
                                    tint = if (isSelected) Color.White else Color(0xFF795548), // White or Brown
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = screen.title,
                                    color = if (isSelected) Color.White else Color(0xFF795548),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } // <-- End of the if statement
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Mandi.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Mandi.route) {
                HomeRoute(navController = navController, viewModel = homeViewModel, authViewModel)
            }
            composable(Screen.Add.route) {
                AddSeed(seedRepository, authViewModel)
            }
            composable(Screen.Activity.route) {
               ActivityPage(requestViewModel, authViewModel)
            }
            composable(Screen.Khata.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("Khata Screen") }
            }
            composable(Screen.Profile.route) {
                ProfileScreen(authViewModel = authViewModel)
            }

            composable(
                route = "cardDetail/{seedJson}",
                arguments = listOf(navArgument("seedJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("seedJson")
                val seed = Gson().fromJson(Uri.decode(encodedJson), Seed::class.java)

                CardDetail(seed = seed, navController,authViewModel,requestViewModel)
            }
        }
    }
}