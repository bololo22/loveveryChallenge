package com.example.test.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.test.ui.mvi.MessageListViewModel

@Composable
fun SearchScreen(viewModel: MessageListViewModel) {
    var userText by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val focusManager =  LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = userText,
                onValueChange = { newText ->
                    userText = newText
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Handle the button click here
                    }
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onSearchByUser(userText)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Search")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(state.messagesByUser) { item ->
                MessageItem(message = item)
            }
        }


    }
}