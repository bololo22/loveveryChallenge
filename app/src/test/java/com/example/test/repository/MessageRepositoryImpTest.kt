package com.example.test.repository

import com.example.test.repository.model.MessageResponse
import com.example.test.repository.remote.ApiService
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import org.mockito.kotlin.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever
import retrofit2.Response

class MessageRepositoryImpTest {

    private lateinit var apiService: ApiService
    private lateinit var messageRepository: MessageRepositoryImp

    @Before
    fun setup() {
        apiService = mock()
        messageRepository = MessageRepositoryImp(apiService)
    }

    @Test
    fun `getMessages returns empty list on unsuccessful response`() = runBlocking {
        // Arrange
        whenever(apiService.getMessages()).thenReturn(Response.error(400, mock()))

        // Act
        val result = messageRepository.getMessages()

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `getMessages returns empty list on successful response with non-200 status code`() = runBlocking {
        // Arrange
        val responseBody = mock<MessageResponse>()
        whenever(apiService.getMessages()).thenReturn(Response.success(404, responseBody))

        // Act
        val result = messageRepository.getMessages()

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `getMessages returns list of UIMessage on successful response with 200 status code`() = runBlocking {
        // Arrange
        val responseBody = mock<MessageResponse>()
        whenever(responseBody.statusCode).thenReturn(200)
        whenever(apiService.getMessages()).thenReturn(Response.success(responseBody))

        // Act
        val result = messageRepository.getMessages()

        // Assert
        // Add assertions based on your implementation, e.g., check if the conversion is correct
        verify(responseBody.toUIMessage())
    }

    @Test
    fun `sendMessage returns true on successful response`() = runBlocking {
        // Arrange
        val messageToSend = mock<MessageToSend>()
        whenever(apiService.sendMessage(any())).thenReturn(Response.success(200))

        // Act
        val result = messageRepository.sendMessage(messageToSend)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `sendMessage returns false on unsuccessful response`() = runBlocking {
        // Arrange
        val messageToSend = mock<MessageToSend>()
        whenever(apiService.sendMessage(any())).thenReturn(Response.error(500, mock()))

        // Act
        val result = messageRepository.sendMessage(messageToSend)

        // Assert
        assertEquals(false, result)
    }

    @Test
    fun `searchByUser returns empty list on unsuccessful response`() = runBlocking {
        // Arrange
        val user = "testUser"
        whenever(apiService.getMessagesByUser(user)).thenReturn(Response.error(401, mock()))

        // Act
        val result = messageRepository.searchByUser(user)

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `searchByUser returns empty list on successful response with non-200 status code`() = runBlocking {
        // Arrange
        val user = "testUser"
        val responseBody = mock<MessageResponse>()
        whenever(apiService.getMessagesByUser(user)).thenReturn(Response.success(403, responseBody))

        // Act
        val result = messageRepository.searchByUser(user)

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `searchByUser returns list of UIMessage on successful response with 200 status code`() = runBlocking {
        // Arrange
        val user = "testUser"
        val responseBody = mock<MessageResponse>()
        whenever(responseBody.statusCode).thenReturn(200)
        whenever(apiService.getMessagesByUser(user)).thenReturn(Response.success(responseBody))

        // Act
        val result = messageRepository.searchByUser(user)

        // Assert
        // Add assertions based on your implementation, e.g., check if the conversion is correct
        verify(responseBody).toUIMessageByUser()
    }
}