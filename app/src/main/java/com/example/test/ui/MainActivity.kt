package com.example.test.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.test.ui.mvi.MessageListEffects
import com.example.test.ui.mvi.MessageListViewModel
import com.example.test.ui.view.HomeScreen
import com.example.test.ui.view.SearchScreen
import com.example.test.ui.view.SendMessageScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MessageListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf("All", "By User", "Send")
            var selectedItem by remember { mutableIntStateOf(0) }
            val scaffoldState = rememberScaffoldState()

            val effects by viewModel.effects.collectAsState(initial = MessageListEffects.Initial)
            LaunchedEffect(effects) {
                effects.let { effect ->
                    when (effect) {
                        is MessageListEffects.Error -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = effect.errorText,
                                actionLabel = "Dismiss",
                                duration = SnackbarDuration.Short
                            )
                        }

                        MessageListEffects.Initial -> {}
                        is MessageListEffects.Success -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = effect.successText,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = { hostState ->
                    // Create a SnackbarHost to show Snackbars
                    SnackbarHost(hostState = hostState)
                },
                bottomBar = {
                    BottomNavigation {
                        items.forEachIndexed { index, label ->
                            val icon = when (index) {
                                0 -> if (selectedItem == index) Icons.Filled.Home else Icons.Outlined.Home
                                1 -> if (selectedItem == index) Icons.Filled.Person else Icons.Outlined.Person
                                2 -> if (selectedItem == index) Icons.Filled.MailOutline else Icons.Outlined.MailOutline
                                else -> Icons.Default.Home
                            }
                            BottomNavigationItem(
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                label = { Text(label) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    when (selectedItem) {
                        0 -> HomeScreen(viewModel)
                        1 -> SearchScreen(viewModel)
                        2 -> SendMessageScreen(viewModel)
                    }
                }
            }
        }
    }
}
