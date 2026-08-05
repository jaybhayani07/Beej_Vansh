package com.example.beej_vansh.data.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beej_vansh.data.local.SeedDao
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.domain.repository.SeedRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SeedRepository) : ViewModel(){
    private var _seedList = MutableStateFlow<List<Seed>>(emptyList())
    var seedList : StateFlow<List<Seed>> = _seedList.asStateFlow()

    private var _filteredList = MutableStateFlow<List<Seed>>(emptyList())
    var filteredList : StateFlow<List<Seed>> = _filteredList.asStateFlow()
    private var seedJob: Job? = null



    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadiusKm * c
    }

    fun listenForFilteredList(field : String,value : String){
        viewModelScope.launch {
            repository.getFilteredSeeds(field,value).collect {
                _filteredList.value = it
            }
        }
    }

    fun listenForRealTimeSeeds(buyerLat: Double? = null, buyerLon: Double? = null,currentUserId : String? = null){
        Log.d("Jay", "ViewModel: Calculating with Lat=$buyerLat, Lon=$buyerLon")
        seedJob?.cancel()
        seedJob = viewModelScope.launch {
            repository.getAllSeeds().collect { liveSeed ->
                val sortedSeed = liveSeed.filter { it.sellerId != currentUserId }.map { seed ->

                    if(buyerLat != null && buyerLon != null && seed.lat != 0.0 && seed.lon != 0.0){
                        val dist = calculateDistance(buyerLat, buyerLon, seed.lat, seed.lon)
                        seed.copy(distance = dist)
                    } else {
                        seed.copy(distance = 9999.0)
                    }

                }.sortedBy { it.distance }

                _seedList.value = sortedSeed
            }
        }
    }
}