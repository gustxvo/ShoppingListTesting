package com.gustxvo.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.gustxvo.shoppinglisttesting.data.local.ShoppingDao
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItemDatabase
import com.gustxvo.shoppinglisttesting.data.remote.PixabayApi
import com.gustxvo.shoppinglisttesting.other.BASE_URL
import com.gustxvo.shoppinglisttesting.other.DATABASE_NAME
import com.gustxvo.shoppinglisttesting.repository.DefaultShoppingRepository
import com.gustxvo.shoppinglisttesting.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ShoppingItemDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        shoppingDao: ShoppingDao,
        shoppingApi: PixabayApi
    ) = DefaultShoppingRepository(shoppingDao, shoppingApi) as ShoppingRepository

    @Singleton
    @Provides
    fun providesShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }
}