package com.momecarefoundation.app.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.momecarefoundation.app.BuildConfig
import com.momecarefoundation.app.callback.*
import com.momecarefoundation.app.model.*
import khronos.toString
import org.json.JSONObject
import java.io.File


class APICall(private var context: Context) {
    private var genericError = "Failed to reach server, try again later."
    private val requestQueue = Volley.newRequestQueue(context.applicationContext)
    private var user = User().getUser()
    private var safeRetrieveJsonData = SafeRetrieveJsonData()

    private fun customHeader(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        user?.let {
            headers["Authorization"] = "Token ${it.token}"
        }
        return headers
    }

    companion object {
        const val tag = "APICall"
        val baseUrl =
            if (BuildConfig.DEBUG == true) "https://momecare.herokuapp.com/api/v2.0/data_collection/" else ""
    }


    fun backUpOfflineOrder() {
        val allResponse = Response().allOfflineResponse()
        val allRespondents = Respondent().allOfflineRespondents()

        //MARK: respondent
        val respondentData = JSONObject()
        val respondentDataContent = ArrayList<String>()
        allRespondents.forEach {
            val formattedEach = formatRespondent(it)
            respondentDataContent.add(formattedEach.toString())
        }
        respondentData.put(
            "data",
            respondentDataContent.toString().replace("[", "").replace("]", "")
        )
        val url = baseUrl.plus("respondents/bulk_upload_respondent/")

        val respondentJsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, url, respondentData,
                { response ->
                    Log.e(tag, response.toString())
                    val responseCode = response.getString("response_code")
                    if (responseCode == "100") {
                        Respondent().clearAll()
                    }
                },
                { error ->

                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    return customHeader()
                }
            }

        //MARK: response
        val responseData = JSONObject()
        val responseDataContent = ArrayList<String>()
        allResponse.forEach {
            val formattedEach = formatResponse(it)
            responseDataContent.add(formattedEach.toString())
        }
        responseData.put(
            "data",
            responseDataContent.toString().replace("[", "").replace("]", "")
        )
        val urlResponse = baseUrl.plus("response/bulk_upload_response/")

