package com.example.listadecompras_v2_matalv.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.R
import com.example.listadecompras_v2_matalv.classData.ProductData
import com.example.listadecompras_v2_matalv.dataStorage.InventoryManager

class InventoryAdapter(
    private val context: Context,
    private val inventoryManager: InventoryManager,
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    private var itemList: List<ProductData> = inventoryManager.getInventoryList()
    private val editedItems = mutableSetOf<ProductData>() // New set to track edited items

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageInventoryItem: ImageView = view.findViewById(R.id.imageInventoryItem)
        val textViewInventoryItemName: TextView = view.findViewById(R.id.textViewInventoryItemName)
        val textViewInventoryItemType: TextView = view.findViewById(R.id.textViewInventoryItemType)
        val editTextPricePerUnit: EditText = view.findViewById(R.id.editTextPricePerUnit)
        val textViewTotalPrice: TextView = view.findViewById(R.id.textViewTotalPrice)
        val textViewAmount: TextView = view.findViewById(R.id.textViewAmount)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnDeleteInventoryItem: ImageButton = view.findViewById(R.id.btnDeleteInventoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid", "SetTextI18n", "StringFormatMatches")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val inventoryItem = inventoryManager.GetCurrentInventoryList()[position]

        // Set values for the views
        holder.imageInventoryItem.setImageResource(inventoryItem.imageResId)
        holder.textViewInventoryItemName.text = inventoryItem.name
        holder.textViewInventoryItemType.text = inventoryItem.type
        holder.textViewTotalPrice.text = context.getString(R.string.total_price_add, inventoryItem.totalPrice)
        holder.textViewAmount.text = inventoryItem.amount.toString()
        holder.editTextPricePerUnit.setText(inventoryItem.price.toString())

        // Set the initial amount
        holder.textViewAmount.text = inventoryItem.amount.toString()

        // Minus button click listener
        holder.btnMinus.setOnClickListener {
            onMinusButtonClick(inventoryItem, holder)
        }

        // Plus button click listener
        holder.btnPlus.setOnClickListener {
            onPlusButtonClick(inventoryItem, holder)
        }

        // Delete button click listener
        holder.btnDeleteInventoryItem.setOnClickListener {
            onDeleteButtonClick(inventoryItem, position)
        }

        // TextWatcher for EditText
        holder.editTextPricePerUnit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the price per unit in the underlying data
                val pricePerUnit = s.toString().toDoubleOrNull() ?: 0.0
                if (pricePerUnit >= 0) {
                    inventoryItem.price = pricePerUnit
                    inventoryItem.totalPrice = pricePerUnit * inventoryItem.amount
                    holder.textViewTotalPrice.text = context.getString(R.string.total_price_add, inventoryItem.totalPrice)

                    // Update the underlying data in InventoryManager
                    inventoryManager.updateInventoryItem(inventoryItem)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onMinusButtonClick(item: ProductData, holder: ViewHolder) {
        val newAmount = item.amount - 1
        if (newAmount > 0) {
            item.amount = newAmount
            editedItems.add(item) // Mark the item as edited
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onPlusButtonClick(item: ProductData, holder: ViewHolder) {
        val newAmount = item.amount + 1
        item.amount = newAmount
        editedItems.add(item) // Mark the item as edited
        notifyDataSetChanged()
    }

    private fun updateAmountAndTotalPrice(item: ProductData, holder: ViewHolder) {
        // Update the amount TextView
        holder.textViewAmount.text = item.amount.toString()

        // Check if the new amount is 0
        if (item.amount <= 0) {
            // Remove the item from the list
            val position = inventoryManager.currentInventoryList.indexOf(item)

            // Remove the item from the list
            inventoryManager.removeProduct(item)

            // Notify the adapter about the item removal
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        } else {
            // Update the price per unit and total price
            updatePriceAndTotalPrice(item, holder)

            // Update the underlying data in InventoryManager
            inventoryManager.updateInventoryItem(item)
        }
    }


    @SuppressLint("StringFormatInvalid", "StringFormatMatches", "NotifyDataSetChanged")
    fun updatePriceAndTotalPrice(item: ProductData, holder: ViewHolder) {
        val pricePerUnit = holder.editTextPricePerUnit.text.toString().toDoubleOrNull() ?: 0.0

        // Check if the entered price per unit is valid (non-negative)
        if (pricePerUnit >= 0) {
            // Update the price per unit in the underlying data
            item.price = pricePerUnit

            item.totalPrice = pricePerUnit * item.amount
            holder.textViewTotalPrice.text = context.getString(R.string.total_price_add, item.totalPrice)

            // Notify the adapter about the data change
            notifyDataSetChanged()
        }
    }

    private fun onDeleteButtonClick(item: ProductData, position: Int) {

        // Remove the item from the list
        inventoryManager.removeInventoryItem(item)

        // Notify the adapter about the item removal
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateInventory(newList: List<ProductData>) {
        editedItems.clear() // Clear the set of edited items
        inventoryManager.clearInventoryList()
        inventoryManager.updateInventoryList(newList)
        notifyDataSetChanged()
    }

    fun updateList(newList: List<ProductData>) {
        inventoryManager.clearInventoryList()
        inventoryManager.updateInventoryList(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return inventoryManager.GetCurrentInventoryList().size
    }
}