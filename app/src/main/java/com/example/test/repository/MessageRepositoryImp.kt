package com.example.test.repository

import com.example.test.repository.model.toSendMessageResponse
import com.example.test.repository.model.toUIMessage
import com.example.test.repository.model.toUIMessageByUser
import com.example.test.repository.remote.ApiService
import com.example.test.ui.MessageRepository
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import javax.inject.Inject

class MessageRepositoryImp @Inject constructor(private val apiService: ApiService) :
    MessageRepository {

    override suspend fun getMessages(): List<UIMessage> {
        val response = apiService.getMessages()
        if (response.isSuccessful && response.body()?.statusCode == 200) {
            return response.body().toUIMessage()
        }
        return listOf()
    }

    override suspend fun sendMessage(messageToSend: MessageToSend): Boolean {
        val response = apiService.sendMessage(messageToSend.toSendMessageResponse())
        return response.isSuccessful
    }

    override suspend fun searchByUser(user: String): List<UIMessage> {
        val response = apiService.getMessagesByUser(user)
        if (response.isSuccessful && response.body()?.statusCode == 200) {
            return response.body().toUIMessageByUser()
        }
        return listOf()
    }
}