        val responseJsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, urlResponse, responseData,
                { response ->
                    Log.e(tag, response.toString())
                    val responseCode = safeRetrieveJsonData.getStringJSONData(response,"response_code")
                    if (responseCode == "100") {
                        Response().clearAll()
                    }
                },
                { error ->

                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    return customHeader()
                }
            }

        if (allRespondents.isNotEmpty()) {
            requestQueue.add(respondentJsonObjectRequest)
        }

        if (allResponse.isNotEmpty()) {
            requestQueue.add(responseJsonObjectRequest)
        }
    }

    //MARK: login session
    fun login(number: String, password: String, callback: UserCallback) {
        val data = JSONObject()
        data.put("phone_number", number)
        data.put("password", password)
        val url = baseUrl.plus("users/login/")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val user = parseUser(response.getJSONObject("results"))
                    User().save(user)
                    callback.onReceivedItem(user)
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedDetail(detail)
                }
            },
            { error ->
                Log.e(tag, error.toString())
                callback.onRequestEnded()
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: reset session
    fun initPasswordReset(number: String, callback: BaseInterface) {
        val data = JSONObject()
        data.put("phone_number", number)
        val url = baseUrl.plus("users/forgot_password/")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                callback.onRequestEnded()
                Log.e(tag, response.toString())
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val message = response.getString("detail")
                    callback.onReceivedDetail(message)
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: reset session
    fun passwordReset(code: String, password: String, callback: BaseInterface) {
        val data = JSONObject()
        data.put("unique_code", code)
        data.put("new_password", password)

        val url = baseUrl.plus("users/reset_password/")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val message = response.getString("detail")
                    callback.onReceivedDetail(message)
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)

    }

    //MARK: all survey groups
    fun surveyGroups(url: String, callback: SurveyGroupCallback) {
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val data = ArrayList<Category>()
                    val results = response.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val item = results.getJSONObject(i)
                        data.add(parseSurveyGroup(item))
                    }
                    callback.onReceivedItems(data)
                    callback.onReceivedNextUrl(
                        safeRetrieveJsonData.getStringJSONData(
                            response,
                            "next"
                        )
                    )
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: all surveys
    fun surveys(url: String, categoryId: String? = null, callback: SurveyCallback) {
        val postData = JSONObject()
        categoryId?.let {
            postData.put("category_id", it)
        }
//        data.put("start_date", "")
//        data.put("end_date", "")
//        data.put("institution_id", "")
//        data.put("search_text", "")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, postData,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val data = ArrayList<Survey>()
                    val results = response.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val item = results.getJSONObject(i)
                        data.add(parseSurvey(item))
                    }
                    callback.onReceivedItems(data)
                    callback.onReceivedNextUrl(
                        safeRetrieveJsonData.getStringJSONData(
                            response,
                            "next"
                        )
                    )
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun formatResponse(response: Response): JSONObject {
        val postData = JSONObject()
        postData.put("first_name", response.answer)
        return postData
    }

    private fun formatRespondent(respondent: Respondent): JSONObject {
        val postData = JSONObject()
        postData.put("first_name", respondent.firstName)
        postData.put("last_name", respondent.lastName)
        postData.put("date_of_birth", respondent.dateOfBirth?.toString("YYYY-MM-DD"))
        postData.put("recent_delivery_date", respondent.recentDeliveryDate?.toString("YYYY-MM-DD"))
        postData.put("phone_number", respondent.phone)
        postData.put("language_spoken", respondent.languageSpoken)
        postData.put("community", respondent.community)
        postData.put(
            "has_delivered_recent_child_at_registered_hospital",
            respondent.hasDeliveredRecentChildAtRegisteredHospital
        )
        postData.put(
            "reason_if_not_delivered_at_hospital",
            respondent.reasonIfNotDeliveredAtHospital
        )
        postData.put(
            "has_attended_all_scheduled_child_weighings",
            respondent.hasAttendedAllScheduledChildWeighings
        )
        postData.put(
            "reason_if_not_attended_all_weighings",
            respondent.reasonIfNotAttendedAllWeighings
        )
        postData.put(
            "number_of_exclusive_breastfeeding_months",
            respondent.numberOfExclusiveBreastFeedingMonths
        )
        postData.put("reason_if_less_than_six_months", respondent.reasonIfLessThanSixMonths)
        postData.put("age_of_eldest_child", respondent.ageOfEldestChild)
        postData.put("receive_phone_call_reminders", respondent.receivePhoneCallReminders)
        return postData
    }

    //MARK: create respondents
    fun createRespondent(respondent: Respondent? = null, callback: RespondentCallback) {
        var postData = JSONObject()
        respondent?.let {
            postData = formatRespondent(it)
            Respondent().save(respondent)
        }
        val url = baseUrl.plus("respondents/create_or_edit_respondent/")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, postData,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val results = response.getJSONObject("results")
                    callback.onReceivedItem(parseRespondent(results))
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
                //MARK: delete local date
                respondent?.let {
                    Respondent().deleteItem(it.id)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: survey question
    fun surveyQuestions(survey: String? = null, callback: SurveyGroupCallback) {
        val postData = JSONObject()
        postData.put("survey_id", survey)
        val url = baseUrl.plus("")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, postData,
            { response ->
                Log.e(tag, response.toString())
                callback.onRequestEnded()
                val responseCode = response.getString("response_code")
                if (responseCode == "100") {
                    val data = ArrayList<Category>()
                    val results = response.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val item = results.getJSONObject(i)
                        data.add(parseSurveyGroup(item))
                    }
                    callback.onReceivedItems(data)
                } else {
                    val detail = response.getString("detail")
                    callback.onReceivedError(detail)
                }
            },
            { error ->
                callback.onRequestEnded()
                Log.e(tag, error.toString())
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: get user
    fun getUser(callback: UserCallback) {
        val url = baseUrl.plus("users/me/")

    }

    //MARK: upload image
    fun uploadAvatar(file: File, callback: BaseInterface) {
        val url = baseUrl.plus("users/upload_avatar/")

    }

    //MARK: update user
    fun updateUser(
        firstName: String? = null,
        lastName: String? = null,
        userAvatar: String? = null,
        callback: UserCallback
    ) {
        val data = JSONObject()
        firstName?.let {
            data.put("first_name", it)
        }
        lastName?.let {
            data.put("last_name", it)
        }
        userAvatar?.let {
            data.put("user_avatar", it)
        }
        val url = baseUrl.plus("users/me/")
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                Log.e(tag, response.toString())
            },
            { error ->
                Log.e(tag, error.toString())
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return customHeader()
            }
        }
        requestQueue.add(jsonObjectRequest)

    }


    //MARK: part user
    private fun parseUser(jsonObject: JSONObject): User {
        val id = jsonObject.getInt("id").toString()
        val token = jsonObject.getString("auth_token")
        val email = jsonObject.getString("email")
        val username = jsonObject.getString("username")
        val avatar = jsonObject.getString("user_avatar")
        val firstName = jsonObject.getString("first_name")
        val lastName = jsonObject.getString("last_name")
        val fullName = jsonObject.getString("full_name")
        val phoneNumber = jsonObject.getString("phone_number")
        return User(id, token, username, phoneNumber, email, firstName, fullName, lastName, avatar)
    }

    //MARK: part group
    private fun parseSurveyGroup(jsonObject: JSONObject): Category {
        val id = jsonObject.getInt("id").toString()
        val name = jsonObject.getString("name")
        val description = jsonObject.getString("description")
        val icon = jsonObject.getString("icon")
        val numberOfSurveys = jsonObject.getInt("number_of_surveys")
        return Category(id, name, icon, numberOfSurveys, description)
    }

    //MARK: parse survey
    private fun parseSurvey(jsonObject: JSONObject): Survey {
        val id = jsonObject.getInt("id").toString()
        val name = jsonObject.getString("name")
        val description = safeRetrieveJsonData.getStringJSONData(jsonObject, "description")
        val numOfQuestions =
            safeRetrieveJsonData.getDataIntJSONData(jsonObject, "number_of_questions")
        val numOfResponses = jsonObject.getInt("number_of_responses")
        val date = jsonObject.getString("date_created")
        val image = safeRetrieveJsonData.getStringJSONData(jsonObject, "image")
        var questions = "[]"
        if (!jsonObject.isNull("questions")) {
            questions = jsonObject.getJSONArray("questions").toString()
        }
        return Survey(id, name, description, numOfResponses, numOfQuestions, image, questions)
    }

    //MARK: parse respondent
    private fun parseRespondent(jsonObject: JSONObject): Respondent {
        val id = jsonObject.getInt("id").toString()
        val name = jsonObject.getString("name")
        val description = safeRetrieveJsonData.getStringJSONData(jsonObject, "description")
        val numOfQuestions =
            safeRetrieveJsonData.getDataIntJSONData(jsonObject, "number_of_questions")
        val numOfResponses = jsonObject.getInt("number_of_responses")
        val date = jsonObject.getString("date_created")
        val image = safeRetrieveJsonData.getStringJSONData(jsonObject, "image")

        return Respondent(id, name, description)
    }


}