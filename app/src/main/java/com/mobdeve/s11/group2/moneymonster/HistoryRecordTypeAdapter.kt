package com.mobdeve.s11.group2.moneymonster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryRecordTypeAdapter(
    private val groupedByType: Map<String, List<FinanceRecord>>,
    private val onItemClick: (FinanceRecord) -> Unit
) : RecyclerView.Adapter<HistoryRecordTypeAdapter.TypeViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.type)
        val recordRecyclerView: RecyclerView = itemView.findViewById(R.id.historyTypeRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_record_type, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = groupedByType.keys.elementAt(position)
        val recordsForType = groupedByType[type] ?: emptyList()

        holder.typeTextView.text = type // Set the type (Income/Expense)

        holder.recordRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
            setRecycledViewPool(sharedPool)
        }

        holder.recordRecyclerView.adapter = HistoryRecordAdapter(recordsForType, onItemClick)
    }

    override fun getItemCount(): Int = groupedByType.size
}