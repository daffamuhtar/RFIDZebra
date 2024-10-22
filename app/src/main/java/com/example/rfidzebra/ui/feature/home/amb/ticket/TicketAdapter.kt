package com.example.rfidzebra.ui.feature.home.amb.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.R

class TicketAdapter(private var ticketList: MutableList<TicketItem>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>(), Filterable {

    private var ticketListFiltered: MutableList<TicketItem> = mutableListOf()

    init {
        ticketListFiltered = ticketList
    }

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTicketNumber: TextView = view.findViewById(R.id.tv_ticket)
        val tvTicketDate: TextView = view.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_layout, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketListFiltered[position]
        holder.tvTicketNumber.text = ticket.ticketNumber
        holder.tvTicketDate.text = ticket.date
    }

    override fun getItemCount(): Int = ticketListFiltered.size

    fun updateData(newList: List<TicketItem>) {
        ticketList = newList.toMutableList()
        ticketListFiltered = newList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<TicketItem>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(ticketList)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in ticketList) {
                        if (item.ticketNumber.lowercase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ticketListFiltered = results?.values as MutableList<TicketItem>
                notifyDataSetChanged()
            }
        }
    }
}
