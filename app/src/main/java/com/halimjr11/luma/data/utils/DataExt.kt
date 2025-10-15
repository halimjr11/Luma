package com.halimjr11.luma.data.utils

import com.halimjr11.luma.utils.Constants.NETWORK_ERROR
import com.halimjr11.luma.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.luma.utils.DomainResult
import retrofit2.HttpException
import java.io.IOException

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): DomainResult<T> {
    return try {
        val response = apiCall()
        DomainResult.Success(response)
    } catch (e: HttpException) {
        DomainResult.Error(e.message(), e.code())
    } catch (e: IOException) {
        DomainResult.Error(NETWORK_ERROR)
    } catch (e: Exception) {
        DomainResult.Error(e.localizedMessage ?: UNKNOWN_ERROR)
    }
}