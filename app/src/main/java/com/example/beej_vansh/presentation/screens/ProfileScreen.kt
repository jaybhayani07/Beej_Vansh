package com.example.beej_vansh.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beej_vansh.utils.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel
) {
    val background = Color(0xFFF9F6F0)
    val optionColor = Color(0xFF2E7D32)
    val brownText = Color(0xFF795548)
    val cardBackground = Color.White
    val goldColor = Color(0xFFFFC107)

    val user by authViewModel.userData

    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    val userName = if(user != null) "${user!!.firstName} ${user!!.lastName}" else "Loading.."
    val location = if(user != null) "${user!!.village} , ${user!!.district}" else "Loading.."
    val phoneNumber = if(user != null) user!!.phoneNumber else "Loading.."
    val soilType = if(user != null) user!!.soilType else "Loading.."
    val seedCount = if(user != null) user!!.seedCount else "Loading.."

    Log.d("Profile",userName)
    Log.d("Profile",location)
    Log.d("Profile",phoneNumber)
    Log.d("Profile",soilType)



    Box(modifier = Modifier.fillMaxSize().background(background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(optionColor)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 25.sp
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(end = 16.dp).size(28.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(2.dp, Color.White, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(100.dp).align(Alignment.BottomCenter)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = userName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = location,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .offset(y = 9.dp)
                        .clip(RoundedCornerShape(50.dp))

                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Green.copy(alpha = 0.1f), // Transparent at the top
                                    Color.White.copy(alpha = 0.8f)  // Fades to frosted white at the bottom
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+91 $phoneNumber",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = Color.Black.copy(alpha = 0.2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 30.dp).offset(y = 12.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text(
                                text = "Community Trust Score",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "4.5",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 40.sp,
                                    color = optionColor
                                )
                                Text(
                                    text = " / 5",
                                    fontSize = 24.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                repeat(4) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = goldColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.StarHalf,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEFEBE0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Layers,
                                    contentDescription = null,
                                    tint = brownText,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Primary Soil Type",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = soilType,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier.weight(1f).shadow(8.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = cardBackground),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                            ) {
                                Text(
                                    text = "${seedCount}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 32.sp,
                                    color = optionColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Seeds Listed",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Card(
                            modifier = Modifier.weight(1f).shadow(8.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = cardBackground),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                            ) {
                                Text(
                                    text = "8",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 32.sp,
                                    color = brownText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Exchanges",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}