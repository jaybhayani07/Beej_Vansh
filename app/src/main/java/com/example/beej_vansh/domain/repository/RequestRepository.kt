package com.example.beej_vansh.domain.repository

import android.util.Log
import androidx.compose.material3.Divider
import com.example.beej_vansh.data.model.Request
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RequestRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun checkRequestStatus(seedId: String, buyerId: String): String {
        return try {
            val snapshot = firestore.collection("requests")
                .whereEqualTo("seedId", seedId)
                .whereEqualTo("buyerId", buyerId)
                .get()
                .await()

            if (snapshot.isEmpty) {
                "NONE"
            } else {
                snapshot.documents[0].getString("status") ?: "PENDING"
            }
        } catch (e: Exception) {
            "NONE"
        }
    }

    fun getSendRequest(myId: String) : Flow<List<Request>> = callbackFlow {
        val listener = firestore.collection("requests")
            .whereEqualTo("buyerId",myId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val list = snapshots?.toObjects(Request::class.java) ?: emptyList()
                trySend(list)
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun createRequest(request : Request) : Boolean{
        return try{
            firestore.collection("requests").document(request.id).set(request).await()
            true
        }
        catch (e : Exception){
            Log.e("Firestore",e.message.toString())
            false
        }
    }

    fun getReceivedRequest(myId : String) : Flow<List<Request>> = callbackFlow {
        val listener = firestore.collection("requests")
            .whereEqualTo("sellerId",myId)
            .addSnapshotListener { snapshots, error ->
                if(error != null){
                    Log.e("FirebaseError","Listen failed",error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                Log.d("BeejVansh", "Raw documents found in Firestore: ${snapshots?.size() ?: 0}")

                // ✅ LOG THE FIRST DOCUMENT ID FOUND
                snapshots?.documents?.firstOrNull()?.let {
                    Log.d("BeejVansh", "First Doc ID found: ${it.id} with status: ${it.get("status")}")
                }

                val list = snapshots?.toObjects(Request::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateRequestStatus(requestId : String,newStatus : String){
        firestore.collection("requests")
            .document(requestId)
            .update("status",newStatus)
            .await()
    }
}