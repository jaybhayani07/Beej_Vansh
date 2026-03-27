package com.example.beej_vansh.presentation.screens

import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.presentation.componants.CardDetail
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.optionColor
import com.example.beej_vansh.utils.AuthViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,seedList: List<Seed>,authViewModel: AuthViewModel){

    val user by authViewModel.userData

    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }



    val location = if(user != null) "${user!!.village} , ${user!!.district}" else "Loading.."

    Box(modifier = Modifier.fillMaxSize().background(background)){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.Center,

                    ){
                        Text(text = "\uD83C\uDF31BeejVansh", fontWeight = FontWeight.ExtraBold, color = Color.White,fontSize = 25.sp
                        )
                        Row(modifier = Modifier.padding(top = 3.dp)){
                            Icon(imageVector = Icons.Default.LocationOn,contentDescription = null, tint = Color.White, modifier = Modifier.size(25.dp).padding(top = 5.dp))
                            Text(text = location, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = optionColor),
            )

            LazyColumn(modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                )
            {
                item{
                    Text("Available Seeds")
                }
                items(items =seedList,
                    key = {it.id + it.distance}

                ){seed->
                    SeedCardUi(seed,navController)
                    Spacer(modifier = Modifier.height(25.dp))
                    Log.d("location",seed.location)
                }
            }
        }
    }
}

@Composable
fun SeedCardUi(seed: Seed,navController: NavController) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = {
                val seedJson = Gson().toJson(seed)
                val encodedSeed = Uri.encode(seedJson)

                navController.navigate("cardDetail/$encodedSeed")

            })
    ) {
        Column {
            Box {
                AsyncImage(
                    model = seed.imagePath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                Log.d("HomeScreen", "seedList size: ${seed.soilType}")
                Surface(
                    color = Color(0xFF2E7D32),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn, null, Modifier.size(14.dp), Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val formattedDistance = String.format("%.1f km away", seed.distance)
                        Text(
                            text = formattedDistance,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${seed.cropName} (${seed.variety.ifEmpty { "Standard" }})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001F3F)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomChip(text = "${seed.packaging.name}/${seed.storing.name}")
                    CustomChip(text = "${seed.harvestYear} Harvest")
                }
                Spacer(Modifier.height(10.dp))
                CustomChip(text = "${seed.soilType} Soil")

                Spacer(modifier = Modifier.height(height = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = seed.sellerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001F3F)
                        )
                        Text(
                            text = "${seed.quantity} ${seed.unit} available",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF795548)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Request Connect",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomChip(text: String) {
    Surface(
        color = Color(0xFFEFE4D6),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}