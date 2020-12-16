package com.momecarefoundation.app.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.momecarefoundation.app.R
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.model.Category
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.model.Response
import com.momecarefoundation.app.model.Survey
import com.momecarefoundation.app.util.Utility
import com.squareup.picasso.Picasso
import org.json.JSONObject

/**
 * Created by Monarchy on 09/10/2017.
 */

class FeedAdapter(private var items: List<Any>, private var adapterCallback: AdapterCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoryConstant = 100
    private val respondentConstant = 200
    private val respondsConstant = 300
    private val surveyConstant = 400


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            categoryConstant -> {
                val viewHolderItem = inflater.inflate(R.layout.category_item, viewGroup, false)
                viewHolder = ViewHolderCategory(viewHolderItem)
            }
            respondentConstant -> {
                val viewHolderItem = inflater.inflate(R.layout.respondent_item, viewGroup, false)
                viewHolder = ViewHolderRespondent(viewHolderItem)
            }
            respondsConstant -> {
                val viewHolderItem = inflater.inflate(R.layout.response_item, viewGroup, false)
                viewHolder = ViewHolderResponse(viewHolderItem)
            }
            surveyConstant -> {
                val viewHolderItem = inflater.inflate(R.layout.survey_item, viewGroup, false)
                viewHolder = ViewHolderSurvey(viewHolderItem)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            categoryConstant -> {
                val viewHolderDevice = holder as ViewHolderCategory
                configureCategoryViewHolder(viewHolderDevice, items[position] as Category)
            }
            respondentConstant -> {
                val viewHolderRespondent = holder as ViewHolderRespondent
                configureRespondentViewHolder(viewHolderRespondent, items[position] as Respondent)
            }
            respondsConstant -> {
                val viewHolderResponse = holder as ViewHolderResponse
                configureResponseViewHolder(viewHolderResponse, items[position] as Response)
            }
            surveyConstant -> {
                val viewHolderSurvey = holder as ViewHolderSurvey
                configureSurveyViewHolder(viewHolderSurvey, items[position] as Survey)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is Category -> categoryConstant
            items[position] is Respondent -> respondentConstant
            items[position] is Response -> respondsConstant
            items[position] is Survey -> surveyConstant
            else -> -1
        }
    }

    // MARK: configure the category
    private fun configureCategoryViewHolder(viewHolder: ViewHolderCategory, data: Category) {
        val parent = viewHolder.itemView
        val textViewName = viewHolder.textViewName
        val textViewInfo = viewHolder.textViewInfo
        val imageViewCategory = viewHolder.imageViewCategory

        textViewName?.text = data.name
        textViewInfo?.text = data.info
        if (data.icon.isNotEmpty()) {
            Picasso.get().load(data.icon).into(imageViewCategory)
        } else {
            val textDrawable = Utility().getLetterView(parent.context, data.name, 24, null, false)
            imageViewCategory?.setImageDrawable(textDrawable)
        }
        parent.setOnClickListener {
            adapterCallback.onActionPerformed(data, viewHolder.adapterPosition)
        }

    }

    // MARK: configure the respondent
    private fun configureRespondentViewHolder(viewHolder: ViewHolderRespondent, data: Respondent) {
        val parent = viewHolder.itemView
        val textViewDate = viewHolder.textViewDate
        val textViewInfo = viewHolder.textViewInfo
        val textViewName = viewHolder.textViewName
        val imageViewIcon = viewHolder.imageViewIcon

        textViewName?.text = "${data.lastName} ${data.firstName}"
        textViewInfo?.text = data.phone
        textViewDate?.text = data.community

        val textDrawable = Utility().getLetterView(parent.context, data.firstName, 50, null, true)
        imageViewIcon?.setImageDrawable(textDrawable)

        parent.setOnClickListener {
            adapterCallback.onActionPerformed(data, viewHolder.adapterPosition)
        }

    }

    // MARK: configure the response
    private fun configureResponseViewHolder(viewHolder: ViewHolderResponse, data: Response) {
        val parent = viewHolder.itemView
        val textViewResponseNumber = viewHolder.textViewResponseNumber
        val textViewInfo = viewHolder.textViewInfo
        val textViewDate = viewHolder.textViewDate

        textViewResponseNumber?.text = data.id
        val respondent = APICall(viewHolder.itemView.context).parseRespondent(JSONObject(data.respondent))
        textViewInfo?.text = "${respondent.lastName} ${respondent.firstName}"
        textViewDate?.text = "${respondent.phone}"

        parent.setOnClickListener {
            adapterCallback.onActionPerformed(data, viewHolder.adapterPosition)
        }
    }

    // MARK: configure the survey
    private fun configureSurveyViewHolder(viewHolder: ViewHolderSurvey, data: Survey) {
        val parent = viewHolder.itemView
        val textViewName = viewHolder.textViewName
        val textViewInfo = viewHolder.textViewInfo
        val imageViewIcon = viewHolder.imageViewIcon
        val imageViewAction = viewHolder.imageViewAction
        val textViewResponds = viewHolder.textViewResponds

        textViewName?.text = data.name
        textViewInfo?.text = data.info
        textViewResponds?.text = Utility().formatItem(data.numberOfResponse, "Respond")

        if (data.icon.isNotEmpty()) {
            Picasso.get().load(data.icon).into(imageViewIcon)
        } else {
            val textDrawable = Utility().getLetterView(parent.context, data.name, 80, null, false)
            imageViewIcon?.setImageDrawable(textDrawable)
        }

        imageViewAction?.setOnClickListener {
            adapterCallback.onActionPerformed(data, viewHolder.adapterPosition)
        }

        parent.setOnClickListener {
            adapterCallback.onActionPerformed(data, viewHolder.adapterPosition)
        }
    }
}
