package com.example.beej_vansh.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",indices = [Index(value = ["phoneNumber"], unique = true)])
data class User(
    @PrimaryKey
    val uid : String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email : String = "",
    val phoneNumber: String = "",
    val soilType: String = "",
    val village: String = "",
    val taluka: String = "",
    val district: String = "",
    val state: String = "",
    val seedCount : Int = 0,
    val lat : Double = 0.0,
    val lon : Double = 0.0
)
