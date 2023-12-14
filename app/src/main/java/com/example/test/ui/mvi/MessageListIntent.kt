package com.example.test.ui.mvi

import com.example.test.ui.model.MessageToSend

sealed class MessageListIntent {
    data object Initial : MessageListIntent()
    data object LoadMessages : MessageListIntent()
    data class SendMessage(val messageToSend: MessageToSend) : MessageListIntent()
    data class SearchByUser(val user: String) : MessageListIntent()
}

