package com.mobdeve.s11.group2.moneymonster

data class FinanceRecord (
    val id: Int,
    val type: String,
    val date: String,
    val currency: String,
    val amount: String?,
    val category: String,
    val description: String,
)