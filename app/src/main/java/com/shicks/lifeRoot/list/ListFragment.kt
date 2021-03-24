package com.shicks.lifeRoot.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.Database
import com.shicks.lifeRoot.databinding.ListFragmentBinding

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = Database.getInstance(application).databaseDao

        val viewModelFactory = ListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ListViewModel::class.java)

        val binding = DataBindingUtil.inflate<ListFragmentBinding>(
            inflater, R.layout.list_fragment, container, false)

        binding.listViewModel = viewModel

        val adapter = MyListItemAdapter(clickListener = {myList ->
            viewModel.setNavigateListId(myList.listId)
        })
        binding.myListListView.adapter = adapter

        viewModel.myLists.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        viewModel.navigateListId.observe(viewLifecycleOwner, Observer { id ->
            if (id != null) {
                viewModel.nullNavigateListId()
                this.findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToEditListFragment(id)
                )
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}