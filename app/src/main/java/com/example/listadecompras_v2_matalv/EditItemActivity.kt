package com.example.listadecompras_v2_matalv

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProvider
import com.example.listadecompras_v2_matalv.classData.ProductData

data class IconItem(
    var name: String,
    var iconResId: Int
)

class EditItemActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel

    private lateinit var icons: List<IconItem>
    private lateinit var itemTypes: List<String>

    private lateinit var itemNameEditText: EditText
    private lateinit var pricePerUnitEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var totalPriceTextView: TextView
    private lateinit var iconAdapter: ArrayAdapter<String>
    private lateinit var typeAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editing_item_screen)

        icons = listOf(
            IconItem(getString(R.string.box), R.drawable.box),
            IconItem(getString(R.string.lettuce), R.drawable.lettuce),
            IconItem(getString(R.string.potato), R.drawable.potatos),
            IconItem(getString(R.string.tomato), R.drawable.tomato),
            IconItem(getString(R.string.juice), R.drawable.juice),
            IconItem(getString(R.string.coca_cola), R.drawable.drink),
            IconItem(getString(R.string.toothpaste), R.drawable.colgate),
            IconItem(getString(R.string.amoxicillin), R.drawable.amoxilina),
            IconItem(getString(R.string.aspirin), R.drawable.aspirina),
            // Add more predefined icons as needed
        )

        itemTypes = listOf(
            getString(R.string.vegetables),
            getString(R.string.fruits),
            getString(R.string.drinks),
            getString(R.string.personal_hygiene),
            getString(R.string.medicine),
            getString(R.string.others),
            // Add more item types as needed
        )

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHome)
        btnHome.visibility = View.GONE

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        // Set up the icon spinner
        val spinnerIcon: Spinner = findViewById(R.id.spinnerIcon)
        iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, icons.map { it.name })
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIcon.adapter = iconAdapter

        // Retrieve the ProductData from the intent
        val productData = intent.getSerializableExtra("productData") as? ProductData

        // Check if it's a new item
        val newItem = intent.getBooleanExtra("newItem", true)

        // Set up the item type spinner
        val itemTypeSpinner: Spinner = findViewById(R.id.spinnerItemType)
        typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemTypeSpinner.adapter = typeAdapter

        // Access UI elements
        itemNameEditText = findViewById(R.id.editTextItemName)
        pricePerUnitEditText = findViewById(R.id.editTextPricePerUnit)
        amountEditText = findViewById(R.id.editTextAmount)
        totalPriceTextView = findViewById(R.id.textTotalPriceValue)

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

        // Set up listeners for price per unit and amount changes
        pricePerUnitEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateTotalPrice()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })

        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateTotalPrice()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })

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
            if (newItem) {
                // Logic for adding a new item

                // Retrieve input values from UI elements
                val itemName = itemNameEditText.text.toString()
                val pricePerUnit = pricePerUnitEditText.text.toString().toDoubleOrNull() ?: 0.0
                val amount = amountEditText.text.toString().toIntOrNull() ?: 0
                val selectedIcon = icons[spinnerIcon.selectedItemPosition]
                val selectedItemType = itemTypes[itemTypeSpinner.selectedItemPosition]

                // Calculate total price and ensure it's not less than 0
                val totalPrice = (pricePerUnit * amount).coerceAtLeast(0.0)

                // Create a new ProductData instance with the total price
                val newProductData = ProductData(
                    0,
                    itemName,
                    selectedIcon.name,
                    selectedIcon.iconResId,
                    selectedItemType,
                    amount.coerceAtLeast(1),
                    pricePerUnit,
                    totalPrice
                )

                // Call the addNewProduct function from the ViewModel
                productViewModel.addNewProduct(newProductData)
            } else {
                // Logic for updating an existing item

                // Update the existing ProductData instance with the edited values
                productData?.let {
                    it.name = itemNameEditText.text.toString()
                    it.price = pricePerUnitEditText.text.toString().toDoubleOrNull() ?: 0.0
                    it.amount = amountEditText.text.toString().toIntOrNull() ?: 0
                    it.imageResId = icons[spinnerIcon.selectedItemPosition].iconResId
                    it.imageName = icons[spinnerIcon.selectedItemPosition].name
                    it.type = itemTypes[itemTypeSpinner.selectedItemPosition]
                    it.totalPrice = totalPriceTextView.text.toString().toDoubleOrNull() ?: 0.0

                    // Call the updateProduct function from the ViewModel
                    productViewModel.updateProduct(it)
                }
            }

            finish()
        }
    }

    // Function to update the total price based on the current values of price per unit and amount
    @SuppressLint("StringFormatInvalid")
    private fun updateTotalPrice() {
        val pricePerUnitString = pricePerUnitEditText.text.toString()
        val amountString = amountEditText.text.toString()

        if (pricePerUnitString.isNotEmpty() && amountString.isNotEmpty()) {
            val pricePerUnit = pricePerUnitString.toDoubleOrNull() ?: 0.0
            val amount = amountString.toIntOrNull() ?: 0

            // Calculate total price and ensure it's not less than 0
            val totalPrice = (pricePerUnit * amount).coerceAtLeast(0.0)

            // Update the total price TextView
            totalPriceTextView.text = totalPrice.toString()
        } else {
            // Handle the case where either pricePerUnit or amount is empty
            // You can display an error message or take appropriate action
        }
    }

    fun onBackButtonClick(view: View) {
        // Close the app
        finish()
    }
}