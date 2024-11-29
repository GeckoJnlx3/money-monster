package com.mobdeve.s11.group2.moneymonster.monster

data class MonsterData(
    val level: Int,
    val levelProgress: Int,
    val maxLevelProgress: Int,
    val name: String,
    val moneySaved: Float,
    val moneySpent: Float,
    val adoptedOn: String,
    val imageResId: Int
)