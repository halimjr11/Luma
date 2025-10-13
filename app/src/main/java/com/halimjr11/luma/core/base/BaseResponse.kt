package com.halimjr11.luma.core.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("listStory", alternate = ["story"])
    val data: T? = null
)