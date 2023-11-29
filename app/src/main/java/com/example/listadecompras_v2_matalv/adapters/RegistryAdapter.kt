package com.example.listadecompras_v2_matalv.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras_v2_matalv.R
import com.example.listadecompras_v2_matalv.classData.RegistryData
import java.text.SimpleDateFormat
import java.util.*

class RegistryAdapter(private val context: Context) :
    RecyclerView.Adapter<RegistryAdapter.ViewHolder>() {

    private var moveRegistryList: List<RegistryData> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView = view.findViewById(R.id.imageRegistryItem)
        val itemNameTextView: TextView = view.findViewById(R.id.textViewRegistryItemName)
        val itemTypeTextView: TextView = view.findViewById(R.id.textViewRegistryItemType)
        val timestampTextView: TextView = view.findViewById(R.id.textViewRegistryTimestamp)
        val actionTypeTextView: TextView = view.findViewById(R.id.textViewRegistryActionType)
        val valueChangedTextView: TextView = view.findViewById(R.id.textViewRegistryValueChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.registry_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val moveRegistryItem = moveRegistryList[position]

        holder.iconImageView.setImageResource(moveRegistryItem.iconResId)
        holder.itemNameTextView.text = moveRegistryItem.itemName
        holder.itemTypeTextView.text = moveRegistryItem.itemType

        // Format the timestamp
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.parse(moveRegistryItem.timestamp)
        holder.timestampTextView.text =
            formattedDate?.let {
                SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    .format(it)
            }

        holder.actionTypeTextView.text = moveRegistryItem.actionType
        holder.valueChangedTextView.text = moveRegistryItem.valueChanged
    }

    override fun getItemCount(): Int {
        return moveRegistryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMoveRegistryList(newList: List<RegistryData>) {
        Log.d("RegistryAdapter", "New list size: ${newList.size}")
        moveRegistryList = newList
        notifyDataSetChanged()
    }
}
