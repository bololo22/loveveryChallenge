package com.example.test.ui.mvi

import com.example.test.ui.model.UIMessage
import java.util.Collections.emptyList

data class MessageListState(
    val loading: Boolean = false,
    val messages: List<UIMessage> = emptyList(),
    val messagesByUser: List<UIMessage> = emptyList(),
    val filterAuthor: String = "",
    val error: String? = null,
    val isRefreshing: Boolean = false
)

