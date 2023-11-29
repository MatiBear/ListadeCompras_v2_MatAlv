// MoveRegistryManager.kt

package com.example.listadecompras_v2_matalv.dataStorage

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import com.example.listadecompras_v2_matalv.classData.RegistryData
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class RegistryManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MoveRegistryData", Context.MODE_PRIVATE)
    private val moveRegistryKey = "moveRegistryList"

    // Displayed list
    var currentMoveRegistryList: List<RegistryData> = getMoveRegistryList()

    fun addMoveRegistryItem(moveRegistryItem: RegistryData) {
        val moveRegistryList = currentMoveRegistryList.toMutableList()
        moveRegistryList.add(moveRegistryItem)
        saveMoveRegistryList(moveRegistryList)
        currentMoveRegistryList = moveRegistryList.toList()

        Log.d("RegistryManager", "Item added: $moveRegistryItem")
    }

    fun getMoveRegistryList(): List<RegistryData> {
        val json = sharedPreferences.getString(moveRegistryKey, null)
        return if (json != null) {
            parseJsonToMoveRegistryList(json)
        } else {
            emptyList()
        }
    }

    fun clearMoveRegistryList() {
        currentMoveRegistryList = emptyList()
        saveMoveRegistryList(currentMoveRegistryList)
    }

    private fun saveMoveRegistryList(moveRegistryList: List<RegistryData>) {
        val jsonArray = JSONArray()
        for (moveRegistryItem in moveRegistryList) {
            val jsonObject = JSONObject()
            jsonObject.put("iconResId", moveRegistryItem.iconResId)
            jsonObject.put("itemName", moveRegistryItem.itemName)
            jsonObject.put("itemType", moveRegistryItem.itemType)
            jsonObject.put("timestamp", moveRegistryItem.timestamp)
            jsonObject.put("actionType", moveRegistryItem.actionType)
            jsonObject.put("valueChanged", moveRegistryItem.valueChanged)
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString(moveRegistryKey, jsonArray.toString()).apply()
    }

    private fun parseJsonToMoveRegistryList(json: String): List<RegistryData> {
        val moveRegistryList = mutableListOf<RegistryData>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val moveRegistryItem = RegistryData(
                jsonObject.getInt("iconResId"),
                jsonObject.getString("itemName"),
                jsonObject.getString("itemType"),
                jsonObject.getString("timestamp"),
                jsonObject.getString("actionType"),
                jsonObject.getString("valueChanged")
            )
            moveRegistryList.add(moveRegistryItem)
        }
        return moveRegistryList
    }

    fun getCurrentTimestamp(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis
        return sdf.format(calendar.time)
    }
}
