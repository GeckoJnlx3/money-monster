package com.mobdeve.s11.group2.moneymonster.monster

import android.R.id
import java.sql.Date

data class Monster(
    val monsterId: id,
    val image: Int,
    val name: String,
    val adoptionDate: Date,
    val stats: Int,
    val description: String,
)
