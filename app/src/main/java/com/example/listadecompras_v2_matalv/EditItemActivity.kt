package com.example.listadecompras_v2_matalv

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras_v2_matalv.classData.ProductData

data class IconItem(
    var name: String,
    var iconResId: Int
)

class EditItemActivity : AppCompatActivity() {

    private val icons = listOf(
        IconItem("Box", R.drawable.box),
        IconItem("Lettuce", R.drawable.lettuce),
        IconItem("Potatoes", R.drawable.potatos),
        IconItem("Tomato", R.drawable.tomato),
        // Add more predefined icons as needed
    )

    private val itemTypes = listOf(
        "Vegetable",
        "Fruit",
        // Add more item types as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editing_item_screen)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHome)
        btnHome.visibility = View.GONE

        // Set up the icon spinner
        val spinnerIcon: Spinner = findViewById(R.id.spinnerIcon)
        val iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, icons.map { it.name })
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIcon.adapter = iconAdapter

        // Retrieve the ProductData from the intent
        val productData = intent.getSerializableExtra("productData") as? ProductData

        // Check if it's a new item
        val newItem = intent.getBooleanExtra("newItem", true)

        // Set up the item type spinner
        val itemTypeSpinner: Spinner = findViewById(R.id.spinnerItemType)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemTypeSpinner.adapter = typeAdapter

        // Access UI elements
        val itemNameEditText = findViewById<EditText>(R.id.editTextItemName)
        val pricePerUnitEditText = findViewById<EditText>(R.id.editTextPricePerUnit)
        val amountEditText = findViewById<EditText>(R.id.editTextAmount)
        val imageView = findViewById<ImageView>(R.id.imageViewSelectedIcon)

        // Update UI elements with product data
        if (productData != null) {
            itemNameEditText.setText(productData.name)
            pricePerUnitEditText.setText(productData.price.toString())
            amountEditText.setText(productData.amount.toString())

            // Set selected item for item type spinner
            val typePosition = itemTypes.indexOf(productData.type)
            itemTypeSpinner.setSelection(typePosition)

            // Set selected icon in the imageView
            val selectedIcon = icons.find { it.iconResId == productData.imageResId }
            val selectedIconName = itemTypes.indexOf(productData.imageName)

            if (selectedIcon != null) {
                imageView.setImageResource(selectedIcon.iconResId)

                // Find the position of the selected icon in the icons list
                val iconPosition = icons.indexOf(selectedIcon)

                // Set the selection in the icon spinner
                if (iconPosition != -1) {
                    spinnerIcon.setSelection(iconPosition)
                }
            } else {
                imageView.setImageResource(R.drawable.box)
                spinnerIcon.setSelection(0)
            }

        }

        // Set up the listener for item selection
        spinnerIcon.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                // Get the selected icon based on the position
                val selectedIcon = icons[position]

                // Update the imageView to show the selected icon
                imageView.setImageResource(selectedIcon.iconResId)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here if nothing is selected
            }
        }

        // Adjust toolbar title and confirm button text based on newItem flag
        // Set the title for the Toolbar
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)
        val confirmButton = findViewById<Button>(R.id.btnConfirmEdit)

        if (newItem) {
            toolbarTitle.text = getString(R.string.new_item)
            confirmButton.text = getString(R.string.add_item)
        } else {
            toolbarTitle.text = getString(R.string.edit_item)
            confirmButton.text = getString(R.string.update_item)
        }

        // Set up confirm button click listener
        confirmButton.setOnClickListener {
            // Handle the confirmation logic based on newItem flag
            if (newItem) {
                // Logic for adding a new item
            } else {
                // Logic for updating an existing item
            }
        }
    }

    fun onBackButtonClick(view: View) {
        // Close the app
        finish()
    }
}