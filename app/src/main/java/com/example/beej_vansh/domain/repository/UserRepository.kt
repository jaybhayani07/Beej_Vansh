package com.example.beej_vansh.domain.repository

import android.util.Log
import com.example.beej_vansh.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun saveUser(user: User) : Boolean{
        return try {
            usersCollection.document(user.uid).set(user).await()
            true
        }
        catch (e : Exception){
            Log.e("FirebaseError", "Failed to save user profile: ${e.message}", e)
            false
        }
    }
}