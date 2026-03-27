package com.example.beej_vansh

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beej_vansh.data.ViewModel.HomeRoute
import com.example.beej_vansh.data.ViewModel.HomeViewModel
import com.example.beej_vansh.domain.repository.SeedRepository
import com.example.beej_vansh.presentation.componants.RootNavigation
import com.example.beej_vansh.presentation.screens.AddSeed
import com.example.beej_vansh.presentation.screens.BeejVanshApp
import com.example.beej_vansh.presentation.screens.ProfileScreen
import com.example.beej_vansh.ui.theme.Beej_VanshTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        androidx.room.Room.databaseBuilder(
            applicationContext,
            com.example.beej_vansh.data.local.SeedDatabase::class.java,
            "seed_database"
        ).fallbackToDestructiveMigration(false).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Beej_VanshTheme {
                val seedRepository = remember { SeedRepository(db.seedDao()) }

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (!isGranted) {
                        Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
                    }
                }

                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (!isGranted) {
                        Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

                val factory = remember {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return HomeViewModel(seedRepository) as T
                        }
                    }
                }

                val homeViewModel: HomeViewModel = viewModel(factory = factory)

                RootNavigation(homeViewModel,seedRepository)
                //ProfileScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // This catches the email link and saves it so your button can read it!
        setIntent(intent)
    }
}