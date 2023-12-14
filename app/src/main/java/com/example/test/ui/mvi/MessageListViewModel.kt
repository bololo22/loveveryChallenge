package com.example.test.ui.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.ui.MessageRepository
import com.example.test.ui.model.MessageToSend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(private val repository: MessageRepository) :
    ViewModel() {
    private val _state =
        MutableStateFlow(MessageListState())
    val state: StateFlow<MessageListState> = _state.asStateFlow()

    private val _intent = MutableStateFlow<MessageListIntent>(MessageListIntent.Initial)
    private val intent: Flow<MessageListIntent> = _intent


    private val _effects = MutableStateFlow<MessageListEffects>(MessageListEffects.Initial)
    val effects: Flow<MessageListEffects> = _effects

    init {
        handleIntent()
        loadMessages()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    MessageListIntent.Initial -> {}
                    is MessageListIntent.LoadMessages -> {
                        _state.value = _state.value.copy(loading = true)
                        loadMessages()
                    }

                    is MessageListIntent.SendMessage -> {
                        _state.value = _state.value.copy(loading = true)
                        sendMessage(intent.messageToSend)
                    }

                    is MessageListIntent.SearchByUser -> {
                        _state.value = _state.value.copy(loading = true)
                        searchByUser(intent.user)
                    }
                }
            }
        }
    }
    
    private fun searchByUser(user: String){
        viewModelScope.launch {
            try {
                val result = repository.searchByUser(user)
                _state.value = _state.value.copy(loading = false, messagesByUser = result)
            } catch (e: Exception) {
                _effects.emit(MessageListEffects.Error("Error sending messages"))
                Log.e("MessageListViewModel","searchByUser",e)
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                val result = repository.getMessages()
                _state.value = _state.value.copy(loading = false, messages = result)
            } catch (e: Exception) {
                _effects.emit(MessageListEffects.Error("Error sending messages"))
                Log.e("MessageListViewModel","loadMessages",e)
            }
        }
    }

    private fun sendMessage(messageToSend: MessageToSend) {
        viewModelScope.launch {
            try {
                repository.sendMessage(messageToSend)
                _effects.emit(MessageListEffects.Success("Message Sent"))
            } catch (e: Exception) {
                _effects.emit(MessageListEffects.Error("Error sending messages"))
                Log.e("MessageListViewModel","sendMessage",e)
            }
        }
    }

    fun onSendMessageClicked(messageToSend: MessageToSend) {
        viewModelScope.launch {
            _intent.emit(MessageListIntent.SendMessage(messageToSend))
        }
    }

    fun onRefreshList() {
        viewModelScope.launch {
            _intent.emit(MessageListIntent.LoadMessages)
        }
    }

    fun onSearchByUser(userText: String) {
        viewModelScope.launch {
            _intent.emit(MessageListIntent.SearchByUser(userText))
        }
    }
}

