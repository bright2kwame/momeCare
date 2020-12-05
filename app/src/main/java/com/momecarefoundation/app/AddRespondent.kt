package com.momecarefoundation.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.RespondentCallback
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.util.AppPresenter
import kotlinx.android.synthetic.main.activity_add_respondent.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbarHome
import java.util.*

class AddRespondent : AppCompatActivity() {

    companion object {
        const val respondent = "RESPONDENT"
        const val resultCode = 402
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_respondent)

        setSupportActionBar(toolbarHome as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = "ADD RESPONDENT"


        buttonSubmit.setOnClickListener {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val sendReminders = appCompatCheckBoxReminderMe.isChecked

            val respondDent = Respondent(
                Calendar.getInstance().timeInMillis.toString(),
                firstName,
                lastName,
                phone = phone
            )

            APICall(this).createRespondent(respondDent, object : RespondentCallback {
                override fun onRequestEnded() {
                    super.onRequestEnded()
                    AppPresenter(this@AddRespondent).showMessage(
                        message = "Respondent successfully added. You either head back or add more.",
                        positiveAction = "ADD MORE",
                        negativeActive = "BACK",
                        presenterCallback = object : PresenterCallback {
                            override fun onActionSelected(item: Any) {
                                super.onActionSelected(item)
                                val action = item as String
                                if (action == "ADD MORE") {
                                    resetFields()
                                } else {
                                    intent.putExtra(respondent, respondDent.toString())
                                    setResult(resultCode, intent)
                                    finish()
                                }
                            }
                        })
                }
            })

        }
    }

    private fun resetFields() {
        editTextFirstName.setText("")
        editTextLastName.setText("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}