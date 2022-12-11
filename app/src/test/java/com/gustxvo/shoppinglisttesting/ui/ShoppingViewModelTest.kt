package com.gustxvo.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.gustxvo.shoppinglisttesting.MainCoroutineRule
import com.gustxvo.shoppinglisttesting.getOrAwaitValueTest
import com.gustxvo.shoppinglisttesting.other.MAX_NAME_LENGTH
import com.gustxvo.shoppinglisttesting.other.MAX_PRICE_LENGTH
import com.gustxvo.shoppinglisttesting.other.Status
import com.gustxvo.shoppinglisttesting.repository.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field returns false`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val string = buildString {
            for (i in 0..MAX_NAME_LENGTH) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val string = buildString {
            for (i in 0..MAX_PRICE_LENGTH) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "9999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "200", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `imageUrl is reset after shopping item successfully inserted, returns empty url`() {
        viewModel.insertShoppingItem("name", "200", "3.0")

        val currImageUrl = viewModel.currImageUrl.getOrAwaitValueTest()
        assertThat(currImageUrl).isEmpty()
    }

    @Test
    fun `sets imageUrl can be observed afterwards`() {
        viewModel.setCurrentImageUrl("url")

        val currImageUrl = viewModel.currImageUrl.getOrAwaitValueTest()
        assertThat(currImageUrl).isNotEmpty()
    }
}