package com.example.listadecompras_v2_matalv.classData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.listadecompras_v2_matalv.classData.ProductData

@Dao
interface ProductDataDao {

    @Insert
    suspend fun insertProduct(productData: ProductData)

    @Update
    suspend fun updateProduct(productData: ProductData)

    @Delete
    suspend fun deleteProduct(productData: ProductData)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductData>
}