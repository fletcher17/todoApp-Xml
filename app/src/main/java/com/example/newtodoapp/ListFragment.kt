package com.example.newtodoapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newtodoapp.adapters.ListAdapter
import com.example.newtodoapp.data.models.ToDoData
import com.example.newtodoapp.data.viewModels.SharedViewModel
import com.example.newtodoapp.data.viewModels.ToDoViewModel
import com.example.newtodoapp.databinding.FragmentListBinding
import com.example.newtodoapp.list.SwipeToDelete
import com.example.newtodoapp.utils.observeOnce
import com.google.android.material.snackbar.Snackbar


class ListFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val listAdapter: ListAdapter by lazy { ListAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.scheduleLayoutAnimation()
        swipeToDelete(binding.recyclerView)

        toDoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            listAdapter.setData(data)
        }

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner) {
            showEmptyDatabaseViews(it)
        }


        return view
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = listAdapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteItem(itemToDelete)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedItem(viewHolder.itemView, itemToDelete)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoData) {
        Snackbar.make(view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_SHORT).apply {
            setAction("Undo") {
                toDoViewModel.insertData(deletedItem)
            }
            show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_delete_all -> confirmRemovalAll()
            R.id.menu_priority_high -> toDoViewModel.sortByHighPriority.observe(viewLifecycleOwner) {
                listAdapter.setData(it)
            }
            R.id.menu_priority_low -> toDoViewModel.sortByLowPriority.observe(viewLifecycleOwner) {
                listAdapter.setData(it)
            }
            android.R.id.home -> requireActivity().onBackPressedDispatcher

        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        toDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner) { list ->
            list?.let {
                listAdapter.setData(it)
            }
        }
    }


    private fun confirmRemovalAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setPositiveButton("Yes") { _, _ ->
                toDoViewModel.deleteAll()
                Toast.makeText(
                    requireContext(),
                    "Successfully removed everything!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton("No") { _, _ -> }
            setTitle("Delete everything?")
            setMessage("Are you sure you want to remove everything?")
            create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}