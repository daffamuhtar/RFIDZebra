package com.example.rfidzebra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.ui.TagItem

class CustomTagAdapter(
    private val tagDataList: MutableList<CustomTagData>,
    private val tagCountMap: MutableMap<String, Int>
) :
    RecyclerView.Adapter<CustomTagAdapter.TagDataViewHolder>() {

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
        holder.tagID.text = "Tag ID: ${data.m_sTagID}"
        holder.peakRSSI.text = "Peak RSSI: ${data.m_PeakRSSI}"
        holder.antennaID.text = "Antenna ID: ${data.m_AntennaID}"
        holder.crc.text = "CRC: ${data.m_CRC}"
        holder.tagSeenCount.text = "Tag Seen Count: ${data.m_TagSeenCount}"
        holder.phaseInfo.text = "Phase Info: ${data.m_PhaseInfo}"
        holder.channelIndex.text = "Channel Index: ${data.m_ChannelIndex}"
        holder.memoryBankData.text =
            "Memory Bank EPC = ${data.memoryBankEPC} \n" +
                    "Memory Bank User = ${data.memoryBankUser} \n" +
                    "Memory Bank TID = ${data.memoryBankTID} \n" +
                    "Memory Bank Reserved = ${data.memoryBankReserved} \n"
//        holder.tid.text = "TID: ${data.tid}"
//        holder.userData.text = "User Data: ${data.user}"

        holder.tagIDCount.text = tagCountMap[data.m_sTagID].toString()
    }

    override fun getItemCount(): Int = tagDataList.size

}
