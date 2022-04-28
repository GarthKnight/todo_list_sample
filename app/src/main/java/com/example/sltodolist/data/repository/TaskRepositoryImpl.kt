package com.example.sltodolist.data.repository

import com.example.sltodolist.data.FilterType
import com.example.sltodolist.data.Task
import com.example.sltodolist.data.local.TaskDao
import com.example.sltodolist.extension.schedulersIoToMain
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasks(filterType: FilterType): Single<List<Task>> {
        return when (filterType) {
            FilterType.ALL -> taskDao.getTasks()
            FilterType.COMPLETED -> taskDao.getCompletedTasks()
            FilterType.ACTIVE -> taskDao.getActiveTasks()
            else -> taskDao.getTasks()
        }
    }

    override fun getTask(taskId: String): Single<Task> {
        return taskDao.getTaskById(taskId)
    }

    override fun saveTask(task: Task): Completable {
        return taskDao.insertTask(task)
    }

    override fun completeTask(taskId: String): Completable {
        return taskDao.updateCompleted(taskId, true)
    }

    override fun activateTask(taskId: String): Completable {
        return taskDao.updateCompleted(taskId, false)
    }

    override fun clearCompletedTasks(): Completable {
        return taskDao.deleteCompletedTasks()
    }

    override fun deleteAllTasks(): Completable {
        return taskDao.deleteTasks()
    }

    override fun deleteTask(taskId: String): Completable {
        return taskDao.deleteTaskById(taskId)
    }

}