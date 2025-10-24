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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.Arguments.arguments
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
open class MainViewModelTest {

    @MockK
    lateinit var repository: LumaPagingRepository

    lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    fun `fetchStories should return expected data`(
        testName: String,
        inputStories: List<StoryDomain>,
        assertions: (List<StoryDomain>) -> Unit
    ) = testScope.runTest {
        val fakePaging = PagingData.from(inputStories)
        coEvery { repository.getPagingStories() } returns flowOf(fakePaging)

        viewModel = MainViewModel(repository)

        val actualPagingData = viewModel.homePaging.first()

        coVerify(exactly = 1) { repository.getPagingStories() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        differ.submitData(actualPagingData)
        advanceUntilIdle()

        val snapshot = differ.snapshot()
        assertNotNull(snapshot)
        assertions(snapshot.toList().filterNotNull())
    }

    companion object {
        @JvmStatic
        fun provideTestCases(): Stream<Arguments> = Stream.of(
            arguments(
                "with data - should return data and not null",
                listOf(
                    StoryDomain("1", "Alice", "Story 1"),
                    StoryDomain("2", "Bob", "Story 2"),
                    StoryDomain("3", "Charlie", "Story 3")
                ),
                { snapshot: List<StoryDomain> ->
                    assertEquals(3, snapshot.size)
                    assertEquals("Alice", snapshot[0].name)
                    assertEquals("Bob", snapshot[1].name)
                    assertEquals("Charlie", snapshot[2].name)
                }
            ),
            arguments(
                "empty data - should return empty list",
                emptyList<StoryDomain>(),
                { snapshot: List<StoryDomain> ->
                    assertTrue(snapshot.isEmpty())
                }
            )
        )
    }
}
