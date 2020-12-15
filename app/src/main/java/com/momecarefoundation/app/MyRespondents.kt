package com.momecarefoundation.app

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.momecarefoundation.app.adapter.FeedAdapter
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.callback.RespondentCallback
import com.momecarefoundation.app.model.Respondent
import kotlinx.android.synthetic.main.activity_my_respondents.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MyRespondents : AppCompatActivity() {
    private val feedItems = ArrayList<Respondent>()
    private var baseAdapter: FeedAdapter? = null
    private var tag = "RESPONDENT"
    private var apiCall: APICall? = null
    private var savedList = ArrayList<Respondent>()
    private var moreUrl = ""
    private val loadedPages = ArrayList<String>()
    private val baseUrl = APICall.baseUrl.plus("respondents/filter_respondents/")

    companion object {
        const val respondent = "RESPONDENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_respondents)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = tag


        apiCall = APICall(this)
        baseAdapter = FeedAdapter(feedItems, object : AdapterCallback {
            override fun onActionPerformed(item: Any, position: Int) {
                super.onActionPerformed(item, position)

                val respondent = item as Respondent
                intent.putExtra(AddRespondent.respondent, respondent.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = baseAdapter


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastPosition = feedItems.size - 5
                if (linearLayoutManager.findLastVisibleItemPosition() >= lastPosition) {
                    if (feedItems.isNotEmpty() && moreUrl.isNotEmpty() && !loadedPages.contains(moreUrl)) {
                        loadedPages.add(moreUrl)
                        getData(moreUrl)
                    }
                }
            }
        })

        editTextSearch.doAfterTextChanged {
            searchList(it.toString().trim())
        }

        getData(baseUrl)
    }

    private fun getData(urlIn: String) {
        progressBar.visibility = View.VISIBLE
        apiCall?.myRespondents(urlIn, object : RespondentCallback{

            override fun onReceivedNextUrl(url: String) {
                super.onReceivedNextUrl(url)
                moreUrl = url
            }

            override fun onReceivedItems(results: List<Respondent>) {
                super.onReceivedItems(results)
                savedList.addAll(results)

                feedItems.addAll(results)
                baseAdapter?.notifyDataSetChanged()

                indicateAvailability()
            }

            override fun onRequestEnded() {
                super.onRequestEnded()
                progressBar.visibility = View.INVISIBLE
            }
        })

    }

    // MARK: perform search action
    private fun searchList(text: String) {
        if (text.isEmpty()) {
            populateRecyclerWithList(savedList)
            return
        }
        val result = ArrayList<Respondent>()
        savedList.forEach {
            // MARK: the search matches the name
            val textMatches = (text.equals(it.firstName, ignoreCase = true)) || (text.equals(
                it.lastName,
                ignoreCase = true
            )) || it.firstName.toUpperCase().contains(text.toUpperCase()) || it.phone.toUpperCase()
                .contains(text.toUpperCase())
            if (textMatches) {
                result.add(it)
            }
        }
        populateRecyclerWithList(result)
    }

    // MARK: add and populate list to recycler
    private fun populateRecyclerWithList(items: List<Respondent>) {
        feedItems.clear()
        baseAdapter?.notifyDataSetChanged()

        feedItems.addAll(items)
        baseAdapter?.notifyDataSetChanged()
        indicateAvailability()
        progressBar.visibility = View.INVISIBLE
    }

    private fun indicateAvailability() {
        if (feedItems.isEmpty()) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}