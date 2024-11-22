package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.databinding.MonsterItemBinding
import com.mobdeve.s11.group2.moneymonster.monster.Monster

class MonsterpediaAdapter(private val monsterList: List<Monster>) :
    RecyclerView.Adapter<MonsterpediaAdapter.MonsterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val binding = MonsterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonsterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.binding.monsterName.text = monster.name
        holder.binding.monsterImage.setImageResource(monster.image)
    }

    override fun getItemCount(): Int = monsterList.size

    inner class MonsterViewHolder(val binding: MonsterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val context = it.context
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedMonster = monsterList[position]
                    val intent = Intent(context, MonsterpediaEntryActivity::class.java).apply {
                        putExtra("monster_name", selectedMonster.name)
                        putExtra("monster_image", selectedMonster.image)
                        putExtra("monster_description", selectedMonster.description)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
