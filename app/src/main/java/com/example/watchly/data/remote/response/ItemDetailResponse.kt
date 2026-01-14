package com.example.watchly.data.remote.response


import com.google.gson.annotations.SerializedName

data class ItemDetailResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("backdrop")
    val backdrop: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("type")
    val type: String?,

    @SerializedName("poster")
    val poster: String?,

    @SerializedName("plot_overview")
    val plotOverview: String?,

    @SerializedName("genre_names")
    val genreNames: List<String>?,

    @SerializedName("runtime_minutes")
    val runtimeMinutes: Int?,

    @SerializedName("year")
    val year: Int?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("user_rating")
    val userRating: Double?,

    @SerializedName("critic_score")
    val criticScore: Int?,

    @SerializedName("trailer")
    val trailer: String?
)


