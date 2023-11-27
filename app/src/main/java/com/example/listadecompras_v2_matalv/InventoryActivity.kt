package com.example.listadecompras_v2_matalv

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.adapters.InventoryAdapter
import com.example.listadecompras_v2_matalv.dataStorage.InventoryManager

class InventoryActivity : AppCompatActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_screen)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHomeInventory)
        btnHome.visibility = View.GONE

        // Initialize other views and managers
        recyclerView = findViewById(R.id.recyclerViewInventory)
        inventoryManager = InventoryManager(this)

        // Ensure the inventory list is set to the base list
        inventoryManager.resetInventoryList()

        // Set up RecyclerView and its adapter
        adapter = InventoryAdapter(this, inventoryManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Get reference to the button
        val btnSortInventory: Button = findViewById(R.id.btnSortInventory)

        // Set onClickListener programmatically
        btnSortInventory.setOnClickListener {
            showSortOptionsDialog()
        }

        // Set up search functionality
        val editTextSearch: EditText = findViewById(R.id.editTextSearchInventory)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterProducts(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this example
            }
        })
    }

    private fun filterProducts(query: String) {

        val allProducts = inventoryManager.getInventoryList()

        if (query.isEmpty()) {
            // If the query is empty, show all products
            adapter.updateList(allProducts)
        } else {
            // If the query is not empty, filter and show matching products
            val filteredList = allProducts.filter {
                it.name.contains(query, ignoreCase = true)
            }
            adapter.updateList(filteredList)
        }
    }

    fun onBackButtonClick(view: View) {
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateInventoryClick(view: View) {
        for (i in 0 until adapter.itemCount) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? InventoryAdapter.ViewHolder
            val inventoryItem = inventoryManager.getInventoryList()[i]
            holder?.let {
                val pricePerUnit = it.editTextPricePerUnit.text.toString().toDoubleOrNull() ?: 0.0
                inventoryManager.updateInventoryPrice(inventoryItem.id, pricePerUnit)
            }
        }

        // Notify the adapter about the data change
        adapter.notifyDataSetChanged()

        // Save the current inventory list to the base list
        inventoryManager.saveInventoryList(inventoryManager.currentInventoryList)

        // Show a pop-up message
        Toast.makeText(this, getString(R.string.inventory_updated), Toast.LENGTH_SHORT).show()
    }


    private fun showSortOptionsDialog() {
        val options = arrayOf(
            getString(R.string.sort_by_name),
            getString(R.string.sort_by_type),
            getString(R.string.sort_by_price)
        )

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.sort_options))
            .setSingleChoiceItems(options, -1) { dialog, which ->
                when (which) {
                    0 -> inventoryManager.sortInventoryByName()
                    1 -> inventoryManager.sortInventoryByType()
                    2 -> inventoryManager.sortInventoryByPrice()
                }

                // Instead of updating the entire inventory, notify the adapter about the data change
                adapter.notifyDataSetChanged()

                // Dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }

}
