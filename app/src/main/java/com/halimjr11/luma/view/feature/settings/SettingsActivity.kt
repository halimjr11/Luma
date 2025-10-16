package com.halimjr11.luma.view.feature.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.ActivitySettingsBinding
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.Constants.LANG_EN
import com.halimjr11.luma.utils.Constants.LANG_ID
import com.halimjr11.luma.view.feature.auth.AuthActivity
import com.halimjr11.luma.view.feature.settings.di.loadSettingsModule
import com.halimjr11.luma.view.feature.settings.viewmodel.SettingsViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class SettingsActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override val scope: Scope by activityScope()

    init {
        loadSettingsModule()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.settingsMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupListeners()
        observeData()
    }

    private fun setupListeners() = binding.run {
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            switchTheme.thumbIconDrawable = if (isChecked) {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_moon, null)
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_sun, null)
            }
            viewModel.toggleDarkMode(isChecked)
        }
        switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            switchLanguage.thumbIconDrawable = if (isChecked) {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_indonesia, null)
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_english, null)
            }
            viewModel.toggleLanguage(if (isChecked) LANG_ID else LANG_EN)
        }
        actionLogout.setOnClickListener {
            viewModel.doLogout()
        }
    }

    private fun observeData() = viewModel.run {
        launchAndCollect(viewModel.isDarkModeEnabled) { isEnabled ->
            binding.run {
                if (switchTheme.isChecked != isEnabled) {
                    switchTheme.isChecked = isEnabled
                }
            }
        }
        launchAndCollect(currentLanguage) {
            binding.run {
                switchLanguage.isChecked = it == LANG_ID
            }
        }
        launchAndCollect(logout) { loggedOut ->
            println("Jalanan ==> check logout $loggedOut")
            if (loggedOut) {
                val intent = Intent(this@SettingsActivity, AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
    }
}
