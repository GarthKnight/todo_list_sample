package com.example.sltodolist.internal.app

import android.content.Context
import androidx.room.Room
import com.example.sltodolist.data.local.CommonDatabase
import com.example.sltodolist.data.local.TaskDao
import dagger.Module
import dagger.Provides

@Module
class RoomModule(val context: Context) {

    @Provides
    @AppScope
    fun providesDatabase(): CommonDatabase {
        return Room
            .databaseBuilder(context, CommonDatabase::class.java, "common-database")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @AppScope
    fun providesCitiesDao(roomDatabase: CommonDatabase): TaskDao {
        return roomDatabase.taskDao()
    }

}