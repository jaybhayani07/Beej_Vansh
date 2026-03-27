package com.example.beej_vansh.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beej_vansh.data.model.Seed
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeed(seed: Seed): Long

    @Query("SELECT * FROM seeds")
    fun getSeeds(): Flow<List<Seed>>

    @Query("SELECT * FROM seeds WHERE cropName = :cropName")
    fun getSeedByCrop(cropName: String): Flow<List<Seed>>

    @Query("DELETE FROM seeds WHERE id = :id")
    suspend fun deleteSeed(id: String): Int

}