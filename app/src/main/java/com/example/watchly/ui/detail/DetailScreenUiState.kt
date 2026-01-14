package com.example.watchly.ui.detail

import com.example.watchly.data.remote.response.ItemDetailResponse

data class DetailScreenUiState(
    val details: ItemDetailResponse? =null,
    val isLoading:Boolean=false,
    val error:String?=null
)