package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                val resp = iTunesService.search(dto.expression)

                    resp.apply { resultCode = 200 }
            } catch (e: Exception) {
                    Response().apply { resultCode = 400 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}