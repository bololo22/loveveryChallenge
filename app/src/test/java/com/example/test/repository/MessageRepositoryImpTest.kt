import com.example.test.repository.MessageRepositoryImp
import com.example.test.repository.model.ApiResponse
import com.example.test.repository.remote.ApiService
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MessageRepositoryImpTest {

    private lateinit var apiService: ApiService
    private lateinit var messageRepository: MessageRepositoryImp

    @Before
    fun setup() {
        apiService = mockk()
        messageRepository = MessageRepositoryImp(apiService)
    }

    @Test
    fun `getMessages returns empty list on unsuccessful response`() = runBlocking {
        // Arrange
        val responseBody = mockk<ResponseBody> {
            every { contentType() } returns okhttp3.MediaType.get("application/json")
        }
        val response = mockk<Response<ApiResponse>> {
            every { isSuccessful } returns true
            every { body() } returns responseBody
        }
        coEvery { apiService.getMessages() } returns Response.error(400, response.errorBody()!!)

        // Act
        val result = messageRepository.getMessages()

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `getMessages returns empty list on successful response with non-200 status code`() =
        runBlocking {
            // Arrange
            val responseBody = mockk<ApiResponse>()
            coEvery { apiService.getMessages() } returns Response.success(404, responseBody)

            // Act
            val result = messageRepository.getMessages()

            // Assert
            assertEquals(emptyList<UIMessage>(), result)
        }

    @Test
    fun `getMessages returns list of UIMessage on successful response with 200 status code`() =
        runBlocking {
            // Arrange
            val responseBody = mockk<ApiResponse>()
            coEvery { responseBody.statusCode } returns 200
            coEvery { apiService.getMessages() } returns Response.success(responseBody)

            // Act
            val result = messageRepository.getMessages()

            // Assert
            // Add assertions based on your implementation, e.g., check if the conversion is correct
            // verify(responseBody).toUIMessage()
        }

    @Test
    fun `sendMessage returns true on successful response`() = runBlocking {
        // Arrange
        val messageToSend = mockk<MessageToSend>()
        val apiResponse = mockk<ApiResponse>()
        coEvery { apiService.sendMessage(any()) } returns Response.success(200, apiResponse)

        // Act
        val result = messageRepository.sendMessage(messageToSend)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `sendMessage returns false on unsuccessful response`() = runBlocking {
        // Arrange
        val messageToSend = mockk<MessageToSend>()
        coEvery { apiService.sendMessage(any()) } returns Response.error(500, mockk())

        // Act
        val result = messageRepository.sendMessage(messageToSend)

        // Assert
        assertEquals(false, result)
    }

    @Test
    fun `searchByUser returns empty list on unsuccessful response`() = runBlocking {
        // Arrange
        val user = "testUser"
        coEvery { apiService.getMessagesByUser(user) } returns Response.error(401, mockk())

        // Act
        val result = messageRepository.searchByUser(user)

        // Assert
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `searchByUser returns empty list on successful response with non-200 status code`() =
        runBlocking {
            // Arrange
            val user = "testUser"
            val responseBody = mockk<ApiResponse>()
            coEvery { apiService.getMessagesByUser(user) } returns Response.success(
                403,
                responseBody
            )

            // Act
            val result = messageRepository.searchByUser(user)

            // Assert
            assertEquals(emptyList<UIMessage>(), result)
        }

    @Test
    fun `searchByUser returns list of UIMessage on successful response with 200 status code`() =
        runBlocking {
            // Arrange
            val user = "testUser"
            val responseBody = mockk<ApiResponse>()
            coEvery { responseBody.statusCode } returns 200
            coEvery { apiService.getMessagesByUser(user) } returns Response.success(responseBody)

            // Act
            val result = messageRepository.searchByUser(user)

            // Assert
            // Add assertions based on your implementation, e.g., check if the conversion is correct
            // verify(responseBody).toUIMessageByUser()
        }
}