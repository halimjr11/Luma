package com.halimjr11.luma.data.model


import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
)