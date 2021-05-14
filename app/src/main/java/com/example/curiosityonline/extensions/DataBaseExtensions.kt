package com.example.curiosityonline.extensions

import android.content.Context
import androidx.room.Room
import com.example.curiosityonline.dao.AppDatabase
import com.example.curiosityonline.dao.iface.PhotoDao

fun Context.getPhotoDao() : PhotoDao {
    val db = Room.databaseBuilder(
        this, AppDatabase::class.java,
        "curiosity_online"
    ).build()
    return db.photoDao()
}