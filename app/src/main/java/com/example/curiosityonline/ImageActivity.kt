package com.example.curiosityonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.request.CachePolicy
import com.example.curiosityonline.extensions.gone
import com.example.curiosityonline.extensions.toBitmap
import com.example.curiosityonline.extensions.visible
import com.kpstv.dismiss.image.SwipeDismissImageLayout

class ImageActivity : AppCompatActivity() {
    companion object {
        private const val LOAD_FROM_NETWORK = "load_from_network"
        private const val LOAD_FROM_DATABASE = "load_from_database"
        private const val IMAGE_URI = "image_uri"

        fun startAndLoadFromNetwork(context: Context, imageUrl: String) {
            context.startActivity(Intent(context, ImageActivity::class.java).apply {
                putExtra(LOAD_FROM_NETWORK, true)
                putExtra(IMAGE_URI, imageUrl)
            })
        }
        fun startAndLoadFromNetworkByteArray(context: Context, byteArray: ByteArray) {
            context.startActivity(Intent(context, ImageActivity::class.java).apply {
                putExtra(LOAD_FROM_DATABASE, true)
                putExtra(IMAGE_URI, byteArray)
            })
        }
    }

    private lateinit var sdLayout: SwipeDismissImageLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        progressBar = findViewById(R.id.progressBarImage)
        sdLayout = findViewById(R.id.sdl_layout)
        sdLayout.setSwipeDismissListener { finish() }

        val loadFromNetwork = intent.getBooleanExtra(LOAD_FROM_NETWORK, false)
        val loadFromDataBase = intent.getBooleanExtra(LOAD_FROM_DATABASE, false)
        if (loadFromNetwork) {
            val imageUrl = intent.getStringExtra(IMAGE_URI)
            sdLayout.getRootImageView().load(imageUrl) {
                memoryCachePolicy(CachePolicy.DISABLED)
                listener(
                    onStart = {
                        progressBar.visible()
                    },
                    onError = { _, _ ->
                        progressBar.gone()
                    }, onSuccess = { _, _ ->
                        progressBar.gone()
                    }
                )
            }
        }
        if (loadFromDataBase) {
            val image = intent.getByteArrayExtra(IMAGE_URI)
            if (image != null) {
                sdLayout.getRootImageView().load(image.toBitmap())
            }
        }
    }
}