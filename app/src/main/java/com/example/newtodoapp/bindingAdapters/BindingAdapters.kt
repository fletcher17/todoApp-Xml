package com.example.newtodoapp.bindingAdapters

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.newtodoapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {

//        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_updateFragment)
                }
            }
        }

        //@BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when(emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
                else -> {
                    view.visibility = View.INVISIBLE
                }
            }
        }

    }
}