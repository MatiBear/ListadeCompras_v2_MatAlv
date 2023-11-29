package com.example.listadecompras_v2_matalv.classData

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class ProductData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var imageName: String,
    var imageResId: Int,
    var type: String,
    var amount: Int,
    var price: Double,
    var totalPrice: Double
) : Serializable

