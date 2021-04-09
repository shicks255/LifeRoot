package com.shicks.lifeRoot.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shicks.lifeRoot.database.daos.DatabaseDao
import com.shicks.lifeRoot.database.entities.MyList
import kotlinx.coroutines.launch

class ListViewModel(
    val dataBase: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    var navigateListId = MutableLiveData<Long?>()

    fun setNavigateListId(listId: Long) {
        navigateListId.value = listId
    }

    fun nullNavigateListId() {
        navigateListId.value = null
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch {
            dataBase.deleteListItems(listId)
            dataBase.deleteList(listId)
            _myLists.value = dataBase.getMyLists()
        }
    }

    private var _myLists = MutableLiveData<List<MyList>>()
    val myLists: LiveData<List<MyList>>
        get() = _myLists

    init {
        viewModelScope.launch {
            _myLists.value = dataBase.getMyLists()
            println(_myLists.value)
        }
    }
}