package com.momecarefoundation.app


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonParser
import com.momecarefoundation.app.api.SafeRetrieveJsonData
import com.momecarefoundation.app.model.QuestionItem
import com.momecarefoundation.app.model.QuestionOption
import com.momecarefoundation.app.util.QuestionType
import khronos.toString
import kotlinx.android.synthetic.main.activity_take_survey.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbarHome
import java.util.*
import kotlin.collections.ArrayList


class TakeSurvey : AppCompatActivity() {

    companion object {
        const val survey = "SURVEY"
    }

    private var safeRetrieveJsonData = SafeRetrieveJsonData()
    private val questions = ArrayList<QuestionItem>()
    private var surveyTitle = ""
    private var date = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_survey)

        setSupportActionBar(toolbarHome as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = "TAKE SURVEY"

        getData()

        textViewRespondent.setOnClickListener {

        }
    }

    private fun getData() {
        val parsedData = intent.getStringExtra(survey)

        val questionDataRaw = JsonParser().parse(parsedData).asJsonObject
        surveyTitle = safeRetrieveJsonData.getStringJsonData(questionDataRaw, "name")
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
            textViewQuestion.id = it.id.toInt()
            textViewQuestion.text = it.title
            linearLayoutContent.addView(textViewQuestion)

            when (it.type) {
                QuestionType.OPEN_ENDED.name -> {
                    val textInputLayout =
                        layoutInflater.inflate(
                            R.layout.layout_question_input,
                            null
                        ) as TextInputLayout
                    val editText = textInputLayout.editText
                    editText?.doAfterTextChanged { editInput ->
                        it.answersOptions[0].label = editInput.toString().trim()
                        it.answersOptions[0].isSelected = true
                    }
                }
                QuestionType.DATE.name -> {
                    val textInputLayout =
                        layoutInflater.inflate(
                            R.layout.layout_question_input,
                            null
                        ) as TextInputLayout
                    textInputLayout.setEndIconDrawable(R.drawable.common_google_signin_btn_icon_dark)
                    textInputLayout.setEndIconOnClickListener { _ ->
                        showDateTimePicker(it)
                    }
                    val editText = textInputLayout.editText
                    editText?.isFocusable = false
                }
                QuestionType.SINGLE_SELECT_MULTIPLE_CHOICE.name -> {
                    //MARK: create option holder
                    val singleSelectButton = layoutInflater.inflate(
                        R.layout.layout_question_single_select_group,
                        null
                    ) as MaterialButtonToggleGroup
                    singleSelectButton.orientation = LinearLayout.VERTICAL
                    singleSelectButton.isSingleSelection = true
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
                    }
                    //MARK: add to parent
                    linearLayoutContent.addView(singleSelectButton)
                }
            }
        }

    }

    //MARK: update the selected answer
    private fun updateSelectedAnswer() {

    }

    //MARK: update the selected answer
    private fun removeSelectedAnswer() {

    }

    //MARK: present the data picker
    private fun showDateTimePicker(questionItem: QuestionItem) {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        val position = questions.indexOf(questionItem)
                        questions[position].answersOptions[0].label =
                            Date(date.timeInMillis).toString("YYYY-MM-DD")
                        questions[position].answersOptions[0].isSelected = true
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
                ).show()
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