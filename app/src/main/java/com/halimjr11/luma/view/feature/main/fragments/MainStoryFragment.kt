package com.halimjr11.luma.view.feature.main.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.halimjr11.luma.databinding.FragmentMainStoryBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.view.adapter.StoryAdapter
import com.halimjr11.luma.view.adapter.StoryLoadStateAdapter
import com.halimjr11.luma.view.feature.create.CreateActivity
import com.halimjr11.luma.view.feature.main.di.loadMainModule
import com.halimjr11.luma.view.feature.main.viewmodel.MainViewModel
import com.halimjr11.luma.view.feature.maps.MapsActivity
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainStoryFragment :
    BaseFragment<FragmentMainStoryBinding, ViewModel>(FragmentMainStoryBinding::inflate),
    AndroidScopeComponent {

    override val viewModel: MainViewModel by viewModel()
    private val moreStoryAdapter: StoryAdapter by lazy {
        StoryAdapter()
    }
    private val loadStateAdapter: StoryLoadStateAdapter by lazy {
        StoryLoadStateAdapter().apply {
            setOnRetryCallback {
                moreStoryAdapter.retry()
            }
        }
    }
    override val scope: Scope by fragmentScope()
    private var isFabMenuOpen = false

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                moreStoryAdapter.refresh()
            }
        }

    init {
        loadMainModule()
    }

    override fun setupUI() = with(binding) {
        setupFabMenu()
        rvAllStories.isVisible = true
        rvAllStories.apply {
            adapter = moreStoryAdapter.withLoadStateFooter(loadStateAdapter)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        moreStoryAdapter.setOnClickCallback {
            val action =
                MainStoryFragmentDirections.actionMainStoryFragmentToDetailStoryFragment(it.id)
            findNavController().navigate(action)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(homePaging) {
            moreStoryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        super.observeData()
    }

    private fun setupFabMenu() {
        val mainFab = binding.fabTool
        val fabOption1 = binding.fabCreateStory
        val fabOption2 = binding.fabMap

        mainFab.setOnClickListener {
            if (isFabMenuOpen) {
                closeFabMenu(fabOption1, fabOption2)
            } else {
                openFabMenu(fabOption1, fabOption2)
            }
            isFabMenuOpen = !isFabMenuOpen
        }

        fabOption1.setOnClickListener {
            val intent = Intent(context, CreateActivity::class.java)
            resultLauncher.launch(intent)
        }

        fabOption2.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun openFabMenu(vararg fabs: FloatingActionButton) {
        fabs.forEachIndexed { index, fab ->
            fab.apply {
                visibility = View.VISIBLE
                alpha = 0f
                translationY = 100f
                animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay((index * 50).toLong())
                    .setDuration(150)
                    .start()
            }
        }
        binding.fabTool.animate().rotation(45f).setDuration(150).start()
    }

    private fun closeFabMenu(vararg fabs: FloatingActionButton) {
        fabs.forEachIndexed { index, fab ->
            fab.animate()
                .translationY(100f)
                .alpha(0f)
                .setStartDelay((index * 50).toLong())
                .setDuration(150)
                .withEndAction { fab.visibility = View.GONE }
                .start()
        }
        binding.fabTool.animate().rotation(0f).setDuration(150).start()
    }
}