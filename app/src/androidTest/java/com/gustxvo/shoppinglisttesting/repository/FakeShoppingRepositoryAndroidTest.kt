package com.gustxvo.shoppinglisttesting.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItem
import com.gustxvo.shoppinglisttesting.data.remote.reponses.ImageResponse
import com.gustxvo.shoppinglisttesting.other.Resource

class FakeShoppingRepositoryAndroidTest : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems =
        MutableLiveData<List<ShoppingItem>>(shoppingItems)

    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> =
        observableShoppingItems

    override fun observeTotalPrice(): LiveData<Float> =
        observableTotalPrice

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> =
        if (shouldReturnNetworkError) Resource.error("Error", null)
        else Resource.success(ImageResponse(listOf(), 0, 0))
}