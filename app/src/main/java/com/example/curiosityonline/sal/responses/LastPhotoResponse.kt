package com.example.curiosityonline.sal.responses

import com.example.curiosityonline.sal.models.Camera
import com.example.curiosityonline.sal.models.Rover

class LastPhotoResponse {
    var latest_photos: List<LatestPhoto>? = null
}
class LatestPhoto {
    var id = 0
    var sol = 0
    var camera: Camera? = null
    var img_src: String? = null
    var earth_date: String? = null
    var rover: Rover? = null
}

