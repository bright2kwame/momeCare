package com.momecarefoundation.app.model

import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Monarchy on 10/10/2017.
 */
open class Category(
    @PrimaryKey var id: String = "",
    var token: String = "",
    var number: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var profile: String = "",
) : RealmObject() {

    // clear all objects from UserModel.class
    fun clearAll() {
        Category().deleteAll()
    }

    // save a user model
    fun saveUser(userModel: Category) {
        userModel.save()
    }

    // query a single first item
    fun getUser(): Category? {
        return Category().queryFirst()
    }


    // MARK: remove the token
    fun removeToken() {
        val user = Category().queryFirst()
        user?.token = ""
        if (user != null) {
            saveUser(user)
        }
    }

    // MARK: update the user
    fun updateUserNumber(number: String) {
        val user = Category().queryFirst()
        user?.number = number
        if (user != null) {
            saveUser(user)
        }
    }

    private fun toJSON(): String {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("id", id)
            jsonObject.put("phone_number", number)
            jsonObject.put("email", email)
            jsonObject.put("avatar", profile)
            jsonObject.put("first_name", firstName)
            jsonObject.put("last_name", lastName)
            jsonObject.put("profile", profile)
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
