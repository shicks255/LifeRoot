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

    fun saveListAndItems(listName: String, items: List<ListItemWithChange>) {
        if (list.value != null) {
            list.value?.updatedTimeMilli = System.currentTimeMillis()
            list.value?.listName = listName

            //this is a new list
            viewModelScope.launch {
                if (list.value!!.listId == 0L) {
                    val newListId = dataBase.insertList(list.value!!)
                    list.value!!.listId = newListId
                    handleListUpdates(items)
                } else {
                    dataBase.updateList(list.value!!)
                    handleListUpdates(items)
                }
            }
        }
    }

    private suspend fun handleListUpdates(items: List<ListItemWithChange>) {
        items.forEach {
            if (it.item.itemDetails != it.value && it.value.isNotEmpty()) {
                val newValue = it.item.copy(
                    updatedTimeMilli = System.currentTimeMillis(),
                    itemDetails = it.value
                )
                if (newValue.listItemId == 0L)
                    dataBase.insertListItem(newValue)
                else
                    dataBase.updateListItem(newValue)
            }
        }
        _listItems.value = dataBase.getMyListItems(list.value!!.listId)
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