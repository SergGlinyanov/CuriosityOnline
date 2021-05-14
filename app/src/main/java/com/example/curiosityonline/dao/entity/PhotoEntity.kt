package com.example.curiosityonline.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,
    @ColumnInfo(name = "_image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray,
    @ColumnInfo(name = "_banned")
    var isBanned: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoEntity

        if (id != other.id) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + image.contentHashCode()
        return result
    }
}