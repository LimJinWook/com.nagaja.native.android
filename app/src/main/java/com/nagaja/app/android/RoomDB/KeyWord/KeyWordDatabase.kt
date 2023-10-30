package com.nagaja.app.android.RoomDB.KeyWord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contacts::class], version = 1, exportSchema = false)
abstract class KeyWordDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao

    companion object {
        private var instance: KeyWordDatabase? = null

        @Synchronized
        fun getInstance(context: Context): KeyWordDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, KeyWordDatabase::class.java, "database-contacts")
                .allowMainThreadQueries()
                .build()
            }
            return instance
        }
    }
}
