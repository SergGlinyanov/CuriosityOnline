package com.example.curiosityonline.sal.apiclients

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.curiosityonline.sal.apiclients.adapters.ByteArrayAdapter
import com.example.curiosityonline.sal.apiclients.adapters.UnixDateAdapter
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.full.createType


open class BaseApiClient {
	companion object {
		private val commonHttpClient = OkHttpClient()
		private const val URL = "https://api.nasa.gov/mars-photos/api/v1"
		const val NASA_API_CURIOSITY_PHOTOS = "rovers/curiosity/photos"
		const val NASA_API_CURIOSITY_LATEST_PHOTOS = "rovers/curiosity/latest_photos"
		const val NASA_API_CURIOSITY_MANIFEST = "manifests/curiosity"
		const val NASA_API_KEY = "JT9qVKmnL6hTUVFYEy9xPom36LnPaeVd0Q5iKOEf"

		private fun createSerializer() = GsonBuilder()
			.registerTypeAdapter(Date::class.java, UnixDateAdapter())
			.registerTypeAdapter(Date::class.createType(nullable = true).javaClass, UnixDateAdapter())
			.registerTypeAdapter(ByteArray::class.java, ByteArrayAdapter())
			.registerTypeAdapter(ByteArray::class.createType(nullable = true).javaClass, ByteArrayAdapter())
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.create()

		private fun createSerializerLowerCase() = GsonBuilder()
			.registerTypeAdapter(Date::class.java, UnixDateAdapter())
			.registerTypeAdapter(Date::class.createType(nullable = true).javaClass, UnixDateAdapter())
			.registerTypeAdapter(ByteArray::class.java, ByteArrayAdapter())
			.registerTypeAdapter(ByteArray::class.createType(nullable = true).javaClass, ByteArrayAdapter())
			.create()

		public fun <T> serialize(value: T, type: Type): String = createSerializer().toJson(value, type)
		public fun <T> serializeWithLowerCase(value: T, type: Type): String = createSerializerLowerCase().toJson(value, type)

		public fun <T> deserialize(s: String, type: Type): T = createSerializer().fromJson(s, type)
		public fun <T> deserializeWithLowerCase(s: String, type: Type): T = createSerializerLowerCase().fromJson(s, type)
	}

	protected suspend fun <TRequest, TResponse> makeRequestForNasaApi(relativeUrl: String, request: TRequest, requestType: Type, responseType: Type): TResponse = withContext(Dispatchers.Default) {
		val networkRequest = prepareNetworkRequestForNasaApi(relativeUrl, request, requestType)
		return@withContext suspendCoroutine<TResponse> { continuation ->
			createHttpClient().newCall(networkRequest).enqueue(object : Callback {
				override fun onFailure(call: Call, e: IOException) {
					continuation.resumeWithException(e)
				}

				override fun onResponse(call: Call, response: Response) {
					try {
						val result = processNetworkResponseNasaApi<TResponse>(response, responseType)
						continuation.resume(result)
					} catch (ex: Exception) {
						continuation.resumeWithException(ex)
					}
				}
			})
		}
	}

	private fun <TRequest> prepareNetworkRequestForNasaApi(relativeUrl: String, request: TRequest, requestType: Type): Request {
		val mediaType = "application/json".toMediaType()
		val requestBody = serializeWithLowerCase(request, requestType).toRequestBody(mediaType)
		return Request.Builder()
			.url(URL.trimEnd('/') + "/" + relativeUrl.trimStart('/'))
			.get()
			.build()
	}

	private fun <TResponse> processNetworkResponse(response: Response, responseType: Type): TResponse {
		val responseBody = response.body?.string() ?: ""
		if (response.code != 200) {
			throw Exception("Request to address ${response.request.url} completed with status code ${response.code}, response \"$responseBody\"")
		}
		return deserialize(responseBody, responseType)
	}

	private fun <TResponse> processNetworkResponseNasaApi(response: Response, responseType: Type): TResponse {
		val responseBody = response.body?.string() ?: ""
		if (response.code != 200) {
			throw Exception("Request to address ${response.request.url} completed with status code ${response.code}, response \"$responseBody\"")
		}
		return deserializeWithLowerCase(responseBody, responseType)
	}

	private fun createHttpClient() = commonHttpClient.newBuilder()
		.connectTimeout(60, TimeUnit.SECONDS)
		.writeTimeout(60, TimeUnit.SECONDS)
		.readTimeout(60, TimeUnit.SECONDS)
		.build()
}
