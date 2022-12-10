package com.gustxvo.shoppinglisttesting.repository

import androidx.lifecycle.LiveData
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItem
import com.gustxvo.shoppinglisttesting.data.remote.reponses.ImageResponse

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): com.gustxvo.shoppinglisttesting.other.Resource<ImageResponse>
}