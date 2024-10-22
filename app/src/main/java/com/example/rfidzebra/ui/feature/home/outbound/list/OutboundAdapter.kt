package com.example.rfidzebra.ui.feature.home.outbound.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.databinding.ItemOutboundLayoutBinding


class OutboundAdapter(private val ticketList: List<OutboundItem>) :
    RecyclerView.Adapter<OutboundAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(val binding: ItemOutboundLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemOutboundLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]
        holder.binding.tvTicket.text = ticket.ticketNumber
        holder.binding.tvDateOutbound.text = ticket.dateOutbound
        holder.binding.tvQtyOutbound.text = ticket.qtyOutbound.toString()
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
}
