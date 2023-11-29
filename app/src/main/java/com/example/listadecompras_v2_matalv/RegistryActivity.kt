package com.example.listadecompras_v2_matalv

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.adapters.RegistryAdapter
import com.example.listadecompras_v2_matalv.classData.RegistryData
import com.example.listadecompras_v2_matalv.dataStorage.RegistryManager

class RegistryActivity : AppCompatActivity() {

    private lateinit var moveRegistryManager: RegistryManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RegistryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movement_registry_screen)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHome)
        btnHome.visibility = View.GONE

        // Initialize other views and managers
        recyclerView = findViewById(R.id.recyclerViewMoveRegistry)
        moveRegistryManager = RegistryManager(this)

        // Set up RecyclerView and its adapter
        adapter = RegistryAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Test: Add an empty item if the list is initially empty
        if (moveRegistryManager.currentMoveRegistryList.isEmpty()) {
            val emptyItem = RegistryData(
                iconResId = R.drawable.potatos, // Use an appropriate empty icon
                itemName = "Empty Item",
                itemType = "None",
                timestamp = "2023-11-23 12:00:00", // Use a valid timestamp
                actionType = "None",
                valueChanged = "None"
            )
            moveRegistryManager.addMoveRegistryItem(emptyItem)
        }
        adapter.updateMoveRegistryList(moveRegistryManager.currentMoveRegistryList)
    }

    fun onBackButtonClick(view: View) {
        finish()
    }

    fun onClearRegistryClick(view: View) {
        moveRegistryManager.clearMoveRegistryList()
        adapter.updateMoveRegistryList(emptyList())
    }
}