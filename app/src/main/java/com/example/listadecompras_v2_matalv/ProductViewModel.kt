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
        _allProducts = MutableLiveData<List<ProductData>>().apply {
            postValue(emptyList()) // or initial data if available
        }
        _displayList = MutableLiveData<List<ProductData>>().apply {
            postValue(emptyList()) // or initial data if available
        }

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
    fun addNewProduct(productData: ProductData) {
        CoroutineScope(Dispatchers.IO).launch {
            // Generate a new ID for the new product
            val newId = generateNewId()

            // Set the new ID to the productData
            productData.id = newId

            // Insert the new product into the database
            productDataDao.insertProduct(productData)

            // Fetch data again to update LiveData
            fetchData()

            // Notify observers that the data has changed
            _allProducts.postValue(_allProducts.value)
        }
    }

    private fun generateNewId(): Long {
        // You can implement your own logic to generate a unique ID,
        // such as querying the database for the maximum ID and adding 1.
        // For simplicity, let's assume a timestamp-based ID.
        return System.currentTimeMillis()
    }

    fun updateProduct(productData: ProductData) {
        CoroutineScope(Dispatchers.IO).launch {
            productDataDao.updateProduct(productData)
            // After updating, fetch updated data
            fetchData()
        }
    }

    fun deleteProduct(productData: ProductData) {
        CoroutineScope(Dispatchers.IO).launch {
            productDataDao.deleteProduct(productData)
            // After deleting, fetch updated data
            fetchData()
        }
    }

    fun resetDisplayList() {
        _displayList.value = _allProducts.value
    }

    fun searchProducts(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // Implement search logic and update _displayList accordingly
            val searchResult = _allProducts.value?.filter { it.name.contains(query, ignoreCase = true) }
            _displayList.postValue(searchResult)
        }
    }

    fun sortProductsByName() {
        // Implement sorting logic by name
        val sortedList = _allProducts.value?.sortedBy { it.name }
        // Update the display list with sorted data
        _displayList.postValue(sortedList)
    }

    fun sortProductsByType() {
        // Implement sorting logic by type and update _displayList accordingly
    }

    fun sortProductsByPrice() {
        // Implement sorting logic by price and update _displayList accordingly
    }
}