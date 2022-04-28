package com.example.sltodolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sltodolist.data.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class CommonDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}