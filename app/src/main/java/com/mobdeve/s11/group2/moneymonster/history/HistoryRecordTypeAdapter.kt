package com.mobdeve.s11.group2.moneymonster.history

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.finance.FormatUtils
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.SettingsActivity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryRecordTypeAdapter(
    private val groupedByDate: Map<java.util.Date, List<FinanceRecord>>,
    private val onItemClick: (FinanceRecord) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<HistoryRecordTypeAdapter.TypeViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val balanceTextView: TextView = itemView.findViewById(R.id.balance)
        val recordRecyclerView: RecyclerView = itemView.findViewById(R.id.historyTypeRecycler)
        val balanceArrow: ImageView = itemView.findViewById(R.id.balanceArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_record_type, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val date = groupedByDate.keys.elementAt(position)
        val recordsForDate = groupedByDate[date] ?: emptyList()

        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        holder.balanceTextView.text = formattedDate

        val groupedByType = recordsForDate.groupBy { it.type }

        val totalExpense = groupedByType["Expense"]?.sumOf { it.amount?.toDoubleOrNull() ?: 0.00 } ?: 0.00
        val totalIncome = groupedByType["Income"]?.sumOf { it.amount?.toDoubleOrNull() ?: 0.00 } ?: 0.00

        val balance = totalIncome - totalExpense
        val currency = loadCurrency()
        holder.balanceTextView.text = FormatUtils.formatAmount(balance, currency)

        when {
            balance > 0 -> {
                holder.balanceArrow.setImageResource(R.drawable.up_arrow) // Ensure up_arrow exists in res/drawable
                holder.balanceArrow.visibility = View.VISIBLE
            }
            balance < 0 -> {
                holder.balanceArrow.setImageResource(R.drawable.down_arrow) // Ensure down_arrow exists in res/drawable
                holder.balanceArrow.visibility = View.VISIBLE
            }
            else -> {
                holder.balanceArrow.visibility = View.GONE
            }
        }

        holder.recordRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
            adapter = HistoryRecordAdapter(recordsForDate, onItemClick, loadCurrency())
            setRecycledViewPool(sharedPool)
        }
    }
    override fun getItemCount(): Int = groupedByDate.size

    private fun loadCurrency() : String{
        val sharedPref: SharedPreferences = context.getSharedPreferences(SettingsActivity.PREFERENCE_FILE, Context.MODE_PRIVATE)
        return sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"
    }
}