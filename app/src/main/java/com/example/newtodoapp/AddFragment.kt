package com.example.newtodoapp

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.newtodoapp.data.models.Priority
import com.example.newtodoapp.data.models.ToDoData
import com.example.newtodoapp.data.viewModels.SharedViewModel
import com.example.newtodoapp.data.viewModels.ToDoViewModel
import com.example.newtodoapp.databinding.FragmentAddBinding
import com.example.newtodoapp.utils.*


class AddFragment : Fragment(R.layout.fragment_add) {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddBinding.bind(view)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_add -> insertDataToDb()
                    android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
    }


    private fun insertDataToDb() {
        val mTitle = binding.editTextTextPersonName.text.toString()
        val priority = binding.prioritiesSpinner.selectedItem.toString()
        val description = binding.descriptionEt.text.toString()

        val validation = verifyDataFromUser(mTitle, description)
        if (validation) {
            // Insert Data to Database
            val newData = ToDoData(
                0,
                mTitle,
                parsePriorityType(priority),
                description
            )
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}