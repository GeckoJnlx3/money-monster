package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.monster.Monster

class MonsterpediaMonsterAdapter(private val monsterList: List<Monster>) :
    RecyclerView.Adapter<MonsterpediaMonsterAdapter.MonsterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.monster_item, parent, false)
        return MonsterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.nameTextView.text = monster.name
        holder.imageView.setImageResource(monster.image)
    }

    override fun getItemCount(): Int = monsterList.size

    inner class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.monsterName)
        val imageView: ImageView = itemView.findViewById(R.id.monsterImage)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedMonster = monsterList[position]
                    val intent = Intent(context, MonsterpediaEntryActivity::class.java).apply {
                        putExtra("monster_name", selectedMonster.name)
                        putExtra("monster_image", selectedMonster.image)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
