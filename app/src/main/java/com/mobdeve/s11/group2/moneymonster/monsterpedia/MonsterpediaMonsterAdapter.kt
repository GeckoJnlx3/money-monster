package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
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
        if (monster.unlocked) {
            holder.imageView.clearColorFilter()
        } else {
            holder.imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            holder.itemView.isClickable = false
        }

        holder.itemView.setOnClickListener {
            if (monster.unlocked) {
                val intent = Intent(holder.itemView.context, MonsterpediaEntryActivity::class.java).apply {
                    putExtra("MONSTER_ID", monster.monsterId)
                }
                holder.itemView.context.startActivity(intent)
            } else {
                val nextMonsterName = getNextMonsterName(holder.itemView, monster.monsterId)
                Toast.makeText(
                    holder.itemView.context,
                    "Level up $nextMonsterName first to record it in the Monsterpedia!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = monsterList.size

    private fun getNextMonsterName(view: View, currentMonsterId: Int): String {
        val dbHelper = DatabaseHelper(view.context)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT ${DatabaseHelper.COL_NAME} FROM ${DatabaseHelper.MONSTER_TABLE_NAME} WHERE ${DatabaseHelper.COL_MONSTER_ID} = ?",
            arrayOf((currentMonsterId - 1).toString())
        )

        val nextMonsterName = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))
        } else {
            "monster"
        }

        cursor.close()
        db.close()

        return nextMonsterName
    }

    inner class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.monsterName)
        val imageView: ImageView = itemView.findViewById(R.id.monsterImage)
    }
}
