package com.example.newtodoapp.data.viewModels

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newtodoapp.R
import com.example.newtodoapp.data.models.Priority
import com.example.newtodoapp.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty()
    }


    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView?)?.setTextColor(ContextCompat.getColor(application, R.color.red))
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView?)?.setTextColor(ContextCompat.getColor(application, R.color.yellow))
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView?)?.setTextColor(ContextCompat.getColor(application, R.color.green))
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Another interface callback
        }
    }


     fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> {
                0
            }
            Priority.MEDIUM -> {
                1
            }
            Priority.LOW -> {
                2
            }
        }
    }
}