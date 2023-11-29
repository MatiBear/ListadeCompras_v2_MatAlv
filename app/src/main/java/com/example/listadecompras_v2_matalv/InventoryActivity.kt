package com.example.listadecompras_v2_matalv

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.adapters.InventoryAdapter
import com.example.listadecompras_v2_matalv.classData.ProductData

class InventoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_screen)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHomeInventory)
        btnHome.setImageResource(R.drawable.button_plus)

        // Initialize other views and view models
        recyclerView = findViewById(R.id.recyclerViewInventory)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        // Set up RecyclerView and its adapter
        adapter = InventoryAdapter(this, productViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

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

    }

    fun onNewItemClick(view: View) {
        // Create an Intent to start EditItemActivity
        val intent = Intent(this, EditItemActivity::class.java)

        // Create a ProductData instance with default or initial values for a new item
        val productData = ProductData(
            id = 0, // Room will auto-generate the ID
            name = "",
            imageName = "Box",
            imageResId = R.drawable.box, // Set a default image resource ID
            type = "Other",
            amount = 0,
            price = 0.0,
            totalPrice = 0.0
        )

        // Put the ProductData and newItem values into the intent
        intent.putExtra("productData", productData)
        intent.putExtra("newItem", true)

        // Start the EditItemActivity
        startActivity(intent)
    }

    fun onBackButtonClick(view: View) {
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateInventoryClick(view: View) {
        for (i in 0 until adapter.itemCount) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? InventoryAdapter.ViewHolder
            val productData = adapter.getItemAtPosition(i)
            holder?.let {
                val pricePerUnit = it.editTextPricePerUnit.text.toString().toDoubleOrNull() ?: 0.0
                productData.price = pricePerUnit
                productViewModel.updateProduct(productData)
            }
        }

        // Notify the adapter about the data change
        adapter.notifyDataSetChanged()

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
                    0 -> productViewModel.sortProductsByName()
                    1 -> productViewModel.sortProductsByType()
                    2 -> productViewModel.sortProductsByPrice()
                }

                // Instead of updating the entire inventory, notify the adapter about the data change
                adapter.notifyDataSetChanged()

                // Dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }
}