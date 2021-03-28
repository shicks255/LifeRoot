package com.shicks.lifeRoot.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shicks.lifeRoot.HomeFragmentDirections
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.Database
import com.shicks.lifeRoot.database.entities.ListItem
import com.shicks.lifeRoot.database.entities.MyList
import com.shicks.lifeRoot.databinding.EditListFragmentBinding

class EditListFragment : Fragment() {

    companion object {
        fun newInstance() = EditListFragment()
    }

    private lateinit var viewModel: EditListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = Database.getInstance(application).databaseDao

        val viewModelFactory = EditListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditListViewModel::class.java)

        val binding: EditListFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.edit_list_fragment, container, false
        )
        binding.editListViewModel = viewModel

        val adapter = EditListItemAdapter()
        binding.editListListView.adapter = adapter

        val listId = EditListFragmentArgs.fromBundle(this.requireArguments()).editListId
        viewModel.setListAndItems(listId)

        viewModel.listItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setupData(it)
            }
        })

        viewModel.list.observe(viewLifecycleOwner, Observer {
            binding.editListTitle.setText(it.listName)
        })

        binding.cancelListButton.setOnClickListener { view ->
            view.findNavController().navigate(EditListFragmentDirections.actionEditListFragmentToListFragment())
        }
        binding.saveListButton.setOnClickListener { view ->
            adapter.data.forEach {
                println(it.toString())
            }
            val listName = binding.editListTitle.text.toString()
            val items = adapter.data
            viewModel.saveListAndItems(listName, items);
            Toast.makeText(this.context, "List Updated", Toast.LENGTH_SHORT)
                .show()
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}