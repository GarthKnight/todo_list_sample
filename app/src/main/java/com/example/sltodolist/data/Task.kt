package com.example.sltodolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "deadline") var deadline: Long = -1L,
    @ColumnInfo(name = "priority") var priority: Int = 0,
    @ColumnInfo(name = "completed") var isCompleted: Boolean = false,
) {

    fun getHumanReadableDeadline(): String {
        val returnFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return returnFormat.format(deadline)
    }

}
