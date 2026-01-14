package com.example.watchly.data.remote.response


import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("titles")
    val titles: List<Title>,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)