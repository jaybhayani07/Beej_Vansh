package com.example.beej_vansh.presentation.screens

import android.content.res.Configuration
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.beej_vansh.R
import com.example.beej_vansh.data.model.PackageMethod
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.data.model.StorageMethod
import com.example.beej_vansh.data.model.WeightUnit
import com.example.beej_vansh.domain.repository.SeedRepository
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.brownText
import com.example.beej_vansh.ui.theme.cardBackground
import com.example.beej_vansh.ui.theme.optionColor
import com.example.beej_vansh.utils.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSeed( repository: SeedRepository, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    val user by authViewModel.userData

    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    val userName = if(user != null)"${user!!.firstName} ${user!!.lastName}" else "Loading.."
    val phoneNumber = if(user != null) user!!.phoneNumber else "Loading.."
    val location = if(user != null) "${user!!.village}, ${user!!.district}" else "Loading.."
    val soilType = if(user!= null) user!!.soilType else "Loading.."

    var showCamera by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var capturedFile by remember { mutableStateOf<File?>(null) }
    var selectedCrop by remember { mutableStateOf("") }
    var selectedPackage by remember { mutableStateOf("") }
    var varietyName by remember { mutableStateOf("") }
    var selectedStorage by remember { mutableStateOf("") }
    var flag by remember { mutableStateOf(0) }
    var packageFlag by remember { mutableStateOf(0) }
    var harvestYear by remember { mutableStateOf("") }
    var storageFlag by remember { mutableStateOf(0) }
    var quantity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf("Kg") }
    var selectedprice by remember { mutableStateOf("") }
    val options = listOf("Kg", "Man(20 KG)", "Khaandi(400 KG)")
    var isYearError = harvestYear.isNotEmpty() && harvestYear.length < 4
    var isOther by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Add Seed Listing", color = Color.White)
                        Text(text = "Share seeds with your community", fontSize = 12.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF795548)),
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(text = "Select Crop Type", modifier = Modifier.padding(15.dp), fontWeight = FontWeight.Bold, color = brownText)
                }

                val cropRows = listOf(
                    listOf(Triple("Groundnuts", R.drawable.groundnuts, 1), Triple("Wheat", R.drawable.wheat, 2), Triple("Jeera", R.drawable.jeera, 3)),
                    listOf(Triple("Dhaniya", R.drawable.dhaniya, 4), Triple("Cotton", R.drawable.cotton, 5), Triple("Soyabean", R.drawable.soyabean, 6))
                )

                cropRows.forEach { rowCrops ->
                    item {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowCrops.forEach { crop ->
                                Card(
                                    modifier = Modifier.weight(1f).height(100.dp),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    onClick = {
                                        flag = crop.third
                                        selectedCrop = crop.first
                                        isOther = false
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize().background(if (flag == crop.third) optionColor else cardBackground),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(painter = painterResource(crop.second), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Text(text = crop.first, fontSize = 12.sp, color = brownText, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .height(100.dp)
                                .width(118.dp)
                                .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = {
                                    flag = 7
                                    isOther = true
                                })
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().background(if(flag == 7) Color(0xFF2E7D32) else background),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "+", fontSize = 30.sp, color = brownText, fontWeight = FontWeight.Bold)
                                Text(text = "Other", fontSize = 12.sp, color = brownText, fontWeight = FontWeight.Bold)
                            }
                        }

                        if(isOther) {
                            OutlinedTextField(
                                value = selectedCrop,
                                onValueChange = {selectedCrop = it},
                                label = { Text("Enter Seed Name") },
                                modifier = Modifier.padding(top = 20.dp).width(200.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = cardBackground,
                                    unfocusedContainerColor = cardBackground,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedTextColor = brownText,
                                    unfocusedTextColor = brownText
                                )
                            )
                        }
                    }
                }

                item {
                    Text(text = "Packaging Type", modifier = Modifier.padding(15.dp), fontWeight = FontWeight.Bold, color = brownText)
                }

                val packageOptions = listOf("Gunny" to 1, "Packet" to 2, "Open" to 3)

                item {
                    Box(
                        modifier = Modifier.padding(start = 12.dp).height(60.dp).clip(RoundedCornerShape(20.dp)).background(cardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.width(370.dp).background(cardBackground),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            packageOptions.forEach { option ->
                                Card(
                                    modifier = Modifier.weight(1f).padding(4.dp).height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = {
                                        packageFlag = option.second
                                        selectedPackage = option.first
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(if(packageFlag == option.second) Color(0xFF2E7D32) else cardBackground),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(text = option.first, fontSize = 18.sp, color = brownText, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }
                }
                item{
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text(text = "Variety", modifier = Modifier.padding(start = 15.dp,top = 10.dp), fontWeight = FontWeight.Bold, color = brownText)
                }

                item {
                    OutlinedTextField(
                        value = varietyName,
                        onValueChange = { varietyName = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            focusedBorderColor = Color.Gray,
                            unfocusedTextColor = Color.Gray,
                        ),
                        label={
                            Text(text = "Variety", color = brownText)
                        },
                        modifier = Modifier.padding(start = 15.dp).width(340.dp),
                        singleLine = true
                    )
                }

                item{
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text(text = "Harvest Year", modifier = Modifier.padding(start = 15.dp,top = 10.dp), fontWeight = FontWeight.Bold, color = brownText)
                }

                item {
                    OutlinedTextField(
                        value = harvestYear,
                        onValueChange = { harvestYear = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            focusedBorderColor = Color.Gray,
                            unfocusedTextColor = Color.Gray,
                        ),
                        isError = isYearError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label={
                            Text(text = "Harvest Year", color = brownText)
                        },
                        supportingText = {
                            if (isYearError) {
                                Text(text = "Invalid Year", color = Color.Red)
                            }
                        },
                        modifier = Modifier.padding(start = 15.dp).width(340.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                            )
                        },
                        singleLine = true
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text(text = "Storage Method", modifier = Modifier.padding(start = 15.dp), fontWeight = FontWeight.Bold, color = brownText)
                }

                val storage1 = listOf("Gunny" to 1, "Metal Drum" to 2, "Open" to 3)

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Row {
                        storage1.forEach { option ->
                            Card(
                                modifier = Modifier.height(40.dp).weight(1f).padding(start = 15.dp,end=10.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                onClick = {
                                    storageFlag = option.second
                                    selectedStorage = option.first
                                }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().background(if(storageFlag == option.second) Color(0xFF2E7D32) else cardBackground),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = option.first, fontSize = 16.sp, color = brownText, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Text(text = "Quantity (Expected , If Don't Know)", modifier = Modifier.padding(start = 15.dp), fontWeight = FontWeight.ExtraBold, color = brownText)
                }

                item {
                    Row {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cardBackground,
                                unfocusedContainerColor = cardBackground,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                            ),
                            label = {
                                Text(text = "Quantity", color = brownText)
                            },
                            modifier = Modifier.padding(start = 15.dp).width(200.dp),
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Unit") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier.menuAnchor().padding(top = 8.dp,start = 15.dp).width(150.dp),
                                colors = ExposedDropdownMenuDefaults.textFieldColors(
                                    focusedContainerColor = cardBackground,
                                    unfocusedContainerColor = cardBackground,
                                    focusedTextColor = brownText,
                                    unfocusedTextColor = brownText,
                                ),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(text = option)},
                                        onClick = {
                                            selectedUnit = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Text(text = "Price (₹ / ${selectedUnit})", modifier = Modifier.padding(start = 15.dp), fontWeight = FontWeight.ExtraBold, color = brownText)
                }

                item {
                    OutlinedTextField(
                        value = selectedprice ,
                        onValueChange = { selectedprice = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            focusedBorderColor = Color.Gray,
                            unfocusedTextColor = Color.Gray,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(text = "Price per ${selectedUnit}", color = brownText)
                        },
                        modifier = Modifier.padding(start = 15.dp).width(340.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CurrencyRupee,
                                contentDescription = null,
                            )
                        },
                        singleLine = true
                    )
                }

                item {
                    Text("Seed Photo", modifier = Modifier.padding(15.dp), fontWeight = FontWeight.ExtraBold, color = brownText)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(start = 12.dp, end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (showCamera || capturedFile != null) Color.Black else background)
                            .border(width = 5.dp, color = brownText, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (capturedFile != null) {
                            AsyncImage(model = capturedFile, contentDescription = null, modifier = Modifier.fillMaxSize())
                            Button(
                                onClick = {
                                    capturedFile = null
                                    showCamera = true
                                },
                                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
                            ) {
                                Text("Retake")
                            }
                        } else if (showCamera) {
                            AndroidView(factory = { ctx ->
                                PreviewView(ctx).apply {
                                    this.controller = controller
                                    controller.bindToLifecycle(lifecycleOwner)
                                }
                            }, modifier = Modifier.fillMaxSize())

                            IconButton(
                                onClick = {
                                    val photoFile = File(
                                        context.cacheDir,
                                        "seed_${System.currentTimeMillis()}.jpg"
                                    )

                                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                                    controller.takePicture(
                                        outputOptions,
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageSavedCallback {
                                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                capturedFile = photoFile
                                                showCamera = false
                                            }

                                            override fun onError(exception: ImageCaptureException) {

                                            }
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 20.dp)
                                    .size(70.dp)
                                    .background(Color.White, RoundedCornerShape(50.dp))
                            ) {
                                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { showCamera = true },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = brownText
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column {
                                    Text("Take Photo Of Seeds In Hand", color = brownText, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                    Text("Show Seeds Clearly For Better Visibility", color = brownText, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, modifier = Modifier.padding(start = 20.dp))
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            if (capturedFile == null || selectedCrop.isEmpty() || selectedprice.isEmpty()) {
                                Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                            } else {
                                scope.launch {
                                    isLoading = true

                                    var generatedLat = 0.0
                                    var generatedLon = 0.0

                                    try{
                                        kotlinx.coroutines.withTimeoutOrNull(2000L){
                                            withContext(Dispatchers.IO){
                                                val geocoder = Geocoder(context,Locale.getDefault())

                                                if(location != "Loading.." && location.isNotBlank()){
                                                    val addresses = geocoder.getFromLocationName("$location, India",1)
                                                    if(!addresses.isNullOrEmpty()){
                                                        generatedLat = addresses[0].latitude
                                                        generatedLon = addresses[0].longitude
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    catch(e : Exception){
                                        Log.e("lat&lon",e.message.toString())
                                    }

                                    val imageUrl = repository.uploadToCloudinary(capturedFile!!)

                                    if (imageUrl != null) {
                                        val mappedUnit = when (selectedUnit) {
                                            "Kg" -> WeightUnit.KG
                                            "Man(20 KG)" -> WeightUnit.MAN
                                            "Khaandi(400 KG)" -> WeightUnit.KHAANDI
                                            else -> WeightUnit.KG
                                        }

                                        val mappedPackage = when (selectedPackage) {
                                            "Gunny" -> PackageMethod.Gunny
                                            "Packet" -> PackageMethod.PACKET
                                            "Open" -> PackageMethod.OPEN
                                            else -> PackageMethod.OPEN
                                        }

                                        val mappedStorage = when (selectedStorage) {
                                            "Gunny" -> StorageMethod.Gunny
                                            "Metal Drum" -> StorageMethod.METAL_DRUM
                                            "Open" -> StorageMethod.OPEN
                                            else -> StorageMethod.NONE
                                        }
                                        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                        val currentSeed = user?.seedCount ?: 0
                                        val updatedSeed = currentSeed + 1

                                        val newSeed = Seed(
                                            cropName = selectedCrop,
                                            variety = varietyName,
                                            quantity = quantity.toDoubleOrNull() ?: 0.0,
                                            price = selectedprice.toDoubleOrNull() ?: 0.0,
                                            unit = mappedUnit,
                                            harvestYear = harvestYear.toIntOrNull() ?: 0,
                                            sellerName = userName,
                                            sellerPhone = phoneNumber,
                                            sellerId = currentUserId,
                                            packaging = mappedPackage,
                                            storing = mappedStorage,
                                            soilType = soilType,
                                            imagePath = imageUrl,
                                            lat = generatedLat,
                                            lon = generatedLon,
                                            distance = 0.0,
                                            location = location
                                        )
                                        Log.d("jay","${newSeed.lat} ${newSeed.lon}")

                                        val success = repository.insertSeed(newSeed)


                                        isLoading = false
                                        if(success){
                                            authViewModel.updateUserSeedCount(updatedSeed)
                                            Toast.makeText(context, "Seed Added Successfully!", Toast.LENGTH_SHORT).show()

                                            capturedFile = null
                                            showCamera = false
                                            selectedCrop = ""
                                            selectedprice = ""
                                            quantity = ""
                                            harvestYear = ""
                                            flag = 0
                                            packageFlag = 0
                                            varietyName = ""
                                            storageFlag = 0
                                            selectedPackage = ""
                                            selectedStorage = ""
                                            selectedUnit = "Kg"
                                            isOther = false
                                        }
                                        else {
                                            Toast.makeText(context, "Error: Could not save to database.", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            disabledContainerColor = Color.Gray
                        )
                    ) { Text("LIST SEED", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White) }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50), strokeWidth = 5.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Uploading Seed...", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}