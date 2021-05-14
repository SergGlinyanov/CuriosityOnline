package com.example.curiosityonline.sal.models

data class Photo(var id: Int = 0,
                 var sol: Int = 0,
                 var camera: Camera? = null,
                 var img_src: String? = null,
                 var earth_date: String? = null,
                 var rover: Rover? = null) {

}