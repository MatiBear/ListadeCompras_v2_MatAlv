package com.example.listadecompras_v2_matalv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listadecompras_v2_matalv.classData.ProductData
import com.example.listadecompras_v2_matalv.classData.ProductDataDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productDataDao: ProductDataDao // Assuming you have a DAO

    private val _allProducts: MutableLiveData<List<ProductData>> // MutableLiveData for all products
    private val _displayList: MutableLiveData<List<ProductData>> // MutableLiveData for display list

    val allProducts: LiveData<List<ProductData>>
        get() = _allProducts

    val displayList: LiveData<List<ProductData>>
        get() = _displayList

    init {
        // Initialize productDao
        productDataDao = AppDatabase.getDatabase(application).productDataDao()

        // Initialize MutableLiveData objects
        _allProducts = MutableLiveData()
        _displayList = MutableLiveData()

        // Fetch data from productDao and update LiveData
        fetchData()
    }


    fun clearProductList() {
        // Clear the list of products
        _allProducts.value = emptyList()
    }

    fun updateProductList(newList: List<ProductData>) {
        // Update the list of products
        _allProducts.value = newList
    }

    fun getAllProductsList(): List<ProductData> {
        // Access the LiveData and convert it to a regular list
        return allProducts.value.orEmpty()
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Assuming getAllProducts() returns a List<ProductData> from your DAO
            val productList = productDataDao.getAllProducts()

            // Set LiveData values on the main thread
            _allProducts.postValue(productList)
            _displayList.postValue(productList)
        }
    }

    fun addProduct(productData: ProductData) {
        CoroutineScope(Dispatchers.IO).launch {
            // Perform add operation on productDao
            productDataDao.insertProduct(productData)
            // After adding, fetch updated data
            fetchData()
        }
    }

    fun updateProduct(productData: ProductData) {
        // Perform update operation on productDao
        // You need to implement this method in your ProductDao
    }

    fun deleteProduct(productData: ProductData) {
        // Perform delete operation on productDao
        // You need to implement this method in your ProductDao
    }

    fun resetDisplayList() {
        _displayList.value = _allProducts.value
    }

    fun searchProducts(query: String) {
        // Implement search logic and update _displayList accordingly
    }

    fun sortProductsByName() {
        CoroutineScope(Dispatchers.IO).launch {
            // Implement sorting logic by name
            val sortedList = _allProducts.value?.sortedBy { it.name }
            // Update the display list with sorted data
            _displayList.postValue(sortedList)
        }
    }


    fun sortProductsByType() {
        // Implement sorting logic by type and update _displayList accordingly
    }

    fun sortProductsByPrice() {
        // Implement sorting logic by price and update _displayList accordingly
    }
}