package com.momecarefoundation.app.model

import org.json.JSONObject

data class QuestionOption(var id: String, var label: String, var isSelected: Boolean){

    override fun toString(): String {
        return parseToJson().toString()
    }

   private fun parseToJson(): JSONObject {
        val postData = JSONObject()
        postData.put("question_id", id)
        postData.put("answer_content", label)
        return postData
    }

    override fun equals(other: Any?): Boolean {
        return (other is QuestionOption) && (this.id == other.id && this.label == other.label)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}