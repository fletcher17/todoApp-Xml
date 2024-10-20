package com.example.newtodoapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newtodoapp.data.models.Priority
import com.example.newtodoapp.data.models.ToDoData
import com.example.newtodoapp.data.viewModels.SharedViewModel
import com.example.newtodoapp.data.viewModels.ToDoViewModel
import com.example.newtodoapp.databinding.FragmentUpdateBinding
import com.example.newtodoapp.utils.parsePriorityType
import com.example.newtodoapp.utils.verifyDataFromUser

class UpdateFragment : Fragment(R.layout.fragment_update) {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUpdateBinding.bind(view)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> updateItem()
                    R.id.menu_delete -> confirmItemRemoval()
                    android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        binding.currentTitleEt.setText(args.currentItem.title)
        binding.descriptionEt.setText(args.currentItem.description)
        binding.currentPrioritiesSpinner.setSelection(mSharedViewModel.parsePriority(args.currentItem.priority))
        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

    }


    //Show AlertDialog to confirm removal of item
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Delete ${args.currentItem.title}?")
            setMessage("Are you sure you want to remove ${args.currentItem.title}?")
            setNegativeButton("No") { _, _ ->
            }
            setPositiveButton("Yes") { _, _ ->
                toDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: ${args.currentItem.title}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            create().show()
        }
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val getPriority = binding.currentPrioritiesSpinner.selectedItem.toString()

        val validation = verifyDataFromUser(title, description)
        if (validation) {
            // Update Current Item
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                parsePriorityType(getPriority),
                description
            )
            toDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}