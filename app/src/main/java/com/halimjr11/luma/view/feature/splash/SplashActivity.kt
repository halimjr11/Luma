package com.halimjr11.luma.view.feature.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.halimjr11.luma.databinding.ActivitySplashBinding
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.auth.AuthActivity
import com.halimjr11.luma.view.feature.main.MainActivity
import com.halimjr11.luma.view.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModel()
    override val scope: Scope by activityScope()

    init {
        loadSplashModule()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        launchAndCollect(viewModel.splashState) {
            if (it is UiState.Success) {
                animateLogo(it.data)
            }
        }
    }

    private fun animateLogo(isLoggedIn: Boolean) = with(binding) {
        val fadeIn = ObjectAnimator.ofFloat(splashLogo, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 0.8f, 1f)
        val scaleY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 0.8f, 1f)

        AnimatorSet().run {
            playTogether(fadeIn, scaleX, scaleY)
            duration = 1200
            interpolator = DecelerateInterpolator()
            start()

            addListener(onEnd = {
                val pulseX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 1f, 1.05f, 1f)
                val pulseY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 1f, 1.05f, 1f)
                AnimatorSet().apply {
                    playTogether(pulseX, pulseY)
                    duration = 800
                    startDelay = 300
                    start()
                }

                lifecycleScope.launch {
                    delay(2500)
                    val targetActivity =
                        if (isLoggedIn) MainActivity::class.java else AuthActivity::class.java
                    startActivity(Intent(this@SplashActivity, targetActivity))
                    finish()
                }
            })
        }
    }
}
