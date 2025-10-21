package com.halimjr11.luma.view.feature.main.fragments

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.luma.databinding.FragmentMainStoryBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.ui.helper.visibleIf
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.adapter.FeaturedAdapter
import com.halimjr11.luma.view.adapter.StoryAdapter
import com.halimjr11.luma.view.adapter.StoryLoadStateAdapter
import com.halimjr11.luma.view.feature.create.CreateActivity
import com.halimjr11.luma.view.feature.main.di.loadMainModule
import com.halimjr11.luma.view.feature.main.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainStoryFragment :
    BaseFragment<FragmentMainStoryBinding, ViewModel>(FragmentMainStoryBinding::inflate),
    AndroidScopeComponent {

    override val viewModel: MainViewModel by viewModel()
    private val featuredAdapter: FeaturedAdapter by lazy {
        FeaturedAdapter()
    }
    private val moreStoryAdapter: StoryAdapter by lazy {
        StoryAdapter()
    }
    private val loadStateAdapter: StoryLoadStateAdapter by lazy {
        StoryLoadStateAdapter().apply {
            setOnRetryCallback { }
        }
    }
    override val scope: Scope by fragmentScope()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadHomeEvents()
            }
        }

    init {
        loadMainModule()
    }

    override fun setupUI() = with(binding) {
        mainScrollView.isNestedScrollingEnabled = false
        rvFeaturedStories.apply {
            adapter = featuredAdapter
            setHasFixedSize(true)
        }
        rvAllStories.apply {
            adapter = moreStoryAdapter.withLoadStateFooter(
                footer = loadStateAdapter
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        featuredAdapter.setOnClickCallback {
            val action =
                MainStoryFragmentDirections.actionMainStoryFragmentToDetailStoryFragment(it.id)
            findNavController().navigate(action)
        }

        moreStoryAdapter.setOnClickCallback {
            val action =
                MainStoryFragmentDirections.actionMainStoryFragmentToDetailStoryFragment(it.id)
            findNavController().navigate(action)
        }

        fabCreate.setOnClickListener {
            val intent = Intent(context, CreateActivity::class.java)
            resultLauncher.launch(intent)
        }

        moreStoryAdapter.addLoadStateListener { loadState ->
            binding.progressLoading.visibleIf(loadState.source.refresh is LoadState.Loading)
            binding.evMain.visibleIf(loadState.source.refresh is LoadState.NotLoading && moreStoryAdapter.itemCount == 0)
            binding.mainScrollView.visibleIf(loadState.source.refresh is LoadState.NotLoading && moreStoryAdapter.itemCount > 0)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(homeStories) { state ->
            when (state) {
                is UiState.Success -> {
                    val (featured, _) = state.data
                    featuredAdapter.submitList(featured)
                    startAutoSlide()
                }

                is UiState.Error -> {
                    binding.evMain.setMessageAndCallback(state.message) {
                        viewModel.loadHomeEvents()
                    }
                }

                else -> {}
            }
        }

        launchAndCollect(homePaging) {
            moreStoryAdapter.submitData(it)
        }
        super.observeData()
    }

    private fun startAutoSlide() {
        var currentPosition = 0
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(3000)
                if (featuredAdapter.itemCount == 0) break

                currentPosition = (currentPosition + 1) % featuredAdapter.itemCount
                binding.rvFeaturedStories.smoothScrollToPosition(currentPosition)
            }
        }
    }
}