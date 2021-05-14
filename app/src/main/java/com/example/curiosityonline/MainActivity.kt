package com.example.curiosityonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.curiosityonline.adapters.*
import com.example.curiosityonline.dao.entity.PhotoEntity
import com.example.curiosityonline.extensions.gone
import com.example.curiosityonline.extensions.visible
import com.example.curiosityonline.sal.models.Photo
import com.example.curiosityonline.ui.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PhotoAdapterListener, PhotoEntityAdapterListener {

    lateinit var viewModel: PhotoViewModel
    private val photoAdapter = PhotoAdapter(this)
    private var isPhotoAdapter = false
    private val photoEntityAdapter = PhotoEntityAdapter(this)
    private var isPhotoEntityAdapter = false
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = PhotoViewModel(this)
        viewModel.getPhotos()
        startWithViewModel(viewModel)
    }

    private fun startWithViewModel(viewModel: PhotoViewModel?) {
        viewModel?.state?.observe(this, { state ->
            when (state) {
                is State.LoadedStatePhoto -> {
                    progressBar.gone()
                    if (!isPhotoAdapter) {
                        recyclerViewPhoto.adapter = photoAdapter
                        isPhotoAdapter = true
                    }
                    photoAdapter.submitList(state.data.toMutableList())}
                is State.LoadedStatePhotoEntity -> {
                    progressBar.gone()
                    if (!isPhotoEntityAdapter) {
                        recyclerViewPhoto.adapter = photoEntityAdapter
                        isPhotoEntityAdapter = true
                    }
                    photoEntityAdapter.submitList(state.data)
                }
                is State.LoadingState -> {
                    progressBar.visible()
                }
                else -> {
                    progressBar.visible()
                }
            }
        })
    }

    override fun onClickDeletePhoto(photoModel: Photo, position: Int) {
        viewModel.deletePhoto(photoModel)
        this.position = position - 1
    }

    override fun onClickDeletePhoto(photoModel: PhotoEntity) {
        viewModel.deletePhoto(photoModel)
    }
}