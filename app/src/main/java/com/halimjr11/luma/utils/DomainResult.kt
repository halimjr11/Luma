package com.halimjr11.luma.utils

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val message: String, val code: Int? = null) : DomainResult<Nothing>()
}
