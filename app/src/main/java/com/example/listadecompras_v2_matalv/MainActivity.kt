package com.example.listadecompras_v2_matalv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the title for the Toolbar
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)
        toolbarTitle.text = getString(R.string.title_main)

        // Find the back button
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setImageResource(R.drawable.exit)

        // Get the reference to the Toolbar
        val btnHome: ImageButton = findViewById(R.id.btnHome)
        btnHome.visibility = View.GONE
    }

    // Handle the Inventory button click
    fun onInventoryButtonClick(view: View) {
        val intent = Intent(this, InventoryActivity::class.java)
        startActivity(intent)
    }

    // Handle the Movement Registry button click
    fun onMovementRegistryButtonClick(view: View) {
        val intent = Intent(this, RegistryActivity::class.java)
        startActivity(intent)
    }

    // Handle the back button click
    fun onBackButtonClick(view: View) {
        // Close the app
        finish()
    }
}