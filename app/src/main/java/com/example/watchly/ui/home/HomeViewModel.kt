package com.example.watchly.ui.home

import androidx.lifecycle.ViewModel
import com.example.watchly.data.repository.WatchlyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WatchlyRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {

        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }
        repository.getMoviesAndShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { (movies, tvShows) ->
                    _uiState.update {
                        it.copy(
                            movies = movies.titles,
                            tvShows = tvShows.titles,
                            isLoading = false
                        )
                    }
                },
                { throwable ->
                    val errorMessage = mapErrorToMessage(throwable)
                    _uiState.update {
                        it.copy(
                            error = errorMessage,
                            isLoading = false,
                        )
                    }
                }
            )
    }


    private fun mapErrorToMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No internet connection. Please check your network."
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is IOException -> "Network error. Please check your connection."
            is HttpException -> {
                when (throwable.code()) {
                    404 -> "Content not found"
                    500 -> "Server error. Please try again later."
                    401, 403 -> "Access denied. Check your API key."
                    429 -> "Too many requests. Please wait and try again."
                    else -> "Something went wrong (Error ${throwable.code()})"
                }
            }

            else -> throwable.message ?: "An unexpected error occurred"
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }


}