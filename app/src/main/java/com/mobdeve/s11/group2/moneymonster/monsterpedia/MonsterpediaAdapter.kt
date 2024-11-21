package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.monster.Monster

class MonsterpediaAdapter(private val monsterList: List<Monster>) : RecyclerView.Adapter<MonsterpediaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.monsterpediaSquare)
        val textView: TextView = itemView.findViewById(R.id.monsterNameSquareTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.monster_square, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.imageView.setImageResource(monster.imageResource)
        holder.textView.text = monster.name

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MonsterpediaEntryActivity::class.java).apply {
                putExtra("monster_name", monster.name)
                putExtra("monster_image", monster.imageResource)
            }
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return monsterList.size
    }
}
