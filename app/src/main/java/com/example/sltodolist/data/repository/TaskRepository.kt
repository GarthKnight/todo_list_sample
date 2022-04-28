package com.example.sltodolist.data.repository

import com.example.sltodolist.data.FilterType
import com.example.sltodolist.data.Task
import io.reactivex.Completable
import io.reactivex.Single

interface TaskRepository {

    fun getTasks(filterType: FilterType): Single<List<Task>>

    fun getTask(taskId: String): Single<Task>

    fun saveTask(task: Task): Completable

    fun completeTask(taskId: String): Completable

    fun activateTask(taskId: String): Completable

    fun clearCompletedTasks(): Completable

    fun deleteAllTasks(): Completable

    fun deleteTask(taskId: String): Completable
}