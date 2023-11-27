package com.example.listadecompras_v2_matalv.classData

data class ProductData(
    val id: Int,
    var name: String,
    var imageResId: Int, // Resource ID of the image (e.g., R.drawable.lettuce)
    var type: String,
    var amount: Int,
    var price: Double,
    var totalPrice: Double
)