package com.example.test.repository.remote

import com.example.test.repository.model.ApiResponse
import com.example.test.repository.model.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("proto/messages")
    suspend fun getMessages(): Response<ApiResponse>

    @POST("proto/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<ApiResponse>

    @GET("proto/messages/{user}")
    suspend fun getMessagesByUser(@Path("user") user: String): Response<ApiResponse>

}
