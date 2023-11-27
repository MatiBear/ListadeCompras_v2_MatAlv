package com.example.listadecompras_v2_matalv.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.classData.ProductData
import com.example.listadecompras_v2_matalv.dataStorage.ProductListManager
import com.example.listadecompras_v2_matalv.R

class ProductListAdapter(
    private val context: Context,
    private val productListManager: ProductListManager,
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProduct: ImageView = view.findViewById(R.id.imageProduct)
        val textViewProductName: TextView = view.findViewById(R.id.textViewProductName)
        val textViewBrand: TextView = view.findViewById(R.id.textViewType)
        val textViewAmount: TextView = view.findViewById(R.id.textViewAmount)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton  = view.findViewById(R.id.btnPlus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.products_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = productListManager.getCurrentProductList()[position]

        holder.imageProduct.setImageResource(product.imageResId)
        holder.textViewProductName.text = product.name
        holder.textViewBrand.text = product.type
        holder.textViewAmount.text = product.amount.toString()

        holder.btnMinus.setOnClickListener {
            onMinusButtonClick(product)
            notifyDataSetChanged() // Notify adapter to update the view
        }

        holder.btnPlus.setOnClickListener {
            onPlusButtonClick(product)
            notifyDataSetChanged() // Notify adapter to update the view
        }
    }

    override fun getItemCount(): Int {
        return productListManager.getCurrentProductList().size
    }

    private fun onMinusButtonClick(product: ProductData) {
        val newAmount = product.amount - 1
        if (newAmount >= 0) {
            product.amount = newAmount
            productListManager.updateProductAmount(product.id, newAmount)
        }
    }

    private fun onPlusButtonClick(product: ProductData) {
        val newAmount = product.amount + 1
        product.amount = newAmount
        productListManager.updateProductAmount(product.id, newAmount)
    }

    // Function to update the list in the adapter
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ProductData>) {
        productListManager.clearProductList()
        productListManager.updateProductList(newList)
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<ProductData> {
        return productListManager.getCurrentProductList()
    }
}
