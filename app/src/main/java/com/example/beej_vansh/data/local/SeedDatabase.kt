package com.example.beej_vansh.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.data.model.User

@Database(entities = [Seed::class, User::class], version = 5, exportSchema = false)
abstract class SeedDatabase : RoomDatabase() {
    abstract fun seedDao() : SeedDao
}