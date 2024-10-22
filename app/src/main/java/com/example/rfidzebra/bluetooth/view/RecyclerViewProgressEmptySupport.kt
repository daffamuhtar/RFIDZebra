package com.example.rfidzebra.bluetooth.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewProgressEmptySupport : RecyclerView {

    private var emptyView: View? = null

    private val emptyObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            val adapter: Adapter<*>? = adapter
            if (adapter != null && emptyView != null) {
                if (adapter.itemCount === 0) {
                    emptyView!!.visibility = View.VISIBLE
                    this@RecyclerViewProgressEmptySupport.visibility = View.GONE
                } else {
                    emptyView!!.visibility = View.GONE
                    this@RecyclerViewProgressEmptySupport.visibility = View.VISIBLE
                }
            }
        }
    }

    private var progressView: ProgressBar? = null
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    )

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    fun setProgressView(progressView: ProgressBar?) {
        this.progressView = progressView
    }

    fun startLoading() {
        // Hides the empty view.
        if (emptyView != null) {
            emptyView!!.visibility = GONE
        }
        // Shows the progress bar.
        if (progressView != null) {
            progressView!!.visibility = VISIBLE
        }
    }

    fun endLoading() {
        // Hides the progress bar.
        if (progressView != null) {
            progressView!!.visibility = GONE
        }

        // Forces the view refresh.
        emptyObserver.onChanged()
    }
}