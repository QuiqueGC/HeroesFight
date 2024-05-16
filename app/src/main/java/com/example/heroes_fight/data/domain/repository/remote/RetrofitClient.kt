package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.constants.Constants
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor() {

    private val retrofit: Retrofit
    private val remoteApiService: RemoteApiService

    init {
        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        remoteApiService = retrofit.create(RemoteApiService::class.java)
    }

    fun getApiServices(): RemoteApiService {
        return remoteApiService
    }
}