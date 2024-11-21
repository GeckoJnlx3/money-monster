package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import android.util.Log

class MonsterpediaActivity : ComponentActivity() {

    private lateinit var speciesRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monsterpedia)

        databaseHelper = DatabaseHelper(this)

        val monsters = databaseHelper.getAllMonsters()

        val gwompList = monsters.filter { it.species.contains("gwomp", ignoreCase = true) }
        val mamooList = monsters.filter { it.species.contains("mamoo", ignoreCase = true) }
        val aveList = monsters.filter { it.species.contains("ave", ignoreCase = true) }

        val speciesList = listOf(
            Species("Gwomp", gwompList),
            Species("Mamoo", mamooList),
            Species("Ave", aveList)
        )

        speciesRecyclerView = findViewById(R.id.speciesRV)
        speciesRecyclerView.layoutManager = LinearLayoutManager(this)
        speciesRecyclerView.adapter = MonsterpediaSpeciesAdapter(speciesList)
    }
}
