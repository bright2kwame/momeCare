package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Survey(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var info: String = "",
    var numberOfResponse: Int = 0,
    var numberOfQuestions: Int = 0,
    var icon: String = "",
    var questions: String = "",
) : RealmObject() {

    // clear all objects from UserModel.class
    fun clearAll() {
        Survey().deleteAll()
    }

    // save a user model
    fun save(item: Survey) {
        item.save()
    }

    // query a single first item
    fun getSurvey(id: String): Survey? {
        return Survey().queryFirst { equalTo("id", id) }
    }

    // save all items
    fun saveAll(all: List<Survey>) {
        all.saveAll()
    }

    // get all
    fun all(): List<Survey> {
        return Survey().queryAll()
    }

    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("name", name)
            jsonObject.put("info", info)
            jsonObject.put("numberOfResponse", numberOfResponse)
            jsonObject.put("numberOfResponse", numberOfQuestions)
            jsonObject.put("questions", questions)
            jsonObject.put("icon", icon)
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
