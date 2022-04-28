package com.example.sltodolist.presentation.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sltodolist.R
import com.example.sltodolist.data.FilterType
import com.example.sltodolist.databinding.FragmentListBinding
import com.example.sltodolist.extension.getNextDay
import com.example.sltodolist.internal.DI
import com.example.sltodolist.presentation.base.EventObserver
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {


    @Inject
    internal lateinit var viewModelProvider: Provider<ListViewModel>

    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: TaskListAdapter
    private lateinit var binding: FragmentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DI.app.provideComponent().inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        initViewModelEvents()

        viewModel.fetchTasks()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {
                viewModel.deleteCompleted()
                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> false
        }

    private fun setupListAdapter() {
        listAdapter = TaskListAdapter(viewModel)
        binding.rvTasks.adapter = listAdapter

        viewModel.tasks.observe(viewLifecycleOwner) { list ->
            val sortedList =
                list.sortedWith(compareBy({ it.isCompleted }, { it.deadline }, { it.priority }))
            listAdapter.submitList(sortedList)
        }
    }

    private fun initViewModelEvents() {
        viewModel.openTaskEvent.observe(
            viewLifecycleOwner,
            EventObserver { taskId ->
                openTask(taskId)
            }
        )
        viewModel.newTaskEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                navigateToAddTask()
            }
        )
        viewModel.commonErrorEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                Toast.makeText(context, R.string.text_common_error, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun openTask(taskId: String) {
        val action = ListFragmentDirections.actionTasksScreenToTaskDetailScreen(taskId)
        findNavController().navigate(action)
    }

    private fun navigateToAddTask() {
        val time = Calendar.getInstance().getNextDay()

        val action = ListFragmentDirections.actionTasksScreenToAddEditTaskScreen(
            null, null, null, 0, time
        )
        findNavController().navigate(action)
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.menu_filter, menu)

            setOnMenuItemClickListener {
                viewModel.setFilter(
                    when (it.itemId) {
                        R.id.active -> FilterType.ACTIVE
                        R.id.completed -> FilterType.COMPLETED
                        else -> FilterType.ALL
                    }
                )
                true
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}