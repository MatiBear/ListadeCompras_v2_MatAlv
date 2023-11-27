// ProductsActivity.kt

package com.example.listadecompras_v2_matalv

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.adapters.ProductListAdapter
import com.example.listadecompras_v2_matalv.dataStorage.InventoryManager
import com.example.listadecompras_v2_matalv.dataStorage.ProductListManager

class ProductsActivity : AppCompatActivity() {

    private lateinit var productListManager: ProductListManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private lateinit var textViewSortingOrder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products_screen)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHomeProducts)
        btnHome.visibility = View.GONE

        // Initialize other views and managers
        recyclerView = findViewById(R.id.recyclerViewProducts)
        val inventoryManager = InventoryManager(this)
        productListManager = ProductListManager(this, inventoryManager)

        // Set up RecyclerView and its adapter
        adapter = ProductListAdapter(this, productListManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        textViewSortingOrder = findViewById(R.id.textViewSortingOrder)

        // Set up sorting button
        val btnSort: Button = findViewById(R.id.btnSort)
        btnSort.setOnClickListener {
            showSortOptionsDialog()
        }

        // Set up search functionality
        val editTextSearch: EditText = findViewById(R.id.editTextSearch)
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

        val allProducts = productListManager.getBaseProductList()

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

    private fun showSortOptionsDialog() {
        val options = arrayOf(getString(R.string.sort_by_name),
            getString(R.string.sort_by_type))

        val show = AlertDialog.Builder(this)
            .setTitle(getString(R.string.sort_options))
            .setSingleChoiceItems(options, -1) { dialog, which ->
                when (which) {
                    0 -> productListManager.orderProductListByName()
                    1 -> productListManager.orderProductListByType()
                }

                // Update the sorting order text
                textViewSortingOrder.text = options[which]

                // Update the RecyclerView adapter
                adapter.updateList(productListManager.getCurrentProductList())

                // Dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }

    fun onBackButtonClick(view: View) {
        val productList = adapter.getCurrentList()
        productListManager.updateInventoryWithIncreasedAmounts(productList, this)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val productList = adapter.getCurrentList()
        productListManager.updateInventoryWithIncreasedAmounts(productList, this)
        super.onBackPressed()
    }
}