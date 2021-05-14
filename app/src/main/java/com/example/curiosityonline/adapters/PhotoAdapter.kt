package com.example.curiosityonline.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.curiosityonline.R
import com.example.curiosityonline.sal.models.Photo
import com.example.curiosityonline.ImageActivity.Companion.startAndLoadFromNetwork
import kotlinx.android.synthetic.main.layout_image.view.*

class PhotoAdapter(private val listener: PhotoAdapterListener): ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.imagePhoto.load(photo.img_src){
            crossfade(true)
            placeholder(R.drawable.ic_nasa_logo)
        }
        holder.imagePhoto.setOnLongClickListener {
            listener.onClickDeletePhoto(photo, position)
            true
        }
        holder.imagePhoto.setOnClickListener {
            startAndLoadFromNetwork(holder.imagePhoto.context, photo.img_src!!)
        }
    }

    inner class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imagePhoto: ImageView = itemView.imageViewPhoto
    }

    class PhotoDiffCallback: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

interface PhotoAdapterListener {
    fun onClickDeletePhoto(photoModel: Photo, position: Int)
}