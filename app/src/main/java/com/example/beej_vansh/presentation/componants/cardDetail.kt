package com.example.beej_vansh.presentation.componants

import android.content.Intent
import android.net.Uri
import android.text.Layout
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.beej_vansh.data.ViewModel.RequestViewModel
import com.example.beej_vansh.data.model.Request
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.brownText
import com.example.beej_vansh.ui.theme.cardBackground
import com.example.beej_vansh.ui.theme.optionColor
import com.example.beej_vansh.utils.AuthViewModel


@Composable
fun CardDetail(seed : Seed,navController: NavController,authViewModel: AuthViewModel,requestViewModel: RequestViewModel = viewModel()) {
    val context = LocalContext.current
    val user by authViewModel.userData
    var isSending by rememberSaveable{ mutableStateOf(false) }
    var requestStatus by rememberSaveable{ mutableStateOf("NONE") }

    LaunchedEffect(seed.id,user?.uid) {
        if(user != null){
            requestViewModel.checkRequestStatus(seedId = seed.id, buyerId = user!!.uid){
                status ->
                requestStatus = status
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(cardBackground)
        ) {
            AsyncImage(
                model = seed.imagePath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .size(40.dp)
                .background(Color.White, CircleShape)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = brownText)
        }

        Surface(
            modifier = Modifier
                .padding(top = 300.dp, start = 16.dp, end = 16.dp)
                .width(360.dp)
                .clip(shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cardBackground)
            ) {

                Text(
                    text = seed.cropName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Variety: ${seed.variety}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp),
                    color = brownText
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    Modifier.padding(start = 20.dp, end = 20.dp)
                ) {
                    DetailCard(title = "Harvest",value = "${seed.harvestYear}",icon = null,tint = optionColor,modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(20.dp))
                    DetailCard(title = "Quantity",value = "${seed.quantity} ${seed.unit}",icon = null,tint = optionColor,modifier = Modifier.weight(1f))
                }
                Spacer(modifier=Modifier.height(22.dp))
                Row(
                    Modifier.padding(start = 20.dp, end = 20.dp)
                ) {
                    DetailCard(title = "Price/Rate",value = "₹${seed.price}/${seed.unit}",icon = null,tint = optionColor,modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(20.dp))

                Row{
                    Card(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(optionColor)
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding( 25.dp)
                            ){
                                Icon(imageVector = Icons.Default.Inventory2,contentDescription = null, tint = Color.White, modifier = Modifier.size(45.dp))
                                Spacer(Modifier.width(10.dp))
                                Column() {
                                    Text(text = "Storage Method", color = Color.White,)
                                    Text(text = "${seed.storing} ✓", color = Color.White, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
                FeatureRow(icon = Icons.Default.Layers, title = "Soil Type", value = seed.soilType)
                Log.d("soil",seed.soilType)
                Spacer(Modifier.height(15.dp))
                FeatureRow(icon = Icons.Default.AllInbox, title = "Packaging", value = seed.packaging.name)

                HorizontalDivider(modifier = Modifier.padding(20.dp), color = Color.LightGray, thickness = 2.dp)

                val formattedDist = String.format("%.1f km away",seed.distance)

                Text(text = "Listed by :-",color = Color.DarkGray, modifier = Modifier.padding(start = 20.dp,top=5.dp))
                Text(text = seed.sellerName,color = Color.Black, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(start = 30.dp,top = 5.dp))
                Text(text = seed.location,color = Color.DarkGray, modifier = Modifier.padding(start = 30.dp,top=5.dp))
                Text(text = formattedDist,color = Color.Black, modifier = Modifier.padding(start = 30.dp,top=5.dp), fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(20.dp))
                Text(
                    text = if (requestStatus == "ACCEPTED") "${seed.sellerPhone}   <-- Click to Call" else "",
                    color = Color.Blue,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(start = 30.dp, top = 5.dp)
                        .clickable(
                            onClick = {
                                val cleanNumber =
                                    seed.sellerPhone.filter { it.isDigit() || it == '+' || it.isWhitespace() }
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$cleanNumber")
                                }
                                context.startActivity(intent)
                            }
                        ),
                    fontWeight = FontWeight.ExtraBold,

                    )
                Spacer(Modifier.height(100.dp))
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center

    ) {
        Button(
            onClick = {
                if(user != null){
                    if(requestStatus != "ACCEPTED"){
                        isSending = true
                        requestViewModel.sendRequest(
                            seed = seed,
                            user = user!!,
                            onSuccess = {
                                isSending = false
                                requestStatus = "PENDING"
                                Toast.makeText(context, "Request sent to seller!", Toast.LENGTH_LONG).show()
                            },
                            onError = { error ->
                                isSending = false
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }

                }
            },
            enabled = !isSending && requestStatus != "PENDING",
            modifier = Modifier
                .width(250.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,optionColor),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),

        ) {
            Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = optionColor)
            Spacer(modifier = Modifier.width(8.dp))
            val text = when{
                isSending -> "Sending.."
                requestStatus == "PENDING" -> "Request sent ✅"
                requestStatus == "ACCEPTED" -> "ACCEPTED!! ✅"
                else -> "Connect"
            }
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = optionColor)
        }
    }
}

@Composable
fun FeatureRow(icon : ImageVector,title : String,value : String){
    Row(modifier = Modifier.padding(start = 20.dp)){
        Icon(imageVector = icon,contentDescription = null, tint = brownText, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(text = "${title}:",color = Color.DarkGray, fontSize = 17.sp)
        Spacer(Modifier.width(20.dp))
        Text(text = value,color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
    }
}

    @Composable
    fun DetailCard(title : String,value : String,icon : ImageVector?,tint : Color,modifier: Modifier){
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(background)
                .border(1.dp, brownText, RoundedCornerShape(12.dp))
                .padding(16.dp)
                    //Color(0xFFEFEBE0)
        ){

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(icon != null){
                        Icon(imageVector = icon,contentDescription = null, Modifier.size(16.dp),tint = Color.Gray)
                    }
                    Text(text = title, color = Color.DarkGray)
                }
                Spacer(Modifier.height(4.dp))

                Text(text = value.ifEmpty { "N/A" }, color = tint, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

        }
    }