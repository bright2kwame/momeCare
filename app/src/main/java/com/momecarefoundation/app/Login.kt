package com.momecarefoundation.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.bottomSheet.BottomSheetResetPassword
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.UserCallback
import com.momecarefoundation.app.model.User
import com.momecarefoundation.app.util.AppPresenter
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    companion object {
        private const val tag = "LOGIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonSignIn.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                AppPresenter(this).showMessage(message = "Provide a valid account information and proceed")
                return@setOnClickListener
            }

            startLogin(number = phoneNumber, password)
        }

        textViewPassword.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                AppPresenter(this).showMessage(message = "Provide a phone number to reset password")
                return@setOnClickListener
            }

            BottomSheetResetPassword.newInstance(phoneNumber, action = object : PresenterCallback {

                override fun onActionSelected(item: Any) {

                }

            }).show(supportFragmentManager, tag)

        }
    }

    //MARK: handle login
    private fun startLogin(number: String, password: String) {
        progressBar.visibility = View.VISIBLE
        buttonSignIn.isEnabled = false

        APICall(this).login(number, password, object : UserCallback {
            override fun onRequestEnded() {
                super.onRequestEnded()
                progressBar.visibility = View.INVISIBLE
                buttonSignIn.isEnabled = true
            }

            override fun onReceivedDetail(message: String) {
                super.onReceivedDetail(message)
                AppPresenter(this@Login).showMessage(message = message)
            }

            override fun onReceivedError(error: String) {
                super.onReceivedError(error)
                AppPresenter(this@Login).showMessage(message = error)
            }

            override fun onReceivedItem(result: User) {
                super.onReceivedItem(result)
                goHome()
            }
        })
    }

    //MARK: navigate home
    private fun goHome() {
        val actionIntent = Intent(this, Home::class.java)
        startActivity(actionIntent)
        finish()
    }
}