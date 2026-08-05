package com.example.beej_vansh.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beej_vansh.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var isVerificationSent = mutableStateOf(false)
    var savedEmail = mutableStateOf("")
    var resendTime = mutableStateOf(0)

    var userData = mutableStateOf<User?>(null);

    fun updateUserSeedCount(newCount : Int){
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid

            if(currentUserId != null){
                try{
                    firestore.collection("users").document(currentUserId).update("seedCount",newCount)
                        .await()

                    userData.value = userData.value?.copy(seedCount = newCount)
                }
                catch (e : Exception){
                    Log.d("seedCount",e.message.toString())
                }
            }
        }
    }

    fun fetchUserData(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            viewModelScope.launch {
                try{
                    val document = firestore.collection("users").document(currentUser.uid).get().await()
                    if(document.exists()){
                        userData.value = document.toObject(User::class.java)
                    }
                }
                catch (e : Exception){
                    Log.e("User","Failed to fetch user")
                }
            }
        }
    }

    fun sendPasswordLessEmail(email : String,onSuccess : () -> Unit,onError : (String) -> Unit){
        val actionCodeSetting = ActionCodeSettings.newBuilder()
            .setUrl("https://beejvansh-860b8.firebaseapp.com/login")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.example.beej_vansh",true,"12")
            .build()

        auth.sendSignInLinkToEmail(email,actionCodeSetting)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    savedEmail.value = email;
                    isVerificationSent.value = true
                    startResendTimer()
                    onSuccess()
                }
                else{
                    onError(task.exception?.message ?: "Failed to send link!!!")
                }
            }
    }

    fun handleEmailLink(intent : Intent?,userProfile : User,onSuccess: () -> Unit,onError: (String) -> Unit){
        val emailLink = intent?.data?.toString()

        if(emailLink != null && auth.isSignInWithEmailLink(emailLink)){
            val emailToVerify = savedEmail.value

            if(emailToVerify.isNotEmpty()){
                auth.signInWithEmailLink(emailToVerify,emailLink)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            val currentUser = auth.currentUser
                            if(currentUser != null){
                                viewModelScope.launch {
                                    try{
                                        val document = firestore.collection("users").document(currentUser.uid).get().await()
                                        if(!document.exists()){
                                            val finalProfile = userProfile.copy(uid = currentUser.uid)
                                            firestore.collection("users").document(currentUser.uid).set(finalProfile).await()
                                        }
                                        onSuccess()
                                    }
                                    catch (e : Exception){
                                        onError("Failed to check or save profile")
                                    }
                                }
                            }
                        }
                        else{
                            onError("Error signing in with this link")
                        }
                    }
            }
            else{
                onError("Email session lost")
            }
        }
        else {
            onError("Invalid or expired link")
        }
    }

    private fun startResendTimer() {
        viewModelScope.launch {
            resendTime.value = 45
            while(resendTime.value > 0){
                delay(1000L)
                resendTime.value -= 1;
            }
        }
    }

    fun signInAnonymously(onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    viewModelScope.launch {
                        try {
                            val document = firestore.collection("users").document(currentUser.uid).get().await()
                            if (!document.exists()) {
                                // Create a dummy guest profile
                                val guestProfile = User(
                                    uid = currentUser.uid,
                                    firstName = "Guest",
                                    lastName = "User",
                                    email = "guest@demo.com",
                                    phoneNumber = "0000000000",
                                    soilType = "N/A",
                                    village = "Demo Village",
                                    taluka = "Demo Taluka",
                                    district = "Demo District",
                                    state = "Demo State",
                                    lat = 0.0,
                                    lon = 0.0
                                )
                                firestore.collection("users").document(currentUser.uid).set(guestProfile).await()
                            }
                            onSuccess()
                        } catch (e: Exception) {
                            onError("Failed to create guest profile")
                        }
                    }
                }
            } else {
                onError(task.exception?.message ?: "Failed to sign in anonymously")
            }
        }
    }
}