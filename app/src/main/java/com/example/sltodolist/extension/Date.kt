package com.example.sltodolist.extension

import java.util.*

fun Calendar.getNextDay(): Long{
    this.add(Calendar.DAY_OF_YEAR, 1)
    return this.timeInMillis
}