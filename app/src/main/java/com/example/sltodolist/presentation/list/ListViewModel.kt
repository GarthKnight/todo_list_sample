package com.example.sltodolist.presentation.list

import androidx.lifecycle.*
import com.example.sltodolist.data.FilterType
import com.example.sltodolist.data.Task
import com.example.sltodolist.data.repository.TaskRepository
import com.example.sltodolist.extension.schedulersIoToMain
import com.example.sltodolist.presentation.base.BaseViewModel
import com.example.sltodolist.presentation.base.Event
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    val openTaskEvent = MutableLiveData<Event<String>>()
    val newTaskEvent = MutableLiveData<Event<Unit>>()

    val tasks = MutableLiveData<List<Task>>()

    fun fetchTasks() {
        taskRepository.getTasks(FilterType.ALL)
            .schedulersIoToMain()
            .subscribe(
                {
                    tasks.value = it
                },
                {
                    Timber.tag("TASKS_FETCH").d("fetch failed")
                    commonErrorEvent.value = Event(Unit)
                })
            .disposeOnCleared()
    }

    fun completeTask(taskId: String, completed: Boolean) {
        val completeSingle = if (completed) {
            taskRepository.completeTask(taskId)
        } else {
            taskRepository.activateTask(taskId)
        }

        completeSingle.schedulersIoToMain()
            .subscribe(
                {
                    Timber.tag("TASK_COMPLETE").d("success")
                },
                {
                    Timber.tag("TASK_COMPLETE").d("fail")
                    commonErrorEvent.value = Event(Unit)
                }
            )
            .disposeOnCleared()
    }

    fun openTask(taskId: String) {
        openTaskEvent.value = Event(taskId)
    }

    fun createTask() {
        newTaskEvent.value = Event(Unit)
    }

    fun deleteCompleted() {
        taskRepository.clearCompletedTasks()
            .schedulersIoToMain()
            .andThen(taskRepository.getTasks(FilterType.ALL).schedulersIoToMain())
            .subscribe(
                {
                    tasks.value = it
                },
                {
                    Timber.tag("TASKS_FETCH").d("fetch failed")
                    commonErrorEvent.value = Event(Unit)
                }
            )
            .disposeOnCleared()
    }

    fun setFilter(type: FilterType) {
        taskRepository.getTasks(type)
            .schedulersIoToMain()
            .subscribe(
                {
                    tasks.value = it
                },
                {
                    Timber.tag("TASKS_FETCH").d("fetch failed")
                    commonErrorEvent.value = Event(Unit)
                })
            .disposeOnCleared()
    }

}