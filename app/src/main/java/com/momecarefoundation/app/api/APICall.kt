package com.momecarefoundation.app.api

import android.content.Context
import android.provider.Telephony.Carriers.PASSWORD
import android.util.ArrayMap
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.momecarefoundation.app.BuildConfig
import com.momecarefoundation.app.callback.BaseInterface
import com.momecarefoundation.app.callback.UserCallback
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.model.Response
import com.momecarefoundation.app.model.User
import org.json.JSONObject
import java.io.File


class APICall(private var context: Context) {
    private var genericError = "Failed to reach server, try again later."
    private val requestQueue = Volley.newRequestQueue(context.applicationContext)
    private var user = User().getUser()

    private fun customHeader(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        // headers["Content-Type"] = "application/json; charset=utf-8";
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
        val allRespondent = Respondent().allOfflineRespondents()


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
            },
            { error ->
                Log.e(tag, error.toString())
                callback.onRequestEnded()
                callback.onReceivedError(genericError)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val header = customHeader()
                Log.e("HEADER", header.toString())
                return header
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    //MARK: reset session
    fun initPasswordReset(number: String, callback: UserCallback) {
        val data = JSONObject()
        data.put("phone_number", number)
        val url = baseUrl.plus("users/forgot_password/")
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

    //MARK: reset session
    fun passwordReset(code: String, password: String, callback: UserCallback) {
        val data = JSONObject()
        data.put("unique_code", code)
        data.put("new_password", password)

        val url = baseUrl.plus("users/reset_password/")
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


}