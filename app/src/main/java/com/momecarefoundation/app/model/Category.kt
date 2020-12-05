package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Category(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var icon: String = "",
    var numOfSurveys: Int = 0,
    var info: String = "",
) : RealmObject() {

    // clear all objects from Category.class
    fun clearAll() {
        Category().deleteAll()
    }

    // clear all objects from Category.class
    fun all(): List<Category> {
       return Category().queryAll()
    }

    // save a user model
    fun save(category: Category) {
        category.save()
    }

    // save all items
    fun saveAll(all: List<Category>) {
        all.saveAll()
    }

    // query a single first item
    fun getCategoryById(id: String): Category? {
        return Category().queryFirst { equalTo("id", id) }
    }


    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("name", name)
            jsonObject.put("icon", icon)
            jsonObject.put("info", info)
            jsonObject.put("numOfSurveys", numOfSurveys)
            jsonObject.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
            ""
        }
    }

    override fun toString(): String {
        return toJSON()
    }
}
