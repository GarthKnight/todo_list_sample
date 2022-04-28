package com.example.sltodolist.presentation.task_detail

import androidx.lifecycle.MutableLiveData
import com.example.sltodolist.data.Task
import com.example.sltodolist.data.repository.TaskRepository
import com.example.sltodolist.extension.schedulersIoToMain
import com.example.sltodolist.presentation.base.BaseViewModel
import com.example.sltodolist.presentation.base.Event
import timber.log.Timber
import javax.inject.Inject

class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    private var currentTaskId: String? = null

    val editTaskEvent = MutableLiveData<Event<Task?>>()
    val deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val task = MutableLiveData<Task>()

    fun getTask(taskId: String) {
        currentTaskId = taskId

        taskRepository.getTask(taskId)
            .schedulersIoToMain()
            .subscribe(
                {
                    task.value = it
                },
                {
                    Timber.d(it)
                }
            ).disposeOnCleared()
    }

    fun editTask() {
        editTaskEvent.value = Event(task.value)
    }

    fun deleteTask() {
        currentTaskId?.let {
            taskRepository.deleteTask(it)
                .schedulersIoToMain()
                .subscribe(
                    {
                        deleteTaskEvent.value = Event(Unit)
                    },
                    { t ->
                        Timber.tag("TASK_DELETE").d(t)
                    }
                )
                .disposeOnCleared()
        }
    }

}