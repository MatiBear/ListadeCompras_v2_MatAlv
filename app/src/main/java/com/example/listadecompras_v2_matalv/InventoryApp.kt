package com.example.listadecompras_v2_matalv

import android.app.Application
import androidx.room.Room
import com.example.listadecompras_v2_matalv.classData.ProductDataDao

class InventoryApp : Application() {

    // The lazy delegate ensures that the database is created only when it's accessed for the first time.
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "your_database_name"
        ).build()
    }

    val productDataDao: ProductDataDao by lazy { database.productDataDao() }
}
