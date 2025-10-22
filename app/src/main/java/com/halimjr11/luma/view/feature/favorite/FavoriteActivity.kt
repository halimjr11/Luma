package com.halimjr11.luma.view.feature.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.ActivityFavoriteBinding
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.Constants.DETAIL_ID_KEY
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.adapter.FavoriteAdapter
import com.halimjr11.luma.view.feature.favorite.di.loadFavoriteModule
import com.halimjr11.luma.view.feature.favorite.viewmodel.FavoriteViewModel
import com.halimjr11.luma.view.feature.main.MainActivity
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class FavoriteActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModel()
    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override val scope: Scope by activityScope()

    init {
        loadFavoriteModule()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        setupListener()
        observeData()
    }

    private fun setupUI() = with(binding) {
        rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupListener() = with(binding) {
        favoriteAdapter.setOnClickCallback {
            val intent = Intent(this@FavoriteActivity, MainActivity::class.java).apply {
                putExtra(DETAIL_ID_KEY, it.id)
            }
            startActivity(intent)
        }
    }


    private fun observeData() = with(viewModel) {
        launchAndCollect(favoriteStories) { state ->
            when (state) {
                is UiState.Success -> favoriteAdapter.submitList(state.data)
                is UiState.Error -> binding.evFavorite.setFavMessageAndCallback(
                    resources.getString(
                        R.string.empty_favorite_message
                    )
                )

                else -> Unit
            }
            binding.run {
                rvFavorite.isVisible = state is UiState.Success
                evFavorite.isVisible = state is UiState.Error
            }
        }
    }
}