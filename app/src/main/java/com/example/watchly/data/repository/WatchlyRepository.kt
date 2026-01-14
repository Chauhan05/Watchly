package com.example.watchly.data.repository

import com.example.watchly.data.remote.ApiService
import com.example.watchly.data.remote.response.ItemDetailResponse
import com.example.watchly.data.remote.response.ItemResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles
import javax.inject.Inject

class WatchlyRepository @Inject constructor(
    private val api: ApiService
) {

    fun getMoviesAndShows(): Single<Pair<ItemResponse, ItemResponse>> {
        return Singles.zip(
            api.getItems("movie"),
            api.getItems("tv_series")
        )
    }

    fun getDetails(id: Int): Single<ItemDetailResponse> = api.getDetails(id)
}