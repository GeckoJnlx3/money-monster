package com.mobdeve.s11.group2.moneymonster.monster

import java.sql.Date

data class Monster(
    val monsterId: Int,
    val species: String,
    val name: String,
    val image: Int,
    val adoptionDate: Date,
    var stage: String,
    val upTick: Int,
    val reqExp: Int,
    var level: Int,
    var statSaved: Double,
    var statSpent: Double,
    val description: String,
    val unlocked: Boolean,
    val onField: Boolean,
//    val levelUpThreshold: Int

//    val savingGoal: Double,
//    val expensesGoal: Double
)
