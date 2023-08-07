package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = 400 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}