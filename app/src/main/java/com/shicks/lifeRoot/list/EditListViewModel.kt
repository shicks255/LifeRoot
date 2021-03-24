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

    var list = MutableLiveData<MyList>()

    private var _listItems = MutableLiveData<List<ListItem>>()
    val listItems: LiveData<List<ListItem>>
        get() = _listItems

    fun saveOrUpdateList(name: String) {
        if (list.value != null) {
            list.value?.updatedTimeMilli = System.currentTimeMillis()
            list.value?.listName = name

            //this is a new list
            if (list.value!!.listId == 0L) {
                viewModelScope.launch {
                    dataBase.insertList(list.value!!)
                }
            } else {
                viewModelScope.launch {
                    dataBase.updateList(list.value!!)
                }
            }
        }
    }

    fun saveItems(itemDetails: String) {

        val newItem = ListItem(
            listId = list.value!!.listId,
            itemNumber = 1,
            itemDetails = itemDetails,
            createdTimeMilli = System.currentTimeMillis(),
            updatedTimeMilli = System.currentTimeMillis()
        )

        viewModelScope.launch {
            dataBase.insertListItem(newItem)
            _listItems.value = dataBase.getMyListItems(list.value!!.listId)
        }
    }

    fun setListAndItems(listId: Long) {
        if (listId == 0L) {
            val newList = MyList(
                listName = ""
            )
            list.value = newList
        } else if (listId > 0L) {
            viewModelScope.launch {
                val editingList = dataBase.getMyList(listId)
                list.value = editingList

                _listItems.value = dataBase
                    .getMyListItems(listId)
            }
        }
    }
}