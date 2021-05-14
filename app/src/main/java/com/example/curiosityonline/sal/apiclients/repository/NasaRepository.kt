package com.example.curiosityonline.sal.apiclients.repository

import com.example.curiosityonline.sal.requests.EmptyRequest
import com.example.curiosityonline.sal.responses.LastPhotoResponse
import com.example.curiosityonline.sal.responses.ManifestResponse
import com.example.curiosityonline.sal.responses.PhotoResponse

interface NasaRepository {
    suspend fun getPhoto(request: EmptyRequest, sol: Int): PhotoResponse
    suspend fun getLastPhoto(request: EmptyRequest): LastPhotoResponse
    suspend fun getManifest(request: EmptyRequest): ManifestResponse
}