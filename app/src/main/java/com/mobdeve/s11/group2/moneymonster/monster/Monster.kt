package com.mobdeve.s11.group2.moneymonster.monster

import java.sql.Date

data class Monster(
    val monsterId: Int,
    val species: String,
    var name: String,
    var image: Int,
    val adoptionDate: Date,
    var stage: String,
    var upTick: Int,
    val reqExp: Int,
    var level: Int,
    var statSaved: Double,
    var statSpent: Double,
    val description: String,
    var unlocked: Boolean,
    val onField: Boolean
)
