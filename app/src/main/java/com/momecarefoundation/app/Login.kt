package com.momecarefoundation.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momecarefoundation.app.bottomSheet.BottomSheetResetPassword
import com.momecarefoundation.app.callback.GenericCallback
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
            val phoneNumber = editTextEmailAddress.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                AppPresenter(this).showMessage(message = "Provide a valid account information and proceed")
                return@setOnClickListener
            }

        }

        textViewPassword.setOnClickListener {
            val phoneNumber = editTextEmailAddress.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                AppPresenter(this).showMessage(message = "Provide a phone number to reset password")
                return@setOnClickListener
            }

            BottomSheetResetPassword.newInstance(action = object : GenericCallback {
                override fun onCompleted(item: Any) {

                }

                override fun onError(item: Any) {

                }
            }).show(supportFragmentManager, tag)

        }
    }
}