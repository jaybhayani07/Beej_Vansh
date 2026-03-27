package com.example.beej_vansh.presentation.componants

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beej_vansh.data.model.Request
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.optionColor
import android.content.Context
import com.example.beej_vansh.ui.theme.brownText

@SuppressLint("UnrememberedMutableState")
@Composable
fun SendRequestCard(request: Request,context: Context){
    Card(
        modifier = Modifier.fillMaxWidth().padding(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(
                    modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Default.Person,contentDescription = null)
                }
                Spacer(Modifier.width(12.dp))
                Column() {
                    Text(text = request.sellerName, fontWeight = FontWeight.ExtraBold,color = Color.Black)
                    Text(text = "You want to connect",color = Color.Gray)
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth().background(background,RoundedCornerShape(10.dp))
            ){
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(text = "Interested in : -", fontWeight = FontWeight.Medium, color = Color.Gray,fontSize = 16.sp)
                    Text(text = request.seedName, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text(text = request.seedQuantity, color = optionColor,modifier = Modifier.padding(start = 10.dp), fontWeight = FontWeight.ExtraBold, fontSize = 19.sp)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column() {
                if(request.status == "ACCEPTED"){
                    Text(text = "Your request is now accepted ,now you can call each other", modifier = Modifier.padding(10.dp),color = brownText)
                    Text(text = "Click Button to call seller!!", modifier = Modifier.padding(10.dp),color = brownText)
                }
                else if(request.status == "PENDING"){
                    Text(text = "Your request is on hold", modifier = Modifier.padding(10.dp),color = brownText)
                    Text(text = "Please wait until seller doesn't accept your request", modifier = Modifier.padding(10.dp),color = brownText)
                }
                else{
                    Text(text = "Your request is declined", modifier = Modifier.padding(10.dp),color = brownText)
                    Text(text = "Please check other seed from mandi", modifier = Modifier.padding(10.dp),color = brownText)
                }
            }

            Spacer(Modifier.height(7.dp))

            Box(
                modifier = Modifier.fillMaxWidth().padding(10.dp).border(shape = RoundedCornerShape(10.dp), color = optionColor, width = 2.dp).clickable(
                    onClick = {
                        val cleanNumber = request.sellerPhone.filter { it.isDigit() || it == '+' || it.isWhitespace()}
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$cleanNumber")
                        }
                        context.startActivity(intent)
                    },
                )
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val text by mutableStateOf(if(request.status == "ACCEPTED") "+91 ${request.sellerPhone}" else "PENDING")
                    Text(text = text, modifier = Modifier.padding(15.dp), color = optionColor,fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
            }
        }
    }
}