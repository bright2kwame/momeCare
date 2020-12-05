package com.momecarefoundation.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.momecarefoundation.app.R
import com.momecarefoundation.app.adapter.FeedAdapter
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.model.Respondent
import kotlinx.android.synthetic.main.fragment_respondent.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class RespondentFragment : Fragment() {
    private val data = ArrayList<Respondent>()
    private var baseAdapter: FeedAdapter? = null

    private var itemSelected = object : AdapterCallback {
        override fun onActionPerformed(item: Any, position: Int) {
            super.onActionPerformed(item, position)
        }
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_respondent, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseAdapter = FeedAdapter(data, itemSelected)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = baseAdapter

        loadsAllItems()
    }

    override fun onResume() {
        super.onResume()
        loadsAllItems()
    }

    // MARK: fetch all items
    private fun loadsAllItems() {
        // MARK: do in background for items that may be many
        doAsync {
            progressBar.visibility = View.VISIBLE
            val allItems = Respondent().all()
            data.clear()
            data.addAll(allItems)

            uiThread {
                populateRecyclerWithList(allItems)
            }
        }
    }

    //MARK: stop the progress
    private fun stopProgress() {
        progressBar?.visibility = View.INVISIBLE
        if (data.isEmpty()) {
            textView?.visibility = View.VISIBLE
            textView.text = "All records submitted"
        } else {
            textView?.visibility = View.GONE
        }
    }

    // MARK: add and populate list to recycler
    private fun populateRecyclerWithList(items: List<Respondent>) {
        data.clear()
        baseAdapter?.notifyDataSetChanged()
        data.addAll(items)
        baseAdapter?.notifyDataSetChanged()
        stopProgress()
    }

}