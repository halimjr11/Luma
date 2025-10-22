package com.halimjr11.luma.view.feature.main.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.repository.LumaPagingRepository
import com.halimjr11.luma.view.feature.main.util.NoopListCallback
import com.halimjr11.luma.view.feature.main.util.StoryDiffCallback
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class MainViewModelTest {

    @MockK
    lateinit var repository: LumaPagingRepository

    lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val dummyStories = listOf(
        StoryDomain("1", "Alice", "Story 1"),
        StoryDomain("2", "Bob", "Story 2"),
        StoryDomain("3", "Charlie", "Story 3")
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `fetchStories_onSuccess_shouldReturnDataAndNotNull`() = testScope.runTest {
        // Given
        val fakePaging = PagingData.from(dummyStories)
        coEvery { repository.getPagingStories() } returns flowOf(fakePaging)

        viewModel = MainViewModel(repository)

        // When
        val actualPagingData = viewModel.homePaging.first()

        // Then
        coVerify(exactly = 1) { repository.getPagingStories() }

        // Use AsyncPagingDataDiffer to collect PagingData snapshot
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        differ.submitData(actualPagingData)
        advanceUntilIdle()

        // Assertions
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `fetchStories_onSuccess_shouldReturnEmptyData`() = testScope.runTest {
        // Given
        val fakeEmpty = PagingData.from(emptyList<StoryDomain>())
        coEvery { repository.getPagingStories() } returns flowOf(fakeEmpty)

        viewModel = MainViewModel(repository)

        // When
        val actualPagingData = viewModel.homePaging.first()

        // Then
        coVerify(exactly = 1) { repository.getPagingStories() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        differ.submitData(actualPagingData)
        advanceUntilIdle()

        assertEquals(0, differ.snapshot().size)
    }
}
