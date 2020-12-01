package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Respondent(
    @PrimaryKey var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var profile: String = "",
    var isBackedUp: Boolean = false,
) : RealmObject() {

    // clear all objects from Respondent.class
    fun clearAll() {
        Respondent().deleteAll()
    }

    // save a respondent
    fun save(item: Respondent) {
        item.save()
    }

    // query a single first item
    fun getUser(): Respondent? {
        return Respondent().queryFirst()
    }

    // save all items
    fun saveAll(all: List<Respondent>) {
        all.saveAll()
    }

    // get all
    fun all() {
        Respondent().queryAll()
    }


    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("first_name", firstName)
            jsonObject.put("last_name", lastName)
            jsonObject.put("phone", phone)
            jsonObject.put("backed_up", isBackedUp)
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
