package com.momecarefoundation.app.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.momecarefoundation.app.R


/**
 * Created by Monarchy on 09/10/2017.
 */

class ViewHolderRespondent(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageViewIcon: ImageView? = null
    var textViewName: TextView? = null
    var textViewInfo: TextView? = null
    var textViewDate: TextView? = null

    init {
        imageViewIcon = itemView.findViewById(R.id.imageViewIcon)
        textViewName = itemView.findViewById(R.id.textViewName)
        textViewInfo = itemView.findViewById(R.id.textViewInfo)
        textViewDate = itemView.findViewById(R.id.textViewDate)
    }
}
