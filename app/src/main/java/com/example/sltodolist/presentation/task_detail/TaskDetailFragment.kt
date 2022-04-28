package com.example.sltodolist.presentation.task_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sltodolist.R
import com.example.sltodolist.data.Task
import com.example.sltodolist.databinding.FragmentTaskDetailBinding
import com.example.sltodolist.internal.DI
import com.example.sltodolist.presentation.base.EventObserver
import javax.inject.Inject
import javax.inject.Provider

class TaskDetailFragment : Fragment() {

    private val args: TaskDetailFragmentArgs by navArgs()

    @Inject
    internal lateinit var viewModelProvider: Provider<TaskDetailViewModel>

    private lateinit var viewModel: TaskDetailViewModel
    private lateinit var binding: FragmentTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DI.app.provideComponent().inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelEvents()

        viewModel.getTask(args.taskId)

        viewModel.task.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.title
            binding.tvDescription.text = it.description
            binding.tvPriority.text = resources.getStringArray(R.array.priorities)[it.priority]
            binding.tvDeadline.text = it.getHumanReadableDeadline()
        }
    }

    private fun initViewModelEvents() {
        viewModel.editTaskEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                it?.let { task -> navigateToEditTask(task) }
            }
        )
        viewModel.deleteTaskEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().popBackStack()
            }
        )
    }

    private fun navigateToEditTask(task: Task) {
        val action = TaskDetailFragmentDirections.actionTaskDetailScreenToAddEditTaskScreen(
            taskId = task.id,
            title = task.title,
            description = task.description,
            deadline = task.deadline,
            priority = task.priority
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}