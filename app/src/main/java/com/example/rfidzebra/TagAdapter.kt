package com.example.rfidzebra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.ui.TagItem
import com.zebra.rfid.api3.TagData

class TagAdapter(private val tagList: MutableList<TagItem>) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagID: TextView = view.findViewById(R.id.tagData)
        val tagProgress: ProgressBar = view.findViewById(R.id.tagProgress)
        val tagPercent: TextView = view.findViewById(R.id.tagPercent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_tag_locate_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tagItem = tagList[position]
        holder.tagID.text = tagItem.tagID
        holder.tagProgress.progress = tagItem.progress
        holder.tagPercent.text = tagItem.percent
    }

    override fun getItemCount(): Int = tagList.size

    fun updateTag(tagID: String, progress: Int, percent: String) {
        val tag = tagList.find { it.tagID == tagID }
        tag?.let {
            it.progress = progress
            it.percent = percent
            notifyDataSetChanged()
        }
    }
}