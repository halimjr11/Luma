package com.halimjr11.luma.domain.model

data class StoryDomain(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val photoUrl: String = "",
    val createdAt: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0
)