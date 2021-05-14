package com.example.curiosityonline.sal.apiclients

import com.google.gson.reflect.*
import com.example.curiosityonline.sal.apiclients.repository.NasaRepository
import com.example.curiosityonline.sal.requests.EmptyRequest
import com.example.curiosityonline.sal.responses.LastPhotoResponse
import com.example.curiosityonline.sal.responses.ManifestResponse
import com.example.curiosityonline.sal.responses.PhotoResponse

class NasaRepositoryImpl : BaseApiClient(), NasaRepository {

	override suspend fun getPhoto(request: EmptyRequest, sol: Int): PhotoResponse {
		val requestType = object : TypeToken<EmptyRequest>() { }.type
		val responseType = object : TypeToken<PhotoResponse>() { }.type
		return makeRequestForNasaApi("$NASA_API_CURIOSITY_PHOTOS?api_key=$NASA_API_KEY&sol=$sol", request, requestType, responseType)
	}

	override suspend fun getLastPhoto(request: EmptyRequest): LastPhotoResponse {
		val requestType = object : TypeToken<EmptyRequest>() { }.type
		val responseType = object : TypeToken<LastPhotoResponse>() { }.type
		return makeRequestForNasaApi("$NASA_API_CURIOSITY_LATEST_PHOTOS?api_key=$NASA_API_KEY&sol=1000", request, requestType, responseType)
	}

	override suspend fun getManifest(request: EmptyRequest): ManifestResponse {
		val requestType = object : TypeToken<EmptyRequest>() { }.type
		val responseType = object : TypeToken<ManifestResponse>() { }.type
		return makeRequestForNasaApi("$NASA_API_CURIOSITY_MANIFEST?api_key=$NASA_API_KEY", request, requestType, responseType)
	}

}
