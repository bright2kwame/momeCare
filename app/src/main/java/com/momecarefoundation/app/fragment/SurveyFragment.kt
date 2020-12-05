package com.momecarefoundation.app.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.momecarefoundation.app.R
import com.momecarefoundation.app.TakeSurvey
import com.momecarefoundation.app.adapter.FeedAdapter
import com.momecarefoundation.app.adapter.GridSpacingItemDecoration
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.callback.SurveyCallback
import com.momecarefoundation.app.model.Survey
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SurveyFragment : Fragment() {

    private val data = ArrayList<Survey>()
    private var baseAdapter: FeedAdapter? = null
    private var apiUrl = APICall.baseUrl.plus("surveys/filter_surveys/")

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private var itemSelected = object : AdapterCallback {
        override fun onActionPerformed(item: Any, position: Int) {
            super.onActionPerformed(item, position)

            val survey = item as Survey
            val takeSurveyIntent = Intent(requireContext(), TakeSurvey::class.java)
            takeSurveyIntent.putExtra(TakeSurvey.survey, survey.toString())
            requireContext().startActivity(takeSurveyIntent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        baseAdapter = FeedAdapter(data, itemSelected)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = baseAdapter


        loadsAllItems(true)
    }

    // MARK: fetch all products
    private fun loadsAllItems(isForce: Boolean) {
        // MARK: do in background for products that may be many
        doAsync {
            val allItems = Survey().all()
            data.clear()
            data.addAll(allItems)

            uiThread {
                populateRecyclerWithList(allItems)
                // MARK: sync the users products
                if (isForce) {
                    showProgress()
                    fetchData(apiUrl)
                }
            }
        }
    }

    //MARK: fetch the data from the server
    private fun fetchData(url: String) {
        progressBar?.visibility = View.VISIBLE
        APICall(requireContext()).surveys(url, null, itemInterface)
    }

    //MARK: show the progress
    private fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    //MARK: stop the progress
    private fun stopProgress() {
        progressBar?.visibility = View.INVISIBLE
        if (data.isEmpty()) {
            textView?.visibility = View.VISIBLE
        } else {
            textView?.visibility = View.GONE
        }
    }

    // MARK: add and populate list to recycler
    private fun populateRecyclerWithList(items: List<Survey>) {
        data.clear()
        baseAdapter?.notifyDataSetChanged()
        data.addAll(items)
        baseAdapter?.notifyDataSetChanged()
        stopProgress()
    }

    private val itemInterface = object : SurveyCallback {

        override fun onReceivedError(error: String) {
            cleanRecordsAndIndicateFailure()
        }

        override fun onReceivedNextUrl(url: String) {
            if (url.isNotEmpty()) {
                fetchData(url)
            } else {
                manipulateOldAndNewProducts()
            }
        }

        override fun onReceivedDetail(message: String) {
            cleanRecordsAndIndicateFailure()
        }

        override fun onReceivedItems(results: List<Survey>) {
            super.onReceivedItems(results)
            data.addAll(results)
        }
    }

    // MARK: clean all records and show status when product sync failed
    private fun cleanRecordsAndIndicateFailure() {
        progressBar.visibility = View.INVISIBLE
        this.data.clear()
        Toast.makeText(requireContext(), "Failed to refresh list", Toast.LENGTH_LONG).show()
    }

    // MARK: delete all old products and restore with new products
    private fun manipulateOldAndNewProducts() {
        doAsync {
            Survey().clearAll()
            Survey().saveAll(data)
            uiThread {
                loadsAllItems(false)
                Toast.makeText(
                    requireContext(),
                    "Synced all items",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}