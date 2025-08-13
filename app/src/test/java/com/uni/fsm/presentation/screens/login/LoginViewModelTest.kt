import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uni.fsm.domain.model.User
import com.uni.fsm.domain.usecase.LoginUseCase
import com.uni.fsm.presentation.screens.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var context: Context
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        context = mockk(relaxed = true)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates uiState and calls onSuccess`() = runTest {
        val user =
            User(user_id = "123", username = "Logesh", email = "logesh@gmail.com", role = "Student")
        coEvery { loginUseCase("Logesh", "s.pwd") } returns Result.success(user)
        val onSuccess = mockk<(String) -> Unit>(relaxed = true)

        viewModel.login("Logesh", "s.pwd", onSuccess, context)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(user, uiState.user)
        assertEquals("Logesh", uiState.userMessage)
        assertEquals(false, uiState.isLoading)
        assertNull(uiState.error)

        verify { onSuccess("123") }
    }

    @Test
    fun `login failure updates uiState with error`() = runTest {
        val exception = Exception("Invalid credentials")
        coEvery { loginUseCase("WrongUser", "WrongPassword") } returns Result.failure(exception)
        val onSuccess = mockk<(String) -> Unit>(relaxed = true)

        viewModel.login("WrongUser", "WrongPassword", onSuccess, context)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(null, uiState.user)
        assertEquals("Invalid credentials", uiState.userMessage)
        assertEquals("Invalid credentials", uiState.error)
        assertEquals(false, uiState.isLoading)

        verify(exactly = 0) { onSuccess(any()) }
    }
}