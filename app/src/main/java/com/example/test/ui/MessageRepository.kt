package com.example.test.ui

import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage

interface MessageRepository {

    suspend fun getMessages(): List<UIMessage>

    suspend fun sendMessage(messageToSend: MessageToSend): Boolean

    suspend fun searchByUser(user: String): List<UIMessage>
}