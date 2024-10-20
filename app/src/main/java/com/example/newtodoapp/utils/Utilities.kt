package com.example.newtodoapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.newtodoapp.MainActivity
import com.example.newtodoapp.R
import com.example.newtodoapp.data.models.Priority

fun parsePriorityType(priority: String): Priority {
    return when (priority) {
        "High Priority" -> {
            Priority.HIGH
        }
        "Medium Priority" -> {
            Priority.MEDIUM
        }
        "Low Priority" -> {
            Priority.LOW
        }
        else -> Priority.LOW
    }
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}


fun verifyDataFromUser(title: String, description: String): Boolean {
    return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
        false
    } else !(title.isEmpty() || description.isEmpty())
}

fun forUtility(application: Application) {


    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Another interface callback
        }
    }

}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object: Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }

    })
}