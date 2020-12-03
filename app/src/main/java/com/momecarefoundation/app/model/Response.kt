package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Response(
    @PrimaryKey var id: String = "",
    var code: String = "",
    var respondentNumber: String = "",
    var respondentFirstName: String = "",
    var respondentLastName: String = "",
    var survey: String = "",
    var answer: String = "",
    var locationLat: String = "",
    var locationLon: String = "",
    var locationName: String = "",
    var isBackedUp: Boolean = false
) : RealmObject() {

    // clear all objects from Respondent.class
    fun clearAll() {
        Response().deleteAll()
    }

    // clear all objects saved
    fun clearAllSaved() {
        Response().delete { equalTo("isBackedUp", true) }
    }

    // save a respondent
    fun save(item: Response) {
        item.save()
    }

    // query a single first item
    fun getResponse(id: String): Response? {
        return Response().queryFirst { equalTo("id", id) }
    }

    // query all offline items
    fun allOfflineResponse(): List<Response> {
        return Response().query { equalTo("isBackedUp", false) }
    }

    // update all as updated
    fun updateLocalResponseStatus(all: List<Response>) {
        all.forEach {
            it.isBackedUp = true
            save(it)
        }
    }

    // save all items
    fun saveAll(all: List<Response>) {
        all.saveAll()
    }

    // get all
    fun all() {
        Response().queryAll()
    }

    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("code", code)
            jsonObject.put("respondent_number", respondentNumber)
            jsonObject.put("respondent_first_name", respondentFirstName)
            jsonObject.put("respondent_last_name", respondentLastName)
            jsonObject.put("survey", survey)
            jsonObject.put("answer", answer)
            jsonObject.put("backed_up", isBackedUp)
            jsonObject.put("latitude", locationLat)
            jsonObject.put("longitude", locationLon)
            jsonObject.put("location_name", locationName)
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
