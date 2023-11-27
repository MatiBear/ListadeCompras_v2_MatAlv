package com.example.listadecompras_v2_matalv.dataStorage

import android.content.Context
import android.content.SharedPreferences
import com.example.listadecompras_v2_matalv.classData.RegistryData
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RegistryManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MoveRegistryData", Context.MODE_PRIVATE)

    private val moveRegistryKey = "moveRegistryList"

    private var moveRegistryList: MutableList<RegistryData> = getMoveRegistryList()

    fun addMoveRegistryItem(item: RegistryData) {
        moveRegistryList.add(item)
        saveMoveRegistryList(moveRegistryList)
    }

    fun removeMoveRegistryItem(item: RegistryData) {
        moveRegistryList.remove(item)
        saveMoveRegistryList(moveRegistryList)
    }

    fun getMoveRegistryList(): MutableList<RegistryData> {
        val json = sharedPreferences.getString(moveRegistryKey, null)
        return if (json != null) {
            parseJsonToMoveRegistryList(json)
        } else {
            mutableListOf()
        }
    }

    private fun saveMoveRegistryList(moveRegistryList: List<RegistryData>) {
        val jsonArray = JSONArray()
        for (item in moveRegistryList) {
            val jsonObject = JSONObject()
            jsonObject.put("iconResId", item.iconResId)
            jsonObject.put("itemName", item.itemName)
            jsonObject.put("itemType", item.itemType)
            jsonObject.put("timestamp", item.timestamp)
            jsonObject.put("actionType", item.actionType)
            jsonObject.put("valueChanged", item.valueChanged)
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString(moveRegistryKey, jsonArray.toString()).apply()
    }

    private fun parseJsonToMoveRegistryList(json: String): MutableList<RegistryData> {
        val registryList = mutableListOf<RegistryData>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val item = RegistryData(
                jsonObject.getInt("iconResId"),
                jsonObject.getString("itemName"),
                jsonObject.getString("itemType"),
                jsonObject.getString("timestamp"),
                jsonObject.getString("actionType"),
                jsonObject.getString("valueChanged")
            )
            registryList.add(item)
        }
        return registryList
    }

    fun clearMoveRegistryList() {
        moveRegistryList.clear()
        saveMoveRegistryList(moveRegistryList)
    }

    fun updateMoveRegistryList(newList: List<RegistryData>) {
        moveRegistryList = newList.toMutableList()
        saveMoveRegistryList(moveRegistryList)
    }

    fun generateTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
