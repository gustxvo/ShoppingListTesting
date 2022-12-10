package com.gustxvo.shoppinglisttesting.data.remote.reponses

data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)