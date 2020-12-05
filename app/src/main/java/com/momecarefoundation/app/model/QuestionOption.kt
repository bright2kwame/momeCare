package com.momecarefoundation.app.model

data class QuestionOption(var id: String, var label: String, var isSelected: Boolean){
    override fun toString(): String {
        return "$label"
    }

    override fun equals(other: Any?): Boolean {
        return (other is QuestionOption) && (this.id == other.id && this.label == other.label)
    }
}