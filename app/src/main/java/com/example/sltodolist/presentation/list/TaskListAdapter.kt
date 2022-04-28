package com.example.sltodolist.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sltodolist.R
import com.example.sltodolist.data.Task
import com.example.sltodolist.databinding.ItemTaskBinding
import java.util.*

class TaskListAdapter(private val viewModel: ListViewModel) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ListViewModel, item: Task) {
            val context = binding.root.context

            binding.viewmodel = viewModel
            binding.task = item

            binding.tvPriority.text = context.resources.getStringArray(R.array.priorities)[item.priority]

            if(item.deadline < Calendar.getInstance().timeInMillis){
                    binding.root.setBackgroundColor(context.resources.getColor(R.color.red_transparent))
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}