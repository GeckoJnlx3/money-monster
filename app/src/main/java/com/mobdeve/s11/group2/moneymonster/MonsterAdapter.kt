package com.mobdeve.s11.group2.moneymonster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MonsterAdapter(private val monsterList: List<Monster>) : RecyclerView.Adapter<MonsterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.monsterpediaSquare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.monster_square, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.imageView.setImageResource(monster.imageResource)
    }

    override fun getItemCount(): Int {
        return monsterList.size
    }
}
