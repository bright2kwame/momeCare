package com.momecarefoundation.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.momecarefoundation.app.model.User
import java.util.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val delay = 1000L

        Handler().postDelayed(
            {
                navigate()
            },
            delay
        )
    }

    // MARK: animation on the activities
    private fun navigate() {
        val intent = Intent()
        val noUser = User().getUser() == null
        when {
            noUser -> {
                intent.setClass(this, Tutorial::class.java)
            }
            else -> {
                intent.setClass(this, Home::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}