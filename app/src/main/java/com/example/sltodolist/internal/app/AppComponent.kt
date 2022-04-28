package com.example.sltodolist.internal.app

import com.example.sltodolist.TdlApplication
import com.example.sltodolist.presentation.add_task.AddTaskFragment
import com.example.sltodolist.presentation.list.ListFragment
import com.example.sltodolist.presentation.task_detail.TaskDetailFragment
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, RoomModule::class])
interface AppComponent {

    fun inject(entry: TdlApplication)

    fun inject(entry: ListFragment)
    fun inject(entry: AddTaskFragment)
    fun inject(entry: TaskDetailFragment)

}