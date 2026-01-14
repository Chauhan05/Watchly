package com.example.watchly.data.remote.response


import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("tmdb_id")
    val tmdbId: Int?,
    @SerializedName("tmdb_type")
    val tmdbType: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("year")
    val year: Int?
)