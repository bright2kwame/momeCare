package com.momecarefoundation.app.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.momecarefoundation.app.R


/**
 * Created by Monarchy on 09/10/2017.
 */

class ViewHolderResponse(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewResponseNumber: TextView? = null
    var textViewDate: TextView? = null
    var textViewInfo: TextView? = null
    
    init {
        textViewResponseNumber = itemView.findViewById(R.id.textViewResponseNumber)
        textViewDate = itemView.findViewById(R.id.textViewDate)
        textViewInfo = itemView.findViewById(R.id.textViewInfo)
    }
}
