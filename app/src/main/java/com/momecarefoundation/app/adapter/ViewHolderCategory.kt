package com.momecarefoundation.app.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.momecarefoundation.app.R


/**
 * Created by Monarchy on 09/10/2017.
 */

class ViewHolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageViewCategoryIcon: ImageView? = null
    var imageViewCategory: ImageView? = null
    var textViewName: TextView? = null
    var textViewInfo: TextView? = null

    init {
        imageViewCategoryIcon = itemView.findViewById(R.id.imageViewCategoryIcon)
        imageViewCategory = itemView.findViewById(R.id.imageViewCategory)
        textViewName = itemView.findViewById(R.id.textViewName)
        textViewInfo = itemView.findViewById(R.id.textViewInfo)
    }
}
