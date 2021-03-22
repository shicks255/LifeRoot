package com.shicks.lifeRoot.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import com.shicks.lifeRoot.database.daos.DatabaseDao
import com.shicks.lifeRoot.database.entities.MyList
import com.shicks.lifeRoot.database.entities.ListItem

@Database(entities = [MyList::class, ListItem::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val databaseDao: DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: com.shicks.lifeRoot.database.Database? = null

        fun getInstance(context: Context): com.shicks.lifeRoot.database.Database {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        com.shicks.lifeRoot.database.Database::class.java,
                        "my_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}