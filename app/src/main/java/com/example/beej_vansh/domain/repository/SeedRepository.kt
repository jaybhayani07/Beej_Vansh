package com.example.beej_vansh.domain.repository

import android.util.Log
import com.example.beej_vansh.data.local.SeedDao
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.data.remote.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SeedRepository(private val seedDao: SeedDao) {

    private val firestore = FirebaseFirestore.getInstance()
    private val seedCollection = firestore.collection("seeds")
    suspend fun uploadToCloudinary(imageFile: File): String? {
        return try {
            val cloudName = "dbtwzmxio"
            val uploadPreset = "seed_app"

            val requestFile = imageFile.asRequestBody("image/jpg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            val presetBody = uploadPreset.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = RetrofitClient.api.uploadImage(cloudName, presetBody, body)

            if (response.isSuccessful) {
                response.body()?.secure_url
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertSeed(seed: Seed)  : Boolean{
        return try{
            seedDao.insertSeed(seed)
            seedCollection.document(seed.id).set(seed)
            true
        }
        catch (e : Exception){
            false
        }
    }

    fun getFilteredSeeds(field : String, value : String) : Flow<List<Seed>> = callbackFlow{
        val listener = seedCollection
            .whereEqualTo(field,value)
            .addSnapshotListener { snapshots, exception ->
                if(exception != null){
                    return@addSnapshotListener
                }

                val list = snapshots?.toObjects(Seed::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose {
            listener.remove()
        }
    }
    fun getAllSeeds() : Flow<List<Seed>> = callbackFlow {
        val subscription = seedCollection.addSnapshotListener { snapshots, exception ->
            if(exception != null){
                Log.e("FirebaseError","Listen Failed",exception)
                // Do not close(exception) as it crashes the app if unhandled
                trySend(emptyList())
                return@addSnapshotListener
            }

            if(snapshots != null){
                val seed = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(Seed::class.java)
                }

                trySend(seed).isSuccess
            }
        }

        awaitClose {
            subscription.remove()
        }
    }

    suspend fun getAllSeedsFromFirebase() : List<Seed>{
        return try{
            val seedList = seedCollection.get().await()
            seedList.documents.mapNotNull { doc->
                doc.toObject(Seed::class.java)
            }
        }
        catch (e : Exception){
            Log.e("FirebaseError", "Failed to fetch seeds: ${e.message}", e)
            emptyList()
        }
    }
}