package com.momecarefoundation.app


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonParser
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.api.SafeRetrieveJsonData
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.ResponseCallback
import com.momecarefoundation.app.model.QuestionItem
import com.momecarefoundation.app.model.QuestionOption
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.model.Response
import com.momecarefoundation.app.util.AppPresenter
import com.momecarefoundation.app.util.QuestionType
import com.momecarefoundation.app.util.Utility
import khronos.toString
import kotlinx.android.synthetic.main.activity_take_survey.*
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbarHome
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class TakeSurvey : AppCompatActivity() {

    companion object {
        const val survey = "SURVEY"
        const val tag = "TAG_SURVEY"
    }

    private var safeRetrieveJsonData = SafeRetrieveJsonData()
    private val questions = ArrayList<QuestionItem>()
    private var surveyTitle = ""
    private var surveyId = ""
    private var date = Calendar.getInstance()
    private var respondent: Respondent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_survey)

        setSupportActionBar(toolbarHome as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = "TAKE SURVEY"

        imageViewAdd.setOnClickListener {
            val startRespondentIntent = Intent(this, AddRespondent::class.java)
            startRespondentIntent.putExtra(AddRespondent.respondentCallback, true)
            pickRespondent.launch(startRespondentIntent)
        }


        imageViewSearch.setOnClickListener {
            val startRespondentIntent = Intent(this, MyRespondents::class.java)
            startRespondentIntent.putExtra(AddRespondent.respondentCallback, true)
            pickRespondent.launch(startRespondentIntent)
        }

        buttonSubmit.setOnClickListener {
            if (respondent == null) {
                AppPresenter(this).showMessage(message = "Add respondent and proceed")
                return@setOnClickListener
            }

            val hasUnAnsweredQuestion = arrayListOf<Boolean>()
            val answers = ArrayList<QuestionOption>()
            questions.forEach { question ->
                if (question.answersOptions.find { it.isSelected } != null) {
                    val selectedOption = question.answersOptions.filter { it.isSelected }
                    answers.addAll(selectedOption)
                } else {
                    hasUnAnsweredQuestion.add(false)
                }
            }

            if (hasUnAnsweredQuestion.isNotEmpty()) {
                AppPresenter(this).showMessage(message = "Answer all questions and proceed")
                return@setOnClickListener
            }

            val lat = MoMeCare.locationReceived?.latitude ?: 0.00
            val lon = MoMeCare.locationReceived?.longitude ?: 0.00

            var postedAnswer = answers.toString()
            var postedRespondent = respondent.toString()

            if (!Utility().hasNetworkConnection(this)) {
                postedAnswer = answers.toString().replace("\"", "'")
                postedRespondent = respondent.toString().replace("\"", "'")
            }

            val response = Response(
                Calendar.getInstance().timeInMillis.toString(),
                surveyId,
                postedAnswer,
                postedRespondent,
                lat,
                lon,
                Utility().getAddress(this, lat, lon),
                false
            )

            buttonSubmit.isEnabled = false
            progressBar.visibility = View.VISIBLE


            APICall(this).createResponse(response, object : ResponseCallback {
                override fun onRequestEnded() {
                    super.onRequestEnded()

                    buttonSubmit.isEnabled = true
                    progressBar.visibility = View.INVISIBLE

                    AppPresenter(this@TakeSurvey).showMessage(
                        message = "Response successfully submitted. You either head back or add more.",
                        positiveAction = "ADD MORE",
                        negativeActive = "BACK",
                        presenterCallback = object : PresenterCallback {
                            override fun onActionSelected(item: Any) {
                                super.onActionSelected(item)
                                val action = item as String
                                if (action == "ADD MORE") {
                                    resetFields()
                                } else {
                                    finish()
                                }
                            }
                        })
                }
            })

        }

        getData()
    }

    private fun resetFields() {
        respondent = null
        textViewRespondent.text = "Add Respondent"
    }

    //MARK: handle start location things
    private val pickRespondent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val respondentIn = result.data?.getStringExtra(AddRespondent.respondent)
                respondentIn?.let {
                    val objectData = JSONObject(it)
                    respondent = APICall(this).parseRespondent(objectData)
                    textViewRespondent.text = "${respondent?.lastName} ${respondent?.firstName}"
                }
            }
        }

    private fun getData() {
        val parsedData = intent.getStringExtra(survey)
        val questionDataRaw = JsonParser().parse(parsedData).asJsonObject
        surveyTitle = safeRetrieveJsonData.getStringJsonData(questionDataRaw, "name")
        surveyId = safeRetrieveJsonData.getStringJsonData(questionDataRaw, "id")
        val jsonDataString = safeRetrieveJsonData.getStringJsonData(questionDataRaw, "questions")
        val jsonData = JsonParser().parse(jsonDataString).asJsonArray

        jsonData.forEach {
            val questionData = it.asJsonObject
            val id = safeRetrieveJsonData.getStringJsonData(questionData, "id")
            val name = safeRetrieveJsonData.getStringJsonData(questionData, "content")
            val type = safeRetrieveJsonData.getStringJsonData(questionData, "question_type")
            val answerOptions = safeRetrieveJsonData.getArrayJsonData(questionData, "answers")
            val options = ArrayList<QuestionOption>()
            if (answerOptions.size() == 0) {
                val placeAnswer = QuestionOption(id, "", false)
                options.add(placeAnswer)
            }
            answerOptions.forEach { optionJson ->
                val optionId = safeRetrieveJsonData.getStringJsonData(optionJson.asJsonObject, "id")
                val optionLabel =
                    safeRetrieveJsonData.getStringJsonData(optionJson.asJsonObject, "content")
                val questionOption = QuestionOption(optionId, optionLabel, false)
                options.add(questionOption)
            }
            val questionItem = QuestionItem(id, name, type, options)
            questions.add(questionItem)
        }

        createQuestionUi()
    }

    private fun createQuestionUi() {
        textViewSurvey.text = surveyTitle
        questions.forEach {
            //MARK: set question title here
            val textViewQuestion =
                layoutInflater.inflate(R.layout.layout_question_textview, null) as TextView
            textViewQuestion.id = Calendar.getInstance().timeInMillis.toInt()
            textViewQuestion.text = it.title
            linearLayoutContent.addView(textViewQuestion)

            when (it.type) {
                QuestionType.OPEN_ENDED.name -> {
                    val textInputLayout =
                        layoutInflater.inflate(
                            R.layout.layout_question_input,
                            null
                        ) as TextInputLayout
                    textInputLayout.id = Calendar.getInstance().timeInMillis.toInt()
                    val editText = textInputLayout.editText
                    editText?.id = Calendar.getInstance().timeInMillis.toInt()
                    editText?.doAfterTextChanged { editInput ->
                        val answerText = editInput.toString().trim()
                        it.answersOptions[0].label = answerText
                        it.answersOptions[0].isSelected = answerText.isNotEmpty()
                    }
                    //MARK: add to parent
                    linearLayoutContent.addView(textInputLayout)
                }
                QuestionType.DATE.name -> {
                    val textInputLayout =
                        layoutInflater.inflate(
                            R.layout.layout_question_input,
                            null
                        ) as TextInputLayout
                    textInputLayout.id = Calendar.getInstance().timeInMillis.toInt()
                    val editText = textInputLayout.editText
                    editText?.id = Calendar.getInstance().timeInMillis.toInt()
                    textInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    textInputLayout.setEndIconDrawable(R.drawable.ic_add_circle)
                    textInputLayout.setEndIconOnClickListener { _ ->
                        showDateTimePicker(it, editText!!)
                    }
                    //editText?.hint = "Select Date"
                    editText?.isFocusable = false
                    editText?.setOnClickListener { _ ->
                        showDateTimePicker(it, editText)
                    }

                    //MARK: add to parent
                    linearLayoutContent.addView(textInputLayout)
                }
                QuestionType.SINGLE_SELECT_MULTIPLE_CHOICE.name -> {
                    //MARK: create option holder
                    val singleSelectButton = layoutInflater.inflate(
                        R.layout.layout_question_single_select_group,
                        null
                    ) as MaterialButtonToggleGroup
                    singleSelectButton.orientation = LinearLayout.VERTICAL
                    singleSelectButton.isSingleSelection = true
                    singleSelectButton.id = Calendar.getInstance().timeInMillis.toInt()
                    //MARK: create options
                    it.answersOptions.forEach { option ->
                        val button = layoutInflater.inflate(
                            R.layout.layout_answer_select_item,
                            null,
                            false
                        ) as MaterialButton
                        button.id = option.id.toInt()
                        button.tag = option.id.toInt()
                        button.text = option.label
                        singleSelectButton.addView(button)
                    }


                    singleSelectButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
                        if (group.checkedButtonId == -1) group.check(checkedId)
                        if (!isChecked) return@addOnButtonCheckedListener
                        val materialButton: MaterialButton = group.findViewById(checkedId)
                        //MARK: all as false just one
                        it.answersOptions.forEach { option ->
                            option.isSelected = false
                        }

                        val optionClicked = QuestionOption(
                            materialButton.id.toString(),
                            materialButton.text.toString(),
                            true
                        )
                        //MARK: currently selected answer
                        val position = it.answersOptions.indexOf(optionClicked)
                        it.answersOptions[position].isSelected = true

                        Log.e("SINGLE", it.answersOptions.toString())
                    }
                    //MARK: add to parent
                    linearLayoutContent.addView(singleSelectButton)
                }
                QuestionType.MULTI_SELECT_MULTIPLE_CHOICE.name -> {
                    //MARK: create option holder
                    val singleSelectButton = layoutInflater.inflate(
                        R.layout.layout_question_single_select_group,
                        null
                    ) as MaterialButtonToggleGroup
                    singleSelectButton.orientation = LinearLayout.VERTICAL
                    singleSelectButton.isSingleSelection = false
                    singleSelectButton.id = Calendar.getInstance().timeInMillis.toInt()
                    //MARK: create options
                    it.answersOptions.forEach { option ->
                        val button = layoutInflater.inflate(
                            R.layout.layout_answer_select_item,
                            null,
                            false
                        ) as MaterialButton
                        button.id = option.id.toInt()
                        button.tag = option.id.toInt()
                        button.text = option.label
                        singleSelectButton.addView(button)
                    }
                    singleSelectButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
                        val materialButton: MaterialButton = group.findViewById(checkedId)
                        val optionClicked = QuestionOption(
                            materialButton.id.toString(),
                            materialButton.text.toString(),
                            isChecked
                        )
                        //MARK: update selection
                        val position = it.answersOptions.indexOf(optionClicked)
                        it.answersOptions[position].isSelected = isChecked

                        Log.e("MULTI", it.answersOptions.toString())
                    }
                    //MARK: add to parent
                    linearLayoutContent.addView(singleSelectButton)
                }
            }
        }

    }

    //MARK: present the data picker
    private fun showDateTimePicker(questionItem: QuestionItem, editText: EditText) {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)

                val position = questions.indexOf(questionItem)
                questions[position].answersOptions[0].label =
                    Date(date.timeInMillis).toString("YYYY-MM-DD")
                questions[position].answersOptions[0].isSelected = true

                editText.setText(Date(date.timeInMillis).toString("YYYY-MM-DD"))

            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}