package com.example.sltodolist.data.local

import androidx.room.*
import com.example.sltodolist.data.Task
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TaskDao {

    @Query("SELECT * FROM Tasks")
    fun getTasks(): Single<List<Task>>

    @Query("SELECT * FROM Tasks WHERE completed = 1")
    fun getCompletedTasks(): Single<List<Task>>

    @Query("SELECT * FROM Tasks WHERE completed = 0")
    fun getActiveTasks(): Single<List<Task>>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Single<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task): Completable

    @Update
    fun updateTask(task: Task): Completable

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean): Completable

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    fun deleteTaskById(taskId: String): Completable

    @Query("DELETE FROM Tasks")
    fun deleteTasks(): Completable

    @Query("DELETE FROM Tasks WHERE completed = 1")
    fun deleteCompletedTasks(): Completable
}
