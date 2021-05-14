package com.example.curiosityonline.sal.responses

class ManifestResponse {
    var photo_manifest: PhotoManifest? = null
}

class PhotoManifest {
    var name: String? = null
    var landing_date: String? = null
    var launch_date: String? = null
    var status: String? = null
    var max_sol = 0
    var max_date: String? = null
    var total_photos = 0
    var photos: List<Photo>? = null
}

