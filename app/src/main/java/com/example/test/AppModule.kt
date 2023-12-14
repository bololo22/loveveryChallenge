package com.example.test

import com.example.test.repository.MessageRepositoryImp
import com.example.test.repository.remote.ApiService
import com.example.test.ui.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://abraxvasbh.execute-api.us-east-2.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideMessageRepository(apiService: ApiService): MessageRepository{
        return MessageRepositoryImp(apiService)
    }
}