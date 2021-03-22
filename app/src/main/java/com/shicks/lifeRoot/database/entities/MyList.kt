package com.shicks.lifeRoot.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "my_list")
data class MyList(
    @PrimaryKey(autoGenerate = true)
    var listId: Long = 0L,

    @ColumnInfo(name = "created_time")
    val createdTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_time")
    var updatedTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "list_name")
    var listName: String
)