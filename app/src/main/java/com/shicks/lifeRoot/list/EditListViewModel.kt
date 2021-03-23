package com.shicks.lifeRoot.list

import android.app.Application
import androidx.lifecycle.*
import com.shicks.lifeRoot.database.daos.DatabaseDao
import com.shicks.lifeRoot.database.entities.ListItem
import com.shicks.lifeRoot.database.entities.MyList
import kotlinx.coroutines.launch

class EditListViewModel(
    val dataBase: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    var editListId = MutableLiveData<Long>()
    var list = MutableLiveData<MyList>()

    private var _listItems = MutableLiveData<List<ListItem>>()
    val listItems: LiveData<List<ListItem>>
        get() = _listItems

    fun saveOrUpdate() {
        if (list.value != null) {
            if (list.value!!.listId == 0L) {
                viewModelScope.launch {
                    dataBase.insertList(list.value!!)
                }
            } else if (list.value!!.listId > 0L) {
                viewModelScope.launch {
                    dataBase.updateList(list.value!!)
                }
            }
        }
    }

    fun saveItems() {
        viewModelScope.launch {
//            listItems.value?.forEach {
//                dataBase.insertListItem(it)
//            }
        }
    }

    fun setList(listId: Long) {
        if (listId == 0L) {
            val newList = MyList(
                listName = ""
            )
            list.value = newList
        } else if (listId > 0L) {
            viewModelScope.launch {
                val editingList = dataBase.getMyList(listId)
                list.value = editingList
            }
        }
    }

}