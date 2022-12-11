package com.gustxvo.shoppinglisttesting.other

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
    * Returns the content and prevents its use again
    */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it has been handled
     */
    fun pickContent(): T = content
}