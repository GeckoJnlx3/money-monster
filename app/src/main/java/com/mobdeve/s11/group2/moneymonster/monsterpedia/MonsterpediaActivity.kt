package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.monster.Monster

class MonsterpediaActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monsterpedia_main)

        recyclerView = findViewById(R.id.monsterpediaRVG)
        recyclerView.layoutManager = GridLayoutManager(this, 3 )

//        var gwompList = listOf(
//            Monster("Gwomp", R.drawable.gwomp_baby),
//            Monster("Gwompor", R.drawable.gwomp_teen),
//            Monster("Wompagwom", R.drawable.gwomp_adult)
//        )
//
//        var mamooList = listOf(
//            Monster("Mamoo", R.drawable.mamoo_baby),
//            Monster("Moomie", R.drawable.mamoo_teen),
//            Monster("Mamoolah", R.drawable.mamoo_adult)
//        )
//
//        var aveList = listOf(
//            Monster("Ave", R.drawable.ave_baby),
//            Monster("Evale", R.drawable.ave_teen),
//            Monster("Alvirose", R.drawable.ave_adult)
//        )

        recyclerView1 = findViewById(R.id.monsterpediaRVM)
        recyclerView1.layoutManager = GridLayoutManager(this, 3 )

        recyclerView2 = findViewById(R.id.monsterpediaRVA)
        recyclerView2.layoutManager = GridLayoutManager(this, 3 )

//        var adapterG = MonsterpediaAdapter(gwompList)
//        var adapterM = MonsterpediaAdapter(mamooList)
//        var adapterA = MonsterpediaAdapter(aveList)
//        recyclerView.adapter = adapterG
//        recyclerView1.adapter = adapterM
//        recyclerView2.adapter = adapterA

    }
}
