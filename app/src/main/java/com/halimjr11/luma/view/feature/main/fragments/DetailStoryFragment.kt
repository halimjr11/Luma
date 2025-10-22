package com.halimjr11.luma.view.feature.main.fragments

import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import coil.load
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.FragmentDetailStoryBinding
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.ui.helper.visibleIf
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.main.di.loadDetailModule
import com.halimjr11.luma.view.feature.main.viewmodel.DetailViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class DetailStoryFragment : BaseFragment<FragmentDetailStoryBinding, ViewModel>(
    FragmentDetailStoryBinding::inflate
), AndroidScopeComponent {
    override val viewModel: DetailViewModel by viewModel()

    override val scope: Scope by fragmentScope()
    private val args: DetailStoryFragmentArgs by navArgs()

    init {
        loadDetailModule()
    }

    override fun setupUI() {
        viewModel.loadDetailStory(args.id)
        super.setupUI()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(detailStory) { state ->
            binding.progressLoading.visibleIf(state is UiState.Loading)
            binding.detailScroll.visibleIf(state is UiState.Success)
            binding.evDetail.visibleIf(state is UiState.Error)
            when (state) {
                is UiState.Success -> {
                    setupDetail(state.data)
                }

                is UiState.Error -> {
                    binding.evDetail.setMessageAndCallback(state.message) {
                        viewModel.loadDetailStory(args.id)
                    }
                }

                else -> {}
            }
        }

        launchAndCollect(isFavoriteFlow) {
            val color = if (it) {
                R.color.light_error
            } else {
                R.color.white
            }

            context?.let { ctx ->
                binding.fabFavorite.setColorFilter(
                    ContextCompat.getColor(ctx, color),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
        super.observeData()
    }

    private fun setupDetail(data: StoryDomain) = with(binding) {
        tvDetailName.text = data.name
        tvStoryUploader.text = data.name
        tvStoryDate.text = data.createdAt
        tvDetailDescription.text = data.description
        ivDetailPhoto.load(data.photoUrl)
        fabFavorite.setOnClickListener {
            viewModel.toggleFavorite(data)
        }
    }

}