package com.shicks.lifeRoot.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

        val binding = DataBindingUtil.inflate<EditListFragmentBinding>(inflater,
        R.layout.edit_list_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = Database.getInstance(application).databaseDao

        val viewModelFactory = EditListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditListViewModel::class.java)

        binding.editListViewModel = viewModel

        val listId = EditListFragmentArgs.fromBundle(this.requireArguments()).editListId
        viewModel.editListId.value = listId
        viewModel.setList(listId)

        binding.cancelListButton.setOnClickListener { view ->
            view.findNavController().navigate(EditListFragmentDirections.actionEditListFragmentToListFragment())
        }
        binding.saveListButton.setOnClickListener { view ->
            viewModel.list.value?.let {
                it.updatedTimeMilli = System.currentTimeMillis()
                it.listName = binding.editListTitle.text.toString()
            }

            viewModel.saveOrUpdate()
            viewModel.listItems.value?.add(
                ListItem(
                    listId = viewModel.editListId.value!!,
                    itemNumber = 1,
                    itemDetails = binding.listItemDetail.text.toString()
                )
            )
            viewModel.saveItems()
            view.findNavController().navigate(EditListFragmentDirections.actionEditListFragmentToListFragment())
        }

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}