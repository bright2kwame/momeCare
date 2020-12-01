package com.momecarefoundation.app.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momecarefoundation.app.R
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.model.Category
import com.momecarefoundation.app.model.Respondent
import com.momecarefoundation.app.model.Response
import com.momecarefoundation.app.model.Survey
import com.squareup.picasso.Picasso

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

    }

    // MARK: configure the respondent
    private fun configureRespondentViewHolder(viewHolder: ViewHolderRespondent, data: Respondent) {
        val parent = viewHolder.itemView

    }

    // MARK: configure the response
    private fun configureResponseViewHolder(viewHolder: ViewHolderResponse, data: Response) {
        val parent = viewHolder.itemView

    }

    // MARK: configure the survey
    private fun configureSurveyViewHolder(viewHolder: ViewHolderSurvey, data: Survey) {
        val parent = viewHolder.itemView

    }
}
