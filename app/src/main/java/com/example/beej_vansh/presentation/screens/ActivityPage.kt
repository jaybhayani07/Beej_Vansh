package com.example.beej_vansh.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocal
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beej_vansh.data.ViewModel.RequestViewModel
import com.example.beej_vansh.presentation.componants.ReceivedRequestCard
import com.example.beej_vansh.presentation.componants.SendRequestCard
import com.example.beej_vansh.ui.theme.background
import com.example.beej_vansh.ui.theme.brownText
import com.example.beej_vansh.ui.theme.cardBackground
import com.example.beej_vansh.ui.theme.optionColor
import com.example.beej_vansh.utils.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ActivityPage(requestViewModel: RequestViewModel,authViewModel: AuthViewModel){

    val allRequest by requestViewModel.receivedRequest.collectAsState()
    val user by authViewModel.userData

    val sendRequest by requestViewModel.getSendRequest.collectAsState()
    var context = LocalContext.current

    LaunchedEffect(user?.uid) {
        user?.uid?.let {myId->
            Log.d("BeejVansh", "Seller is listening for ID: $myId")
            requestViewModel.startListening(myId)
        }
    }

    LaunchedEffect(user?.uid) {
        user?.uid?.let {myId->
            requestViewModel.startListeningSendRequest(myId)
        }
    }

    var state = remember{ mutableStateOf("Received") }

    Box(
        modifier = Modifier.fillMaxSize().background(background)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TopAppBar(
                title = {
                    Column() {
                        Text(text = "Activity",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp
                        )
                        Text(text = "Connection Request",color = Color.White, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = brownText
                )
            )

            Spacer(Modifier.height(15.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp).shadow(elevation = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.background(cardBackground,RoundedCornerShape(18.dp)).padding(10.dp)
                ){
                    Button(
                        onClick = {
                            state.value = "Received"
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(state.value == "Received") optionColor else cardBackground
                        )
                    ) {
                        Text(text = "Received", fontWeight = FontWeight.ExtraBold,
                            color = if(state.value == "Received") Color.White else brownText ,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = {
                            state.value = "Send"
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(state.value == "Send") optionColor else cardBackground
                        )
                    ) {
                        Text(text = "Send", fontWeight = FontWeight.ExtraBold,
                            color = if(state.value == "Send") Color.White else brownText ,
                            fontSize = 18.sp
                        )
                    }
                }
            }


            Spacer(Modifier.height(20.dp))

            if(state.value == "Received"){
                if (allRequest.isEmpty()) {
                    Text("No connection requests yet.", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn {
                        item {
                            Text(text = "Your all received requests :", modifier = Modifier.padding(start = 15.dp), color = brownText,)
                        }
                        items(
                            items = allRequest,
                            key = { it.id }
                        ) { request ->
                            ReceivedRequestCard(
                                request = request,
                                context = context,
                                onApprove = { requestViewModel.approveRequest(request.id) },
                                onDecline = { requestViewModel.declineRequest(request.id) }
                            )
                        }
                    }
                }
            }
            else{
                if (sendRequest.isEmpty()) {
                    Text("No connection requests yet.", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn {
                        item {
                            Text(text = "Your all send requests :", modifier = Modifier.padding(start = 15.dp), color = brownText,)
                        }
                        items(
                            items = sendRequest,
                            key = { it.id }
                        ) { request ->
                            SendRequestCard(request,context)
                        }
                    }
                }
            }

        }
    }
}