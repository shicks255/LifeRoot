package com.shicks.lifeRoot.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shicks.lifeRoot.database.entities.MyList
import com.shicks.lifeRoot.database.entities.ListItem

@Dao
interface DatabaseDao {

    @Insert
    suspend fun insertList(myList: MyList): Long

    @Insert
    suspend fun insertListItem(listItem: ListItem)

    @Update
    suspend fun updateList(myList: MyList)

    @Update
    suspend fun updateListItem(listItem: ListItem)

    @Query("DELETE from list_item where listItemId = :listItemId")
    suspend fun deleteListItem(listItemId: Long)

    @Query("DELETE from list_item where list_id = :listId")
    suspend fun deleteListItems(listId: Long)

    @Query("DELETE from my_list where listId = :listId")
    suspend fun deleteList(listId: Long)

    @Query("SELECT * from my_list order by updated_time desc")
    suspend fun getMyLists(): List<MyList>

    @Query("SELECT * from list_item where list_id = :listId order by completed_time asc")
    suspend fun getMyListItems(listId: Long): List<ListItem>

    @Query("SELECT * from my_list where listId = :listId")
    suspend fun getMyList(listId: Long): MyList
}