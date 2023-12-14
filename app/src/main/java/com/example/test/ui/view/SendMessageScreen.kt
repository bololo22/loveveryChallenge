package com.example.test.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.mvi.MessageListViewModel

@Composable
fun SendMessageScreen(viewModel: MessageListViewModel) {
    var user by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val focusManager =  LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = user,
            onValueChange = { newUser ->
                user = newUser
            },
            placeholder = { Text(text = "User") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = subject,
            onValueChange = { newSubject ->
                subject = newSubject
            },
            placeholder = { Text(text = "Subject") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        TextField(
            value = message,
            onValueChange = { newMessage ->
                message = newMessage.take(100)
            },
            placeholder = { Text(text = "Message (max 100 characters)") },
            maxLines = 5,
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.onSendMessageClicked(MessageToSend(user, subject, message))
                user = ""
                subject = ""
                message = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Send")
        }
    }
}