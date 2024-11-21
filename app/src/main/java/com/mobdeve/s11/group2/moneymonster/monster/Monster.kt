package com.mobdeve.s11.group2.moneymonster.monster

import android.R.id
import java.sql.Date

data class Monster(
    val monsterId: id,
    val name: String,
    val image: Int,
    val adoptionDate: Date,
    val statSaved: Int,
    val statSpent: Int,
    val description: String,
)
