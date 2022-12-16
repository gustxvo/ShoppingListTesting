package com.gustxvo.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.gustxvo.shoppinglisttesting.R
import com.gustxvo.shoppinglisttesting.adapter.ImageAdapter
import com.gustxvo.shoppinglisttesting.getOrAwaitValue
import com.gustxvo.shoppinglisttesting.launchFragmentInHiltContainer
import com.gustxvo.shoppinglisttesting.repository.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
@OptIn(ExperimentalCoroutinesApi::class)
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl() {
        val navController = mock(NavController::class.java)
        val imageUrl = "fakeUrl"
        var testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<ImagePickFragment> {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.submitList(listOf(imageUrl))
            testViewModel = viewModel
        }
        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0, ViewActions.click()
            )
        )

        verify(navController).popBackStack()
        assertThat(testViewModel.currImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }
}