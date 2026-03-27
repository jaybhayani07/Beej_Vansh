package com.example.beej_vansh.data.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beej_vansh.data.model.Request
import com.example.beej_vansh.data.model.Seed
import com.example.beej_vansh.data.model.User
import com.example.beej_vansh.domain.repository.RequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class RequestViewModel(private val repository: RequestRepository) : ViewModel() {
    val _receivedRequest = MutableStateFlow<List<Request>>(emptyList())
    val receivedRequest = _receivedRequest.asStateFlow()

    val _getSendRequest = MutableStateFlow<List<Request>>(emptyList())
    val getSendRequest = _getSendRequest.asStateFlow()
    fun checkRequestStatus(seedId: String, buyerId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val status = repository.checkRequestStatus(seedId, buyerId)
            onResult(status)
        }
    }
    fun sendRequest(seed : Seed, user : User,onSuccess : () -> Unit,onError : (String) -> Unit){
        viewModelScope.launch {
            Log.d("BeejVansh", "Seller is listening for ID from view: ${seed.sellerId}")
            val newRequest = Request(
                id = UUID.randomUUID().toString(),
                seedId = seed.id,
                seedName = "${seed.cropName} (${seed.variety})",
                seedQuantity = "${seed.quantity} ${seed.unit}",
                sellerId = seed.sellerId,
                buyerId = user.uid,
                buyerName = "${user.firstName} ${user.lastName}",
                buyerPhone = user.phoneNumber,
                sellerPhone = seed.sellerPhone,
                status = "PENDING",
                timeStamp = System.currentTimeMillis(),
                sellerName = seed.sellerName
            )

            val result = repository.createRequest(newRequest)

            if(result) onSuccess() else onError("Failed to send request")
        }
    }

    fun startListening(myId : String){
        viewModelScope.launch {
            repository.getReceivedRequest(myId).collect {
                _receivedRequest.value = it
            }
        }
    }

    fun startListeningSendRequest(myId: String){
        viewModelScope.launch {
            repository.getSendRequest(myId).collect {
                _getSendRequest.value = it
            }
        }
    }

    fun approveRequest(requestId : String){
        viewModelScope.launch {
            repository.updateRequestStatus(requestId,"ACCEPTED")
        }
    }

    fun declineRequest(requestId : String){
        viewModelScope.launch {
            repository.updateRequestStatus(requestId,"DECLINED")
        }
    }


}