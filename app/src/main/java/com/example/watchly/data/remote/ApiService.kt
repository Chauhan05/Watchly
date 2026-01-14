package com.example.watchly.data.remote

import com.example.watchly.data.remote.response.ItemDetailResponse
import com.example.watchly.data.remote.response.ItemResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("list-titles")
    fun getItems(
        @Query("types") types: String = "movie"
    ): Single<ItemResponse>

    @GET("title/{id}/details")
    fun getDetails(@Path("id") id:Int):Single<ItemDetailResponse>
}