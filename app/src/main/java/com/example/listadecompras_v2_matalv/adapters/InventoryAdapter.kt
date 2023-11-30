package com.example.listadecompras_v2_matalv.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.EditItemActivity
import com.example.listadecompras_v2_matalv.R
import com.example.listadecompras_v2_matalv.classData.ProductData
import com.example.listadecompras_v2_matalv.ProductViewModel

@SuppressLint("NotifyDataSetChanged")
class InventoryAdapter(
    private val context: Context,
    private val productViewModel: ProductViewModel,
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    private var productList: List<ProductData> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageInventoryItem: ImageView = view.findViewById(R.id.imageInventoryItem)
        val textViewInventoryItemName: TextView = view.findViewById(R.id.textViewInventoryItemName)
        val textViewInventoryItemType: TextView = view.findViewById(R.id.textViewInventoryItemType)
        val textPricePerUnit: TextView = view.findViewById(R.id.textViewPricePerUnit)
        val textViewTotalPrice: TextView = view.findViewById(R.id.textViewTotalPrice)
        val textViewAmount: TextView = view.findViewById(R.id.textViewAmount)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDeleteInventoryItem: ImageButton = view.findViewById(R.id.btnDeleteInventoryItem)
    }

    init {
        // Observe changes in the product list
        productViewModel.allProducts.observeForever {
            it?.let {
                productList = ArrayList(it) // Create a new list to avoid reference issues
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid", "SetTextI18n", "StringFormatMatches")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inventoryItem = productList[position]

        // Set values for the views
        holder.imageInventoryItem.setImageResource(inventoryItem.imageResId)
        holder.textViewInventoryItemName.text = inventoryItem.name
        holder.textViewInventoryItemType.text = inventoryItem.type
        holder.textViewTotalPrice.text = context.getString(R.string.total_price_add, inventoryItem.totalPrice)
        holder.textViewAmount.text = context.getString(R.string.amount_add, inventoryItem.amount)
        holder.textPricePerUnit.text = context.getString(R.string.price_per_unit_add, inventoryItem.price)

        // Edit button click listener
        holder.btnEdit.setOnClickListener {
            onEditButtonClick(inventoryItem)
        }

        // Delete button click listener
        holder.btnDeleteInventoryItem.setOnClickListener {
            onDeleteButtonClick(inventoryItem, position)
        }
    }

    fun getItemAtPosition(position: Int): ProductData {
        return productList[position]
    }

    private fun onDeleteButtonClick(item: ProductData, position: Int) {
        // Remove the item from the list
        productViewModel.deleteProduct(item)

        // Notify the adapter about the item removal
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ProductData>) {
        productList = newList
        notifyDataSetChanged()
    }

    private fun onEditButtonClick(item: ProductData) {
        // Create an Intent to start EditItemActivity
        val intent = Intent(context, EditItemActivity::class.java)

        // Put the ProductData and newItem values into the intent
        intent.putExtra("productData", item)
        intent.putExtra("newItem", false) // Indicate that it's not a new item

        // Start the EditItemActivity
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}