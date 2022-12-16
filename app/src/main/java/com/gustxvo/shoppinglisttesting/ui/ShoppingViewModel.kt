package com.gustxvo.shoppinglisttesting.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItem
import com.gustxvo.shoppinglisttesting.data.remote.reponses.ImageResponse
import com.gustxvo.shoppinglisttesting.other.Event
import com.gustxvo.shoppinglisttesting.other.MAX_NAME_LENGTH
import com.gustxvo.shoppinglisttesting.other.MAX_PRICE_LENGTH
import com.gustxvo.shoppinglisttesting.other.Resource
import com.gustxvo.shoppinglisttesting.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias ImageResourceEvent = Event<Resource<ImageResponse>>
typealias ShoppingItemStatus = Event<Resource<ShoppingItem>>

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<ImageResourceEvent>()
    val images: LiveData<ImageResourceEvent> = _images

    private val _currImageUrl = MutableLiveData<String>()
    val currImageUrl: LiveData<String> = _currImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<ShoppingItemStatus>()
    val insertShoppingItemStatus: LiveData<ShoppingItemStatus> = _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error("Fields must not be empty", null)
                )
            )
            return
        }
        if (name.length > MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Name of the item must not exceed $MAX_NAME_LENGTH characters",
                        null
                    )
                )
            )
            return
        }
        if (priceString.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Price of the item must not exceed $MAX_PRICE_LENGTH characters",
                        null
                    )
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Please enter a valid amount",
                        null
                    )
                )
            )
            return
        }
        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _currImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}