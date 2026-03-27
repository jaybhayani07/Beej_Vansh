package com.example.beej_vansh

import android.app.Application
import androidx.room.Room
import com.example.beej_vansh.data.local.SeedDatabase

class CreateRoom : Application(){

    // This variable will hold our database
    lateinit var database: SeedDatabase

    override fun onCreate() {
        super.onCreate()

        // This command builds the database when the app starts
        database = Room.databaseBuilder(
            applicationContext,
            SeedDatabase::class.java,
            "beejvansh_db"
        ).build()
    }
}