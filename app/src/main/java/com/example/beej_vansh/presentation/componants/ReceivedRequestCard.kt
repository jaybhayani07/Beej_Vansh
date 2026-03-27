package com.example.beej_vansh.presentation.componants

import android.content.Context
import android.content.Intent
import android.graphics.fonts.FontStyle
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beej_vansh.data.model.Request
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.brownText
import com.example.beej_vansh.ui.theme.cardBackground
import com.example.beej_vansh.ui.theme.optionColor

@Composable
fun ReceivedRequestCard(
    request: Request,
    context: Context,
    onApprove : () -> Unit,
    onDecline : () -> Unit
){
    val isApproved = request.status == "ACCEPTED"
    val isDeclined = request.status == "DECLINED"
    val isPending = request.status == "PENDING"

    Card(
        modifier = Modifier.fillMaxWidth().padding(13.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row (verticalAlignment = Alignment.CenterVertically) {

                Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.LightGray), contentAlignment = Alignment.Center){
                    Icon(imageVector = Icons.Default.Person,contentDescription = null)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(request.buyerName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("wants to connect", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background, RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Text("Interested in:", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp,start = 10.dp))
                Text(request.seedName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.padding(top = 12.dp,start = 15.dp))
                Text(request.seedQuantity, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 12.dp,start = 15.dp))
                if(isApproved){
                    Row{
                        Text(text = "Phone Number:- ", fontWeight = FontWeight.ExtraBold, color = Color.Black, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp,start = 10.dp))
                        Text(text = request.buyerPhone, fontWeight = FontWeight.ExtraBold,color = Color.Blue, fontSize = 16.sp,fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            modifier = Modifier.padding(top = 12.dp).clickable(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        val cleanNumber = request.buyerPhone.filter { it.isDigit() || it == '+' }
                                        data = Uri.parse("tel:$cleanNumber")
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(text = "Click on mobile number to call buyer", modifier = Modifier.padding(start = 15.dp), color = brownText)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when{
                isApproved->{
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.weight(1f).background(optionColor).height(65.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "✅ Approved!", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Text(text = "You can now call each other")
                        }

                    }
                }
                isPending->{
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                onApprove()
                            },
                            modifier = Modifier.weight(1f).height(60.dp).shadow(elevation = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Text(" Approve", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onDecline()
                            },
                            modifier = Modifier.weight(1f).height(60.dp).shadow(elevation = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Text(" Decline", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        }
                    }
                }
            }

        }
    }

}
