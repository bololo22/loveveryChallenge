import com.example.test.repository.MessageRepositoryImp
import com.example.test.repository.model.ApiResponse
import com.example.test.repository.model.toSendMessageRequest
import com.example.test.repository.remote.ApiService
import com.example.test.ui.model.MessageToSend
import com.example.test.ui.model.UIMessage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun `test get messages with empty list`() = runBlocking {
        val successfulResponse = Response.success(ApiResponse(200, "{}"))
        coEvery { apiService.getMessages() } returns successfulResponse

        val result = messageRepository.getMessages()

        assertEquals(listOf<UIMessage>(), result)
    }

    @Test
    fun `test get messages with full list`() = runBlocking {
        val successfulResponse = Response.success(
            ApiResponse(
                200,
                "{\"juan\": [{\"subject\": \"2\", \"message\": \"Hello World\"}]}"
            )
        )
        coEvery { apiService.getMessages() } returns successfulResponse

        val result = messageRepository.getMessages()

        assertEquals(listOf(UIMessage("juan", "2", "Hello World")), result)
    }

    @Test
    fun `test get messages with status code dif 200`() = runBlocking {
        val successfulResponse = Response.success(ApiResponse(400, "{}"))
        coEvery { apiService.getMessages() } returns successfulResponse

        val result = messageRepository.getMessages()

        assertEquals(listOf<UIMessage>(), result)
    }

    @Test
    fun `test get messages with failure`() = runBlocking {
        // Mock a failure response
        val errorResponse = Response.error<ApiResponse>(404, ResponseBody.create(null, "Not Found"))
        coEvery { apiService.getMessages() } returns errorResponse

        val result = messageRepository.getMessages()

        // Verify that the result is an empty list in case of failure
        assertEquals(emptyList<UIMessage>(), result)
    }

    @Test
    fun `test send messages`() = runBlocking {
        val successfulResponse = Response.success(ApiResponse(200, "{}"))
        val messageRequest = MessageToSend("Juan", "Test", "Hello World")
        coEvery { apiService.sendMessage(messageRequest.toSendMessageRequest()) } returns successfulResponse

        val result = messageRepository.sendMessage(messageRequest)

        assert(result)
    }

    @Test
    fun `test send message with failure`() = runBlocking {
        // Mock a failure response
        val errorResponse = Response.error<ApiResponse>(404, ResponseBody.create(null, "Not Found"))
        val messageRequest = MessageToSend("Juan", "Test", "Hello World")
        coEvery { apiService.sendMessage(messageRequest.toSendMessageRequest()) } returns errorResponse

        val result = messageRepository.sendMessage(messageRequest)

        // Verify that the result is an empty list in case of failure
        assertFalse(result)
    }

    @Test
    fun `test get messages by user with empty list`() = runBlocking {
        val successfulResponse = Response.success(
            ApiResponse(
                404, "{\n" +
                        "    \"body\": \"\\\"resource not found\\\"\"\n" +
                        "}"
            )
        )
        coEvery { apiService.getMessagesByUser("Juan") } returns successfulResponse

        val result = messageRepository.searchByUser("Juan")

        assertEquals(listOf<UIMessage>(), result)
    }

    @Test
    fun `test get messages by user with full list`() = runBlocking {
        val successfulResponse = Response.success(
            ApiResponse(
                200,
                "{\"user\": \"Juan\", \"message\": [{\"subject\": \"2\", \"message\": \"Hello World\"}]}"
            )
        )
        coEvery { apiService.getMessagesByUser("Juan") } returns successfulResponse

        val result = messageRepository.searchByUser("Juan")

        assertEquals(listOf(UIMessage("Juan", "2", "Hello World")), result)
    }

    @Test
    fun `test get messages by user with status code dif 200`() = runBlocking {
        val successfulResponse = Response.success(ApiResponse(400, "{}"))
        coEvery { apiService.getMessagesByUser("Juan") } returns successfulResponse

        val result = messageRepository.searchByUser("Juan")

        assertEquals(listOf<UIMessage>(), result)
    }

    @Test
    fun `test get messages by user with failure`() = runBlocking {
        // Mock a failure response
        val errorResponse = Response.error<ApiResponse>(404, ResponseBody.create(null, "Not Found"))
        coEvery { apiService.getMessagesByUser("Juan") } returns errorResponse

        val result = messageRepository.searchByUser("Juan")

        // Verify that the result is an empty list in case of failure
        assertEquals(emptyList<UIMessage>(), result)
    }
}