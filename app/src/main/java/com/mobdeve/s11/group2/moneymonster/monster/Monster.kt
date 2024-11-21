package com.mobdeve.s11.group2.moneymonster.monster

import java.util.Date

data class Monster(
    val monsterId: Int,
    val species: String,
    val name: String,
    val image: Int,
    val adoptionDate: Date?,
    val level: Int = 1,
    val statSaved: Int = 0,
    val statSpent: Int = 0,
    val description: String,
    val unlocked: Boolean
)
