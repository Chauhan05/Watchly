package com.example.watchly.ui.home

import com.example.watchly.data.remote.response.Title

data class HomeScreenUiState(
    val movies: List<Title> = emptyList(),
    val tvShows: List<Title> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
