package com.example.sltodolist.internal.app

import com.example.sltodolist.data.repository.TaskRepository
import com.example.sltodolist.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [AppModule.BindsModule::class])
class AppModule {

    @Module
    interface BindsModule {
        @Binds
        fun provideTaskRepository(taskRepository: TaskRepositoryImpl): TaskRepository
    }

}