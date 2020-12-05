package com.momecarefoundation.app.model

data class QuestionItem(
    var id: String,
    var title: String,
    var type: String,
    var answersOptions: ArrayList<QuestionOption>
){
    override fun toString(): String {
        return "$id $title"
    }
}