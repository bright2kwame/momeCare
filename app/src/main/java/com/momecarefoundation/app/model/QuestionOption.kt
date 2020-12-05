package com.momecarefoundation.app.model

data class QuestionOption(var id: String, var label: String, var isSelected: Boolean){

    override fun toString(): String {
        return "{question_id:$id,answer_content:$label}"
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