package com.example.curiosityonline.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.curiosityonline.ImageActivity.Companion.startAndLoadFromNetworkByteArray
import com.example.curiosityonline.R
import com.example.curiosityonline.dao.entity.PhotoEntity
import com.example.curiosityonline.extensions.toBitmap
import com.example.curiosityonline.sal.models.Photo
import kotlinx.android.synthetic.main.layout_image.view.*

class PhotoEntityAdapter(private val listener: PhotoEntityAdapterListener): ListAdapter<PhotoEntity, PhotoEntityAdapter.PhotoEntityViewHolder>(PhotoEntityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoEntityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false)
        return PhotoEntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoEntityViewHolder, position: Int) {
        val photoUrl = getItem(position)
        holder.imagePhoto.load(photoUrl.image.toBitmap()) {
            placeholder(R.drawable.ic_nasa_logo)
        }
        holder.imagePhoto.setOnLongClickListener {
            listener.onClickDeletePhoto(photoUrl)
            true
        }
        holder.imagePhoto.setOnClickListener {
            startAndLoadFromNetworkByteArray(holder.imagePhoto.context, photoUrl.image)
        }
    }

    inner class PhotoEntityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imagePhoto: ImageView = itemView.imageViewPhoto
    }

    class PhotoEntityDiffCallback: DiffUtil.ItemCallback<PhotoEntity>() {
        override fun areItemsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

interface PhotoEntityAdapterListener {
    fun onClickDeletePhoto(photoModel: PhotoEntity)
}