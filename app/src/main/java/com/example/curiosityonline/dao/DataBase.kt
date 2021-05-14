package com.example.curiosityonline.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.curiosityonline.dao.entity.PhotoEntity
import com.example.curiosityonline.dao.iface.PhotoDao

@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

