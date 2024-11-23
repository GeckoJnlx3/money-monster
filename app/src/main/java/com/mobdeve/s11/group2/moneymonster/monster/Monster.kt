package com.mobdeve.s11.group2.moneymonster.monster

import java.sql.Date

data class Monster(
    val monsterId: Int,
    val species: String,
    val name: String,
    val image: Int,
    val adoptionDate: Date,
    val stage: String,
    val upTick: Int,
    val reqExp: Int,
    val level: Int,
    val statSaved: Int,
    val statSpent: Int,
    val description: String,
    val unlocked: Boolean,
    val onField: Boolean
)
