package com.shicks.lifeRoot.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shicks.lifeRoot.database.daos.DatabaseDao
import java.lang.IllegalArgumentException

class EditListViewModelFactory(
    private val dataSource: DatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditListViewModel::class.java)) {
                return EditListViewModel(dataSource, application) as T
            }

            throw IllegalArgumentException("Unknown viewModel class")
        }
}