package com.halimjr11.luma.data.model

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("photoUrl")
    val photoUrl: String? = null
)