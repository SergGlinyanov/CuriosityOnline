package com.example.curiosityonline.ui.viewmodel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.curiosityonline.R
import com.example.curiosityonline.State
import com.example.curiosityonline.dao.entity.PhotoEntity
import com.example.curiosityonline.extensions.default
import com.example.curiosityonline.extensions.getPhotoDao
import com.example.curiosityonline.extensions.set
import com.example.curiosityonline.extensions.toByteArray
import com.example.curiosityonline.sal.apiclients.NasaRepositoryImpl
import com.example.curiosityonline.sal.models.Photo
import com.example.curiosityonline.sal.requests.EmptyRequest
import kotlinx.android.synthetic.main.alert_common.view.*
import kotlinx.coroutines.*

class PhotoViewModel(private val context: Context) : LifecycleObserver, CoroutineScope by MainScope() {

    //private val photoDao = db.photoDao()
    private var photos: MutableList<Photo> = mutableListOf()
    private var photosFromDb: MutableList<PhotoEntity> = mutableListOf()

    val state: MutableLiveData<State> =
        MutableLiveData<State>().default(initValue = State.LoadingState())

    fun getPhotos() {
        launch( createExceptionHandler()) {
            val getLastSolResponse = NasaRepositoryImpl().getManifest(EmptyRequest())
            val lastSol = getLastSolResponse.photo_manifest?.max_sol
            if (lastSol != null) {
                loadPhotoFromApi(lastSol)
            }
        }
    }

    fun deletePhoto(photoModel: Photo) {
        showDeletePhotoAlert(context, photoModel, null)
    }

    fun deletePhoto(photoEntityModel: PhotoEntity) {
        showDeletePhotoAlert(context, null, photoEntityModel)
    }

    private fun loadPhotoFromApi(sol: Int) {
        var solInner = sol
        launch {
            val response = NasaRepositoryImpl().getPhoto(EmptyRequest(), solInner)
            for (photo in response.photos!!) {
                photos.add(photo)
            }
            // если всего загрузилось меньше 20 фото, грузим фото за предыдущий день
            if (photos.size < 20) {
                solInner--
                loadPhotoFromApi(solInner)
                return@launch
            }
            // если в списке оказалось больше 20 фото удаляем лишние
            while (photos.size > 20) photos.removeLast()
            state.set(newValue = State.LoadedStatePhoto(data = photos))

            // insert photo to DB
            photos.forEach{ photo ->
                val imageLoader = context.imageLoader
                val request = ImageRequest.Builder(context)
                    .data(photo.img_src)
                    .size(500, 500)
                    .allowHardware(false) // Disable hardware bitmaps.
                    .build()
                CoroutineScope(Dispatchers.IO).launch {
                    val drawable = (imageLoader.execute(request) as? SuccessResult)?.drawable
                    if (drawable != null) {
                        context.getPhotoDao().insert(PhotoEntity(photo.id, drawable.toByteArray(), false))
                    }
                }
            }
        }
    }

    private fun createExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            launch {
                photosFromDb = context.getPhotoDao().getAll() as MutableList<PhotoEntity>
                state.set(newValue = State.LoadedStatePhotoEntity(data = photosFromDb))
            }
        }
    }

    private fun showDeletePhotoAlert(context: Context, photoModel: Photo?, photoEntityModel: PhotoEntity?) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.alert_common, null)
        mDialogView.messageAlert.text = context.getString(R.string.alertDelete)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialogView.btnAlertPositive.setOnClickListener {
            if(photoModel != null) {
                photos.remove(photoModel)
                CoroutineScope(Dispatchers.IO).launch {
                    context.getPhotoDao().deleteByPhotoId(photoModel.id)
                }
                state.set(newValue = State.LoadedStatePhoto(data = photos))
            }
            if (photoEntityModel != null){
                photosFromDb.remove(photoEntityModel)
                CoroutineScope(Dispatchers.IO).launch {
                    context.getPhotoDao().delete(photoEntityModel)
                }
                state.set(newValue = State.LoadedStatePhotoEntity(data = photosFromDb))
            }
            mAlertDialog.dismiss()
        }
        mDialogView.btnAlertNegative.setOnClickListener { mAlertDialog.dismiss() }
    }
}