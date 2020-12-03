package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Respondent(
    @PrimaryKey var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var profile: String = "",
    var dateOfBirth: Date? = null,
    var locationLat: String = "",
    var locationLon: String = "",
    var locationName: String = "",
    var isBackedUp: Boolean = false,
) : RealmObject() {

    // clear all objects from Respondent.class
    fun clearAll() {
        Respondent().deleteAll()
    }

    // clear all objects saved
    fun clearAllSaved() {
        Respondent().delete { equalTo("isBackedUp", true) }
    }

    // save a respondent
    fun save(item: Respondent) {
        item.save()
    }

    // query a single first item
    fun getItem(id: String): Respondent? {
        return Respondent().queryFirst { equalTo("id", id) }
    }

    // save all items
    fun saveAll(all: List<Respondent>) {
        all.saveAll()
    }

    // query all offline items
    fun allOfflineRespondents(): List<Respondent> {
        return Respondent().query { equalTo("isBackedUp", false) }
    }

    // update all as updated
    fun updateLocalRespondentStatus(all: List<Respondent>) {
        all.forEach {
            it.isBackedUp = true
            save(it)
        }
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
            jsonObject.put("profile", profile)
            jsonObject.put("latitude", locationLat)
            jsonObject.put("longitude", locationLon)
            jsonObject.put("location_name", locationName)
            jsonObject.put("date_of_birth", dateOfBirth)
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
