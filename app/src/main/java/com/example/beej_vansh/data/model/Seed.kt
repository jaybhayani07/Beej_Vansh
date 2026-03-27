package com.example.beej_vansh.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "seeds")
data class Seed(
    @PrimaryKey val id : String = System.currentTimeMillis().toString(),
    val cropName : String = "",
    val variety : String = "",
    val quantity : Double = 0.0,
    val price : Double = 0.0,
    val unit : WeightUnit = WeightUnit.KG,
    val soilType : String = "",
    val harvestYear : Int = 0,
    val sellerId : String = "",
    val sellerName : String = "",
    val sellerPhone : String = "",
    val packaging : PackageMethod = PackageMethod.OPEN,
    val storing : StorageMethod = StorageMethod.NONE,
    val imagePath : String = "",
    val lat : Double = 0.0,
    val lon : Double = 0.0,

    @get:Exclude
    var distance : Double  = 0.0,

    val location : String = ""
)
{
    fun getPricePerKg() : Double {
        return price / unit.conversionFactorToKg
    }
}

enum class WeightUnit(val label : String,val conversionFactorToKg : Double){
    KG("Kilogram", 1.0),
    MAN("Man (20kg)", 20.0),
    KHAANDI("Khaandi (400kg)", 400.0)
}

enum class PackageMethod {
    Gunny,
    PACKET,
    OPEN
}

enum class StorageMethod {
    NONE,
    OPEN,
    Gunny,
    METAL_DRUM,

}