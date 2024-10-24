package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonsterActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter: MonsterAdapter
    private lateinit var monsterList: List<Monster>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monsterpedia_main)

        recyclerView = findViewById(R.id.monsterpediaRVG)
        recyclerView.layoutManager = GridLayoutManager(this, 3 )

        monsterList = listOf(
            Monster("Gwomp", R.drawable.gwomp),
            Monster("Mamoo", R.drawable.gwomp),
            Monster("Ylang", R.drawable.gwomp)
        )

        var gwompList = listOf(
            Monster("Gwomp", R.drawable.gwomp_baby),
            Monster("Gwompor", R.drawable.gwomp_teen),
            Monster("Wompagwoom", R.drawable.gwomp_adult)
        )

        var mamooList = listOf(
            Monster("Mamoo", R.drawable.mamoo_baby),
            Monster("Moomie", R.drawable.mamoo_teen),
            Monster("Moomama", R.drawable.mamoo_adult)
        )

        recyclerView1 = findViewById(R.id.monsterpediaRVM)
        recyclerView1.layoutManager = GridLayoutManager(this, 3 )

        recyclerView2 = findViewById(R.id.monsterpediaRVY)
        recyclerView2.layoutManager = GridLayoutManager(this, 3 )

        var adapterG = MonsterAdapter(gwompList)
        var adapterM = MonsterAdapter(mamooList)
        adapter = MonsterAdapter(monsterList)
        recyclerView.adapter = adapterG
        recyclerView1.adapter = adapterM
        recyclerView2.adapter = adapter

    }
}
