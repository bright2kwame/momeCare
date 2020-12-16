package com.momecarefoundation.app.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.momecarefoundation.app.R


/**
 * Created by Monarchy on 09/10/2017.
 */

class ViewHolderSurvey(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageViewIcon: ImageView? = null
    var imageView: ImageView? = null
    var imageViewAction: MaterialButton? = null
    var textViewName: TextView? = null
    var textViewInfo: TextView? = null
    var textViewResponds: TextView? = null

    init {
        imageViewIcon = itemView.findViewById(R.id.imageViewIcon)
        imageView = itemView.findViewById(R.id.imageView)
        textViewName = itemView.findViewById(R.id.textViewName)
        textViewInfo = itemView.findViewById(R.id.textViewInfo)
        textViewResponds = itemView.findViewById(R.id.textViewResponds)
        imageViewAction = itemView.findViewById(R.id.imageViewAction)
    }
}
