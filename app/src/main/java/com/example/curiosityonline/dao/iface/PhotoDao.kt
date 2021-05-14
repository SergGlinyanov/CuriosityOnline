package com.example.curiosityonline.dao.iface

import androidx.room.*
import com.example.curiosityonline.dao.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
     suspend fun getAll(): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // if contain -> do not insert
    suspend fun insert(photoEntity: PhotoEntity)

    @Delete
    suspend fun delete(photoEntity: PhotoEntity)

    @Query("DELETE FROM photo WHERE _id = :photoId")
    suspend fun deleteByPhotoId(photoId: Int)

    @Update
    suspend fun update(photoEntity: PhotoEntity)

    @Query("DELETE FROM photo")
    suspend fun nukeTable()

}