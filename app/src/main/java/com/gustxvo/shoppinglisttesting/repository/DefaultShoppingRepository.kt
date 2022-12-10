package com.gustxvo.shoppinglisttesting.repository

import androidx.lifecycle.LiveData
import com.gustxvo.shoppinglisttesting.data.local.ShoppingDao
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItem
import com.gustxvo.shoppinglisttesting.data.remote.PixabayApi
import com.gustxvo.shoppinglisttesting.data.remote.reponses.ImageResponse
import com.gustxvo.shoppinglisttesting.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) =
        shoppingDao.insertShoppingItem(shoppingItem)

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) =
        shoppingDao.deleteShoppingItem(shoppingItem)

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> =
        shoppingDao.observeAllShoppingItems()

    override fun observeTotalPrice(): LiveData<Float> =
        shoppingDao.observeTotalPrice()

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> =
        try {
            val response = pixabayApi.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else Resource.error("An unknown error occurred", null)
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server", null)
        }
}