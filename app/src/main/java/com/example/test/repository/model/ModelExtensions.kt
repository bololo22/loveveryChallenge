package com.example.test.repository.model

import com.example.test.repository.RepositoryConstants
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun ApiResponse?.toUIMessage(): List<UIMessage> {
    this ?: return listOf()

    val bodyJson = this.body
    val gson = Gson()
    val mapType = object : TypeToken<Map<String, List<MessageItem>>>() {}.type
    val bodyMap: Map<String, List<MessageItem>> = gson.fromJson(bodyJson, mapType)

    val uiMessages = mutableListOf<UIMessage>()

    for ((author, messageItems) in bodyMap) {
        for (messageItem in messageItems) {
            val uiMessage = UIMessage(
                author = author,
                subject = messageItem.subject,
                messageText = messageItem.message
            )
            uiMessages.add(uiMessage)
        }
    }
    return uiMessages
}

fun ApiResponse?.toUIMessageByUser(): List<UIMessage> {
    this ?: return listOf()

    val gson = Gson()
    val bodyJson = this.body

    val messageResponse = gson.fromJson(bodyJson, MessageResponse::class.java)

    val uiMessages = messageResponse.message.map { messageItem ->
        UIMessage(
            author = messageResponse.user,
            subject = messageItem.subject,
            messageText = messageItem.message
        )
    }
    return uiMessages
}

fun MessageToSend.toSendMessageResponse(): SendMessageRequest {
    return SendMessageRequest(
        user = this.user,
        operation = RepositoryConstants.ADD_MESSAGE_OPERATION,
        subject = this.subject,
        message = this.message
    )
}