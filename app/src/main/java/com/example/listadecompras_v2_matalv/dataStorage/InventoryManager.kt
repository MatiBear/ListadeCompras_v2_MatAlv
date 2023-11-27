package com.example.listadecompras_v2_matalv.dataStorage

import android.content.Context
import android.content.SharedPreferences
import com.example.listadecompras_v2_matalv.classData.ProductData
import org.json.JSONArray
import org.json.JSONObject

class InventoryManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("InventoryData", Context.MODE_PRIVATE)

    private val inventoryKey = "inventoryList"

    var currentInventoryList: List<ProductData> = getInventoryList()

    fun addProduct(product: ProductData) {
        val inventoryList = getInventoryList().toMutableList()
        inventoryList.add(product)
        saveInventoryList(inventoryList)
    }

    fun removeProduct(product: ProductData) {
        val inventoryList = getInventoryList().toMutableList()
        inventoryList.remove(product)
        saveInventoryList(inventoryList)
    }

    fun removeInventoryItem(product: ProductData) {
        val inventoryList = currentInventoryList.toMutableList()
        inventoryList.remove(product)
        updateInventoryList(inventoryList)
    }

    fun GetCurrentInventoryList(): List<ProductData> {
        return currentInventoryList.toList()
    }

    fun updateInventoryItem(product: ProductData) {
        val existingProduct = currentInventoryList.find { it.id == product.id }
        existingProduct?.let {
            currentInventoryList = currentInventoryList.map { p ->
                if (p.id == product.id) product else p
            }
        }
    }

    fun updateInventoryPrice(productId: Int, newPrice: Double) {
        val existingProduct = currentInventoryList.find { it.id == productId }
        existingProduct?.let {
            currentInventoryList = currentInventoryList.map { p ->
                if (p.id == productId) {
                    p.copy(price = newPrice, totalPrice = newPrice * p.amount)
                } else p
            }
        }
    }

    fun updateProduct(product: ProductData) {
        val inventoryList = getInventoryList().toMutableList()
        val existingProduct = inventoryList.find { it.id == product.id }
        existingProduct?.let {
            it.name = product.name
            it.imageResId = product.imageResId
            it.type = product.type
            it.amount = product.amount
            it.price = product.price
            it.totalPrice = product.totalPrice
        }
        saveInventoryList(inventoryList)
    }

    fun getInventoryList(): List<ProductData> {
        val json = sharedPreferences.getString(inventoryKey, null)
        return if (json != null) {
            parseJsonToProductList(json)
        } else {
            emptyList()
        }
    }


    fun saveInventoryList(inventoryList: List<ProductData>) {
        val jsonArray = JSONArray()
        for (product in inventoryList) {
            val jsonObject = JSONObject()
            jsonObject.put("id", product.id)
            jsonObject.put("name", product.name)
            jsonObject.put("imageResId", product.imageResId)
            jsonObject.put("type", product.type)
            jsonObject.put("amount", product.amount)
            jsonObject.put("price", product.price)
            jsonObject.put("totalPrice", product.totalPrice)
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString(inventoryKey, jsonArray.toString()).apply()
    }

    private fun parseJsonToProductList(json: String): List<ProductData> {
        val inventoryList = mutableListOf<ProductData>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val product = ProductData(
                jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getInt("imageResId"),
                jsonObject.getString("type"),
                jsonObject.getInt("amount"),
                jsonObject.getDouble("price"),
                jsonObject.getDouble("totalPrice")
            )
            inventoryList.add(product)
        }
        return inventoryList
    }

    fun getProductById(productId: Int): ProductData? {
        val inventoryList = getInventoryList()
        return inventoryList.find { it.id == productId }
    }

    fun updateProductAmount(productId: Int, newAmount: Int) {
        val inventoryList = getInventoryList().toMutableList()
        val existingProduct = inventoryList.find { it.id == productId }

        existingProduct?.let {
            it.amount = newAmount
        }

        saveInventoryList(inventoryList)
    }

    fun clearInventoryList() {
        currentInventoryList = emptyList()
    }

    fun updateInventoryList(newList: List<ProductData>) {
        currentInventoryList = newList.toList()
    }

    fun resetInventoryList() {
        currentInventoryList = getInventoryList().toList()
    }

    fun sortInventoryByName() {
        currentInventoryList = currentInventoryList.sortedBy { it.name }
    }

    fun sortInventoryByType() {
        currentInventoryList = currentInventoryList.sortedBy { it.type }
    }

    fun sortInventoryByPrice() {
        currentInventoryList = currentInventoryList.sortedBy { it.price }
    }

}