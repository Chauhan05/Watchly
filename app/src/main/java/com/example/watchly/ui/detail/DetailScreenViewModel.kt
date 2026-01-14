package com.example.watchly.ui.detail

import androidx.lifecycle.ViewModel
import com.example.watchly.data.repository.WatchlyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class DetailScreenViewModel @Inject constructor(private val repo: WatchlyRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(DetailScreenUiState())
    val uiState = _uiState.asStateFlow()


    fun loadDetails(id: Int) {
        _uiState.update {
            it.copy(
                details = null,
                isLoading = true,
                error = null,
            )
        }


        repo.getDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { detailResponse ->
                    _uiState.update {
                        it.copy(details = detailResponse, isLoading = false)
                    }

                },
                { throwable ->
                    val errorMessage = mapError(throwable)
                    _uiState.update {
                        it.copy(
                            details = null,
                            error = errorMessage,
                            isLoading = false,
                        )
                    }
                }
            )
    }


    private fun mapError(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No internet connection. Please check your network."
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is IOException -> "Network error. Please check your connection."
            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    404 -> "Item not found"
                    500 -> "Server error. Please try again later."
                    401, 403 -> "Access denied"
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