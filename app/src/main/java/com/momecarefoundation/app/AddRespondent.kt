package com.momecarefoundation.app

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.RespondentCallback
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.util.AppPresenter
import khronos.toString
import kotlinx.android.synthetic.main.activity_add_respondent.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbarHome
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddRespondent : AppCompatActivity() {


    companion object {
        const val respondent = "RESPONDENT"
        const val respondentCallback = "RESPONDENT_CALL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_respondent)

        setSupportActionBar(toolbarHome as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = "ADD RESPONDENT"

        val communities = arrayListOf("Asawinso", "Atsiekpoe", "Deveme", "Vume")
        editTextCommunity.setOnClickListener {
            AppPresenter(this).showList(
                getString(R.string.app_name),
                communities.toTypedArray(),
                object : PresenterCallback {
                    override fun onActionSelected(item: Any) {
                        super.onActionSelected(item)
                        editTextCommunity.setText(item as String)
                    }
                })
        }


        val languages = arrayListOf("English", "Twi", "Ewe", "Sefwi")
        editTextLanguage.setOnClickListener {
            AppPresenter(this).showList(
                getString(R.string.app_name),
                languages.toTypedArray(),
                object : PresenterCallback {
                    override fun onActionSelected(item: Any) {
                        super.onActionSelected(item)
                        editTextLanguage.setText(item as String)
                    }
                })
        }

        editTextRecentDeliveryDate.setOnClickListener {
            showDateTimePicker(editTextRecentDeliveryDate)
        }

        editTextDateOfBirth.setOnClickListener {
            showDateTimePicker(editTextDateOfBirth)
        }

        buttonSubmit.setOnClickListener {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val community = editTextCommunity.text.toString().trim()
            val language = editTextLanguage.text.toString().trim()
            val sonAge = editTextRecentEldersSon.text.toString().trim()
            val dateOfBirth = editTextDateOfBirth.text.toString().trim()
            val recentChildDeliveryDate = editTextRecentDeliveryDate.text.toString().trim()
            var numberOfChildren = editTextNumberOfChildren.text.toString().trim()
            val sendReminders = appCompatCheckBoxReminderMe.isChecked

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || community.isEmpty() || language.isEmpty() || sonAge.isEmpty()) {
                AppPresenter(this).showMessage(message = "Respondent's first name, Last name, phone, community, language and oldest son's age is required")
                return@setOnClickListener
            }

            var birthDateParse: Date? = null
            if (dateOfBirth.isNotEmpty()) {
                birthDateParse = SimpleDateFormat("yyyy-mm-dd").parse(dateOfBirth)
            }

            var recentBirthDateParse: Date? = null
            if (recentChildDeliveryDate.isNotEmpty()) {
                recentBirthDateParse = SimpleDateFormat("yyyy-mm-dd").parse(recentChildDeliveryDate)
            }

            if (numberOfChildren.isEmpty()) {
                numberOfChildren = "0"
            }

            val innerRespondent = Respondent(
                Calendar.getInstance().timeInMillis.toString(),
                firstName,
                lastName,
                phone = phone,
                community = community.toUpperCase(),
                ageOfEldestChild = sonAge,
                languageSpoken = language.toUpperCase(),
                receivePhoneCallReminders = sendReminders,
                dateOfBirth = birthDateParse,
                recentDeliveryDate = recentBirthDateParse,
                numberOfChildren = numberOfChildren,
                isBackedUp = false
            )

            progressBar.visibility = View.VISIBLE
            buttonSubmit.isEnabled = false

            APICall(this).createRespondent(innerRespondent, object : RespondentCallback {
                override fun onRequestEnded() {
                    super.onRequestEnded()

                    progressBar.visibility = View.INVISIBLE
                    buttonSubmit.isEnabled = true
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
                                    if (intent.getBooleanExtra(respondentCallback, false)) {
                                        intent.putExtra(respondent, innerRespondent.toString())
                                        setResult(Activity.RESULT_OK, intent)
                                    }
                                    finish()
                                }
                            }
                        })
                }
            })

        }
    }

    //MARK: present the data picker
    private fun showDateTimePicker(editText: EditText) {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                editText.setText("$year-${monthOfYear + 1}-$dayOfMonth")
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
    }

    private fun resetFields() {
        editTextFirstName.setText("")
        editTextLastName.setText("")
        editTextDateOfBirth.setText("")
        editTextRecentDeliveryDate.setText("")
        editTextPhone.setText("")
        editTextCommunity.setText("")
        editTextLanguage.setText("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}