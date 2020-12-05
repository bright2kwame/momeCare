package com.momecarefoundation.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.RespondentCallback
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.util.AppPresenter
import com.momecarefoundation.app.util.CommunityOptions
import com.momecarefoundation.app.util.LanguageOptions
import kotlinx.android.synthetic.main.activity_add_respondent.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbarHome
import java.util.*

class AddRespondent : AppCompatActivity() {

    companion object {
        const val respondent = "RESPONDENT"
        const val resultCode = 402
        const val respondentCallback = "RESPONDENT_CALL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_respondent)

        setSupportActionBar(toolbarHome as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = "ADD RESPONDENT"

        val communities = arrayListOf(CommunityOptions.values())
        val adapter = ArrayAdapter(
            this,
            R.layout.layout_picker_item,
            communities
        )

        editTextCommunity.setAdapter(adapter)
        editTextCommunity.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos, _ ->
                editTextCommunity.setText(communities[pos].toString())
            }


        val languages = arrayListOf(LanguageOptions.values())
        val adapterLanguage = ArrayAdapter(
            this,
            R.layout.layout_picker_item,
            languages
        )

        editTextLanguage.setAdapter(adapterLanguage)
        editTextLanguage.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos, _ ->
                editTextLanguage.setText(languages[pos].toString())
            }


        buttonSubmit.setOnClickListener {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val community = editTextCommunity.text.toString().trim()
            val language = editTextLanguage.text.toString().trim()
            val sonAge = editTextRecentEldersSon.text.toString().trim()
            val sendReminders = appCompatCheckBoxReminderMe.isChecked

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || community.isEmpty() || language.isEmpty() || sonAge.isEmpty()) {
                AppPresenter(this).showMessage(message = "Respondent's first name, Last name, phone, community, language and oldest son's age is required")
                return@setOnClickListener
            }

            val respondDent = Respondent(
                Calendar.getInstance().timeInMillis.toString(),
                firstName,
                lastName,
                phone = phone,
                community = community,
                ageOfEldestChild = sonAge,
                languageSpoken = language,
                receivePhoneCallReminders = sendReminders
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