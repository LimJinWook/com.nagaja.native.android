package com.nagaja.app.android.RoomDB.KeyWord

import androidx.room.*

@Entity(tableName = "table_keyword_contacts")
data class Contacts(
    @PrimaryKey(autoGenerate = true) val id:Long,
    var category: Int,
    var keyWord: String
)

