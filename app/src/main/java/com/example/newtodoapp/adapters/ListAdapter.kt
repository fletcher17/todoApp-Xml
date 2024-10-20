package com.example.newtodoapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newtodoapp.ListFragmentDirections
import com.example.newtodoapp.R
import com.example.newtodoapp.data.models.Priority
import com.example.newtodoapp.data.models.ToDoData
import com.example.newtodoapp.data.viewModels.ToDoDiffUtil
import com.example.newtodoapp.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }


    inner class ListViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoData: ToDoData) {

            binding.apply {
                titleTextView.text = toDoData.title
                descriptionTextView.text = toDoData.description

                when(toDoData.priority) {
                    Priority.HIGH -> {
                        priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red))
                    }
                    Priority.MEDIUM -> {
                        priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.yellow))
                    }
                    Priority.LOW -> {
                        priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
                    }
                }

                rowBackground.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoData)
                    itemView.findNavController().navigate(action)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}