package com.example.rfidzebra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zebra.rfid.api3.TagData

class TagAdapter(private val tagDataList: MutableList<TagData>, private val tagCountMap: MutableMap<String, Int>) :
    RecyclerView.Adapter<TagAdapter.TagDataViewHolder>() {

    inner class TagDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagID: TextView = itemView.findViewById(R.id.tvTagID)
        val peakRSSI: TextView = itemView.findViewById(R.id.tvPeakRSSI)
        val antennaID: TextView = itemView.findViewById(R.id.tvAntennaID)
        val crc: TextView = itemView.findViewById(R.id.tvCRC)
        val tagSeenCount: TextView = itemView.findViewById(R.id.tvTagSeenCount)
        val phaseInfo: TextView = itemView.findViewById(R.id.tvPhaseInfo)
        val channelIndex: TextView = itemView.findViewById(R.id.tvChannelIndex)
        val memoryBankData: TextView = itemView.findViewById(R.id.tvMemoryBankData)
        val tid: TextView = itemView.findViewById(R.id.tvTID)
        val userData: TextView = itemView.findViewById(R.id.tvUserData)
        val tagIDCount: TextView = itemView.findViewById(R.id.tvTagIDCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tagid, parent, false)
        return TagDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagDataViewHolder, position: Int) {
        val data = tagDataList[position]
        holder.tagID.text = "Tag ID: ${data.tagID}"
        holder.peakRSSI.text = "Peak RSSI: ${data.peakRSSI}"
        holder.antennaID.text = "Antenna ID: ${data.antennaID}"
        holder.crc.text = "CRC: ${data.crc}"
        holder.tagSeenCount.text = "Tag Seen Count: ${data.tagSeenCount}"
        holder.phaseInfo.text = "Phase Info: ${data.phase}"
        holder.channelIndex.text = "Channel Index: ${data.channelIndex}"
        holder.memoryBankData.text = "Memory Bank Data: ${data.memoryBankData}"
        holder.tid.text = "TID: ${data.tid}"
        holder.userData.text = "User Data: ${data.user}"

        if (data.isContainsLocationInfo) {

            holder.tagIDCount.text = " Count : ${tagCountMap[data.tagID].toString()}\n\n Distance : ${data.LocationInfo.relativeDistance.toString()}"

        }else{
            holder.tagIDCount.text = tagCountMap[data.tagID].toString()

        }

    }

    override fun getItemCount(): Int = tagDataList.size
}
