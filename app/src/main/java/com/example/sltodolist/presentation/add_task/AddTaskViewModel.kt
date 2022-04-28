package com.example.sltodolist.presentation.add_task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sltodolist.data.Task
import com.example.sltodolist.data.repository.TaskRepository
import com.example.sltodolist.extension.schedulersIoToMain
import com.example.sltodolist.presentation.base.BaseViewModel
import com.example.sltodolist.presentation.base.Event
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    val taskCreateEvent = MutableLiveData<Event<Unit>>()

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    var priority = 0
    var deadline = -1L

    var taskId: String? = null

    fun saveTask() {

        val currentTitle = title.value
        val currentDescription = description.value

        if (currentTitle.isNullOrEmpty() || currentDescription.isNullOrEmpty()) {
            Timber.tag("SAVING_TASK").d("can't create task")
            return
        }

        val currentTaskId = taskId
        if (currentTaskId == null) {
            val task = Task(
                id = UUID.randomUUID().toString(),
                title = currentTitle,
                description = currentDescription,
                isCompleted = false,
                deadline = deadline,
                priority = priority
            )
            createTask(task)
        } else {
            val task = Task(
                id = currentTaskId,
                title = currentTitle,
                description = currentDescription,
                isCompleted = false,
                deadline = deadline,
                priority = priority
            )
            updateTask(task)
        }
    }

    private fun createTask(task: Task) {
        taskRepository.saveTask(task)
            .schedulersIoToMain()
            .subscribe(
                {
                    Timber.tag("TASK_CREATION").d("Task successfully created")
                    taskCreateEvent.value = Event(Unit)
                },
                {
                    Timber.tag("TASK_CREATION").d(it)
                }
            ).disposeOnCleared()
    }

    private fun updateTask(task: Task) {
        taskRepository.saveTask(task)
            .schedulersIoToMain()
            .subscribe(
                {
                    Timber.tag("TASK_SAVING").d("Task successfully created")
                    taskCreateEvent.value = Event(Unit)
                },
                {
                    Timber.tag("TASK_SAVING").d(it)
                }
            ).disposeOnCleared()
    }

}