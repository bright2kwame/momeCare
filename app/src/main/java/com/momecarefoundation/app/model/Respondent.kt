package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import khronos.toString
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
    var dateOfBirth: Date? = null,
    var recentDeliveryDate: Date? = null,
    var languageSpoken: String = "",
    var community: String = "",
    var hasDeliveredRecentChildAtRegisteredHospital: String = "",
    var reasonIfNotDeliveredAtHospital: String = "",
    var hasAttendedAllScheduledChildWeighings: String = "",
    var reasonIfNotAttendedAllWeighings: String = "",
    var numberOfExclusiveBreastFeedingMonths: String = "",
    var reasonIfLessThanSixMonths: String = "",
    var ageOfEldestChild: String = "",
    var receivePhoneCallReminders: Boolean = true,
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

    //delete first item
    fun deleteItem(id: String) {
        Respondent().deleteItem(id = id)
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
    fun all(): List<Respondent> {
        return Respondent().queryAll()
    }


    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("first_name", firstName)
            jsonObject.put("last_name", lastName)
            jsonObject.put("phone", phone)
            jsonObject.put("backed_up", isBackedUp)
            jsonObject.put("date_of_birth", dateOfBirth)
            jsonObject.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
            ""
        }
    }

    fun parseToJson(): JSONObject {
        val postData = JSONObject()
        postData.put("first_name", firstName)
        postData.put("last_name",lastName)
        postData.put("date_of_birth", dateOfBirth?.toString("YYYY-MM-DD"))
        postData.put("recent_delivery_date", recentDeliveryDate?.toString("YYYY-MM-DD"))
        postData.put("phone_number", phone)
        postData.put("language_spoken", languageSpoken)
        postData.put("community", community)
        postData.put(
            "has_delivered_recent_child_at_registered_hospital",
            hasDeliveredRecentChildAtRegisteredHospital
        )
        postData.put(
            "reason_if_not_delivered_at_hospital",
            reasonIfNotDeliveredAtHospital
        )
        postData.put(
            "has_attended_all_scheduled_child_weighings",
            hasAttendedAllScheduledChildWeighings
        )
        postData.put(
            "reason_if_not_attended_all_weighings",
            reasonIfNotAttendedAllWeighings
        )
        postData.put(
            "number_of_exclusive_breastfeeding_months",
            numberOfExclusiveBreastFeedingMonths
        )
        postData.put("reason_if_less_than_six_months", reasonIfLessThanSixMonths)
        postData.put("age_of_eldest_child", ageOfEldestChild)
        postData.put("receive_phone_call_reminders", receivePhoneCallReminders)
        return postData
    }

    override fun toString(): String {
        return toJSON()
    }
}
