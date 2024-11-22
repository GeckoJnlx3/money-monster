package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.monster.Monster

data class Species(val name: String, val monsters: List<Monster>)

class MonsterpediaSpeciesAdapter(private val speciesList: List<Species>) :
    RecyclerView.Adapter<MonsterpediaSpeciesAdapter.SpeciesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.monsterpedia_species, parent, false)
        return SpeciesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpeciesViewHolder, position: Int) {
        val species = speciesList[position]
        holder.speciesNameTextView.text = species.name

        // Set up the inner RecyclerView
        val monsterAdapter = MonsterpediaMonsterAdapter(species.monsters)
        holder.monsterRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = monsterAdapter
            setRecycledViewPool(RecyclerView.RecycledViewPool())
        }
    }

    override fun getItemCount(): Int = speciesList.size

    inner class SpeciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val speciesNameTextView: TextView = itemView.findViewById(R.id.speciesName)
        val monsterRecyclerView: RecyclerView = itemView.findViewById(R.id.monsterItemRV)

        init {
            // Prevent the parent RecyclerView from intercepting touch events
            itemView.setOnTouchListener { v, event ->
                v.parent?.requestDisallowInterceptTouchEvent(true) // Prevent parent intercept
                false
            }
        }
    }
}
