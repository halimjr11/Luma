package com.halimjr11.luma.view.feature.main.fragments

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.luma.databinding.FragmentMainStoryBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.view.adapter.StoryAdapter
import com.halimjr11.luma.view.adapter.StoryLoadStateAdapter
import com.halimjr11.luma.view.feature.create.CreateActivity
import com.halimjr11.luma.view.feature.main.di.loadMainModule
import com.halimjr11.luma.view.feature.main.viewmodel.MainViewModel
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
        rvAllStories.apply {
            adapter = ConcatAdapter(moreStoryAdapter, loadStateAdapter)
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

        fabCreate.setOnClickListener {
            val intent = Intent(context, CreateActivity::class.java)
            resultLauncher.launch(intent)
        }

        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(homePaging) {
            println("Jalanan ==> data $it")
            moreStoryAdapter.submitData(it)
        }
        super.observeData()
    }
}