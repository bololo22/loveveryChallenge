package com.example.test.ui.mvi

import com.example.test.ui.model.MessageToSend

sealed class MessageListEffects {
    data object Initial : MessageListEffects()
    data class Error(val errorText: String ) : MessageListEffects()
    data class Success(val successText: String ) : MessageListEffects()
}

