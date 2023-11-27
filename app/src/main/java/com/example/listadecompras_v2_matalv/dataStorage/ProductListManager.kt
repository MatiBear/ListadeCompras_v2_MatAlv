package com.example.listadecompras_v2_matalv.dataStorage

import android.content.Context
import android.widget.Toast
import com.example.listadecompras_v2_matalv.classData.ProductData
import com.example.listadecompras_v2_matalv.R

class ProductListManager(context: Context, private val inventoryManager: InventoryManager) {

    private var baseProductList: List<ProductData> = listOf(
        ProductData(1, context.getString(R.string.lettuce), R.drawable.lettuce,
            context.getString(R.string.vegetables), 0, 0.0, 0.0),
        ProductData(2, context.getString(R.string.tomato), R.drawable.tomato,
            context.getString(R.string.fruits), 0, 0.0, 0.0),
        ProductData(3, context.getString(R.string.potato), R.drawable.potatos,
            context.getString(R.string.vegetables), 0, 0.0, 0.0),
        ProductData(4, context.getString(R.string.coca_cola), R.drawable.drink,
            context.getString(R.string.drinks), 0, 0.0, 0.0),
        ProductData(5, context.getString(R.string.juice), R.drawable.juice,
            context.getString(R.string.drinks), 0, 0.0, 0.0),
        ProductData(6, context.getString(R.string.toothpaste), R.drawable.colgate,
            context.getString(R.string.personal_hygiene), 0, 0.0, 0.0),
        ProductData(7, context.getString(R.string.amoxicillin), R.drawable.amoxilina,
            context.getString(R.string.medicine), 0, 0.0, 0.0),
    )

    private var currentProductList: List<ProductData> = baseProductList.toList()

    fun resetProductList() {
        currentProductList = baseProductList.toList()
    }

    fun orderProductListByName() {
        currentProductList = currentProductList.sortedBy { it.name }
    }

    fun orderProductListByType() {
        currentProductList = currentProductList.sortedBy { it.type }
    }

    fun orderProductListByPrice() {
        currentProductList = currentProductList.sortedBy { it.price }
    }

    fun updateProductAmount(productId: Int, newAmount: Int) {
        val existingProduct = currentProductList.find { it.id == productId }
        existingProduct?.amount = newAmount
    }

    fun getBaseProductList(): List<ProductData> {
        return baseProductList.toList()
    }

    fun getProductById(productId: Int): ProductData? {
        return currentProductList.find { it.id == productId }
    }

    fun getCurrentProductList(): List<ProductData> {
        return currentProductList.toList()
    }

    fun clearProductList() {
        currentProductList = emptyList()
    }

    fun updateProductList(newList: List<ProductData>) {
        currentProductList = newList.toList()
    }

    fun updateInventoryWithIncreasedAmounts(productList: List<ProductData>, context: Context) {
        var itemAddedToInventory = false

        for (product in productList) {
            val existingProduct = inventoryManager.getProductById(product.id)

            if (product.amount > 0) {
                if (existingProduct != null) {
                    // If the product with the same id exists in inventory, update the amount
                    val newAmount = existingProduct.amount + product.amount
                    inventoryManager.updateProductAmount(existingProduct.id, newAmount)
                } else {
                    // If the product doesn't exist in inventory, add the whole item
                    inventoryManager.addProduct(product)
                }
                // Set the flag to true since an item was added
                itemAddedToInventory = true
            }
        }

        // Update the base list in the InventoryManager
        inventoryManager.updateInventoryList(inventoryManager.getInventoryList())

        // Show a toast message if an item was added to the inventory
        if (itemAddedToInventory) {
            Toast.makeText(context,
                context.getString(R.string.products_added_to_inventory), Toast.LENGTH_SHORT).show()
        }
    }
}
