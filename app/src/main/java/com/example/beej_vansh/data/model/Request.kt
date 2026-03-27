package com.example.beej_vansh.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection_requests")
data class Request(
    @PrimaryKey val id : String = "",
    val seedId : String = "",
    val seedName : String = "",
    val sellerName : String = "",
    val seedQuantity : String = "",
    val sellerId : String = "",
    val buyerId : String = "",
    val buyerPhone : String = "",
    val sellerPhone : String = "",
    val buyerName : String = "",
    val status : String = "PENDING",
    val timeStamp : Long = System.currentTimeMillis()
)

