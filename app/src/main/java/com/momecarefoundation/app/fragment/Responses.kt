package com.momecarefoundation.app.fragment


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.momecarefoundation.app.R
import com.momecarefoundation.app.adapter.FeedAdapter
import com.momecarefoundation.app.callback.AdapterCallback
import com.momecarefoundation.app.model.Response
import kotlinx.android.synthetic.main.fragment_response.*
import kotlinx.android.synthetic.main.fragment_response.progressBar
import kotlinx.android.synthetic.main.fragment_response.recyclerView
import kotlinx.android.synthetic.main.fragment_response.textView
import kotlinx.android.synthetic.main.toolbar_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class Responses : AppCompatActivity() {

    private val data = ArrayList<Response>()
    private var baseAdapter: FeedAdapter? = null

    companion object {
        const val tag = "RESPONDS"
    }

    private var itemSelected = object : AdapterCallback {
        override fun onActionPerformed(item: Any, position: Int) {
            super.onActionPerformed(item, position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_response)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = tag


        baseAdapter = FeedAdapter(data, itemSelected)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
            val allItems = Response().all()
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
    private fun populateRecyclerWithList(items: List<Response>) {
        data.clear()
        baseAdapter?.notifyDataSetChanged()
        data.addAll(items)
        baseAdapter?.notifyDataSetChanged()
        stopProgress()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}