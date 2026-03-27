package com.example.beej_vansh.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.location.Geocoder
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beej_vansh.data.model.User
import com.example.beej_vansh.presentation.componants.OtpView
import com.example.beej_vansh.presentation.screens.authViewModel
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.brownText
import com.example.beej_vansh.ui.theme.cardBackground
import com.example.beej_vansh.ui.theme.optionColor
import com.example.beej_vansh.utils.AuthViewModel
import com.google.android.play.integrity.internal.ac
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Dispatcher
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if(FirebaseAuth.getInstance().currentUser != null){
            onNavigateToHome()
        }
    }
    val scope = rememberCoroutineScope()
    var email by rememberSaveable{mutableStateOf("")}
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var phoneNo by rememberSaveable { mutableStateOf("") }
    var soilType by rememberSaveable { mutableStateOf("") }
    var village by rememberSaveable { mutableStateOf("") }
    var taluka by rememberSaveable { mutableStateOf("") }
    var district by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf("") }
    var otpValues by rememberSaveable { mutableStateOf(List(6) { "" }) }

    val isVerificationSent by authViewModel.isVerificationSent
    val resendTime by authViewModel.resendTime

    val activity = context.findActivity()


    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize().background(background)
        ) {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                        Row {
                            Icon(imageVector = Icons.Default.PeopleAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                            Spacer(modifier = Modifier.width(13.dp))
                            Text(text = "Create Your Profile", fontWeight = FontWeight.ExtraBold, color = Color.White,fontSize = 25.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = optionColor),
            )

            if(!isVerificationSent){
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item {
                        Text(text = "Enter Your Name :-", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = brownText,
                        )
                    }
                    item {
                        Row() {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("First Name") },
                                modifier = Modifier.weight(1f).padding(top = 4.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = cardBackground,
                                    unfocusedContainerColor = cardBackground,
                                    focusedBorderColor = brownText,
                                    unfocusedBorderColor = brownText,
                                ),
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Last Name") },
                                modifier = Modifier.weight(1f).padding(top = 4.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = cardBackground,
                                    unfocusedContainerColor = cardBackground,
                                    focusedBorderColor = brownText,
                                    unfocusedBorderColor = brownText,
                                ),
                            )
                        }
                    }

                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Enter Your Email Address :-", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = brownText,
                        )
                    }
                    item{
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cardBackground,
                                unfocusedContainerColor = cardBackground,
                                focusedBorderColor = brownText,
                                unfocusedBorderColor = brownText,
                            ),
                            singleLine = true
                        )
                    }

                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Enter Your Phone Number :-", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = brownText,
                        )
                    }
                    item{
                        OutlinedTextField(
                            value = phoneNo,
                            onValueChange = { phoneNo = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cardBackground,
                                unfocusedContainerColor = cardBackground,
                                focusedBorderColor = brownText,
                                unfocusedBorderColor = brownText,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Enter Soil Type :-", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = brownText,
                        )
                    }

                    item{
                        OutlinedTextField(
                            value = soilType,
                            onValueChange = { soilType = it },
                            label = { Text("Soil Type") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cardBackground,
                                unfocusedContainerColor = cardBackground,
                                focusedBorderColor = brownText,
                                unfocusedBorderColor = brownText,
                            ),
                            singleLine = true
                        )
                    }

                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Enter Your Location :-", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = brownText,
                        )
                    }

                    item{
                        Column() {
                            Row() {
                                OutlinedTextField(
                                    value = village,
                                    onValueChange = { village = it },
                                    label = { Text("Village") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = cardBackground,
                                        unfocusedContainerColor = cardBackground,
                                        focusedBorderColor = brownText,
                                        unfocusedBorderColor = brownText,
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(15.dp))

                                OutlinedTextField(
                                    value = taluka,
                                    onValueChange = { taluka = it },
                                    label = { Text("Taluka") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = cardBackground,
                                        unfocusedContainerColor = cardBackground,
                                        focusedBorderColor = brownText,
                                        unfocusedBorderColor = brownText,
                                    ),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            Row() {
                                OutlinedTextField(
                                    value = district,
                                    onValueChange = { district = it },
                                    label = { Text("District") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = cardBackground,
                                        unfocusedContainerColor = cardBackground,
                                        focusedBorderColor = brownText,
                                        unfocusedBorderColor = brownText,
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(15.dp))

                                OutlinedTextField(
                                    value = state,
                                    onValueChange = { state = it },
                                    label = { Text("State") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = cardBackground,
                                        unfocusedContainerColor = cardBackground,
                                        focusedBorderColor = brownText,
                                        unfocusedBorderColor = brownText,
                                    ),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    item{
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    item {
                        Row(){
                            Button(
                                onClick = {
                                    if(phoneNo.length != 10){
                                        Toast.makeText(context, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                                    }
                                    else if(email.isEmpty()){
                                        Toast.makeText(context, "Enter Email Address", Toast.LENGTH_SHORT).show()
                                    }
                                    else if(firstName.isEmpty() || lastName.isEmpty()|| village.isEmpty()|| taluka.isEmpty()|| district.isEmpty()|| state.isEmpty()){
                                        Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        authViewModel.sendPasswordLessEmail(email,
                                            onSuccess = {
                                                Toast.makeText(context,"Sending Link...", Toast.LENGTH_SHORT).show()
                                            },
                                            onError = { errorMsg ->
                                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    }

                                },
                                colors = ButtonDefaults.buttonColors(containerColor = optionColor),
                                modifier = Modifier.weight(1f).height(60.dp)

                            ){
                                Text(text = "Submit", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
            else{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                ) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(100.dp)).background(cardBackground).size(120.dp)){
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).align(Alignment.Center),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "We have sent a login link to", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp,color = brownText)
                    Text(text = email, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp,color = brownText)

                    Spacer(modifier = Modifier.height(30.dp))

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Edit Email Address",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            authViewModel.isVerificationSent.value = false
                        }
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    if(resendTime > 0){
                        Text(text = "Resend Link in ${resendTime}s", color = Color.Gray)
                    }
                    else{
                        Text(text = "Resend Link",color = Color.Blue,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.clickable(onClick = {
                                try{
                                    authViewModel.sendPasswordLessEmail(email,
                                        onSuccess = {
                                            Toast.makeText(context,"Link Resent!!", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { errorMsg->
                                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                }
                                catch (e : Exception){
                                    Toast.makeText(context,"Failed to Resent Link", Toast.LENGTH_LONG).show()
                                }
                            })
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                                scope.launch {

                                    var userLat = 0.0
                                    var userLon = 0.0

                                    try{
                                        withTimeoutOrNull(2000L){
                                            withContext(Dispatchers.IO){
                                                val geocoder = Geocoder(context,Locale.getDefault())
                                                val locationString = "$village, $district, India"

                                                if(village.isNotBlank() && district.isNotBlank()){
                                                    val addresses = geocoder.getFromLocationName(locationString,1)
                                                    if(!addresses.isNullOrEmpty()){
                                                        userLat = addresses[0].latitude
                                                        userLon = addresses[0].longitude
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    catch (e : Exception){
                                        Log.e("Geocode", "Location lookup failed: ${e.message}")
                                    }

                                    val userProfile = User(
                                        firstName = firstName,
                                        lastName = lastName,
                                        email = email,
                                        phoneNumber = phoneNo,
                                        soilType = soilType,
                                        village = village,
                                        taluka = taluka,
                                        district = district,
                                        state = state,
                                        lat = userLat,
                                        lon = userLon
                                    )

                                    authViewModel.handleEmailLink(intent = activity?.intent,
                                        userProfile = userProfile,
                                        onSuccess = {
                                            scope.launch {
                                                Toast.makeText(context,"Welcome!!", Toast.LENGTH_SHORT).show()
                                                onNavigateToHome()
                                            }
                                        },
                                        onError = { errorMsg->
                                            scope.launch {
                                                Toast.makeText(context,errorMsg, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = optionColor),
                        modifier = Modifier.width(330.dp).height(50.dp)
                    ) {
                        Text(text = "Verify..",fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

        }
    }
}
fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}