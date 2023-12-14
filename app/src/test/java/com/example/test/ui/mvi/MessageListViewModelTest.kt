package com.example.test.ui.mvi

import com.example.test.ui.MessageRepository
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MessageListViewModelTest {

    private lateinit var viewModel: MessageListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private val mockRepository: MessageRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MessageListViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after testing
    }

    @Test
    fun `loadMessages updates state with messages on successful response`() =
        testDispatcher.runBlockingTest {
            // Arrange
            val expectedResult = listOf(
                UIMessage("Juan", "Test", "Hello, World!"),
                UIMessage("Dan", "Test 2", "Hello, World! 2")
            )
            coEvery { mockRepository.getMessages() } returns expectedResult

            // Act
            viewModel.onRefreshList()

            // Assert
            assertEquals(false, viewModel.state.value.loading)
            assertEquals(expectedResult, viewModel.state.value.messages)
        }

    @Test
    fun `sendMessage updates effects on successful response`() = testDispatcher.runBlockingTest {
        // Arrange
        val messageToSend = MessageToSend("Juan", "Test", "Hello, World!")
        coEvery { mockRepository.sendMessage(messageToSend) } returns true

        // Act
        viewModel.onSendMessageClicked(messageToSend)

        // Assert
        assertEquals(MessageListEffects.Success("Message Sent"), viewModel.effects.first())
    }

    @Test
    fun `searchByUser updates state with messages on successful response`() =
        testDispatcher.runBlockingTest {
            // Arrange
            val user = "testUser"
            val expectedResult = listOf(
                UIMessage("Juan", "Test", "Hello World"),
                UIMessage("Dan", "Test 2", "User Message 2")
            )
            coEvery { mockRepository.searchByUser(user) } returns expectedResult

            // Act
            viewModel.onSearchByUser(user)

            // Assert
            assertEquals(false, viewModel.state.value.loading)
            assertEquals(expectedResult, viewModel.state.value.messagesByUser)
        }

    @Test
    fun `onRefreshList calls loadMessages`() {
        // Arrange

        // Act
        viewModel.onRefreshList()

        // Assert
        coVerify { mockRepository.getMessages() }
    }

    @Test
    fun `onSendMessageClicked calls sendMessage`() {
        // Arrange
        val messageToSend = MessageToSend("Juan", "Test", "Hello, World!")

        // Act
        viewModel.onSendMessageClicked(messageToSend)

        // Assert
        coVerify { mockRepository.sendMessage(messageToSend) }
    }

    @Test
    fun `onSearchByUser calls searchByUser`() {
        // Arrange
        val user = "testUser"

        // Act
        viewModel.onSearchByUser(user)

        // Assert
        coVerify { mockRepository.searchByUser(user) }
    }
}
