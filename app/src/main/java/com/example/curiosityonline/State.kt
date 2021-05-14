package com.example.curiosityonline

import com.example.curiosityonline.dao.entity.PhotoEntity
import com.example.curiosityonline.sal.models.Photo

sealed class State {
    class LoadingState: State()
    class LoadedStatePhotoEntity(val data: List<PhotoEntity>): State()
    class LoadedStatePhoto(val data: List<Photo>): State()
    class EmptyState: State()
    class ErrorState(val message: String): State()
}