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

        recyclerView1 = findViewById(R.id.monsterpediaRVM)
        recyclerView1.layoutManager = GridLayoutManager(this, 3 )

        recyclerView2 = findViewById(R.id.monsterpediaRVY)
        recyclerView2.layoutManager = GridLayoutManager(this, 3 )

        adapter = MonsterAdapter(monsterList)
        recyclerView.adapter = adapter
        recyclerView1.adapter = adapter
        recyclerView2.adapter = adapter

    }
}
