package com.example.sltodolist.presentation.add_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sltodolist.R
import com.example.sltodolist.databinding.FragmentAddTaskBinding
import com.example.sltodolist.extension.getNextDay
import com.example.sltodolist.internal.DI
import com.example.sltodolist.presentation.base.EventObserver
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


class AddTaskFragment : Fragment() {

    private val args: AddTaskFragmentArgs by navArgs()
    private var binding: FragmentAddTaskBinding? = null

    @Inject
    internal lateinit var viewModelProvider: Provider<AddTaskViewModel>

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        DI.app.provideComponent().inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()
        initSpinner()
        initUI()

        viewModel.taskCreateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }

    private fun initUI() {
        viewModel.taskId = args.taskId
        viewModel.title.value = args.title
        viewModel.description.value = args.description
        viewModel.priority = args.priority
        viewModel.deadline = args.deadline

        val returnFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val date = returnFormat.format(args.deadline)

        binding?.tvDeadlinePicker?.text = date
        binding?.spinnerPriority?.setSelection(args.priority)

        binding?.btnCreate?.text = if (args.taskId.isNullOrEmpty()) {
            getString(R.string.btn_create)
        } else {
            getString(R.string.btn_edit)
        }
    }

    private fun initSpinner() {
        binding?.spinnerPriority?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.priority = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //no-op
                }
            }
    }

    private fun initDatePicker() {
        val tomorrow = Calendar.getInstance().getNextDay()
        val constraintsBuilder = CalendarConstraints.Builder()
        val dateValidator: DateValidator = DateValidatorPointForward.from(Calendar.getInstance().timeInMillis)
        constraintsBuilder.setValidator(dateValidator)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(tomorrow)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val returnFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            val date = returnFormat.format(it)

            binding?.tvDeadlinePicker?.text = date
            viewModel.deadline = it
        }

        binding?.tvDeadlinePicker?.setOnClickListener {
            datePicker.show(childFragmentManager, "date_picker")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}