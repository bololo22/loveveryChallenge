package com.example.test.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.ui.model.UIMessage
import com.example.test.ui.mvi.MessageListState
import com.example.test.ui.mvi.MessageListViewModel

@Composable
fun HomeScreen(viewModel: MessageListViewModel) {
    val state by viewModel.state.collectAsState()
    AllMessages(state) {
        viewModel.onRefreshList()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllMessages(
    state: MessageListState,
    onRefresh: () -> Unit
) {
    val refreshState =
        rememberPullRefreshState(refreshing = state.isRefreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(state.messages) { item ->
                MessageItem(message = item)
            }
        }
        PullRefreshIndicator(state.isRefreshing, refreshState, Modifier.align(Alignment.TopCenter))
    }
}


@Preview
@Composable
fun PreviewAllMessages() {
    val state = MessageListState(messages = listOf(UIMessage("author", "subject", "message")))
    AllMessages(state) {}
}