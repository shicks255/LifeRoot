package com.shicks.lifeRoot.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_item")
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    var listItemId: Long = 0L,

    @ColumnInfo(name = "list_id")
    val listId: Long,

    @ColumnInfo(name = "created_time")
    val createdTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_time")
    val updatedTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "item_number")
    val itemNumber: Int,

    @ColumnInfo(name = "item_details")
    val itemDetails: String

)