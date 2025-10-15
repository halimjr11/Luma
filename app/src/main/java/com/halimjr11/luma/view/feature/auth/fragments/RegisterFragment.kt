package com.halimjr11.luma.view.feature.auth.fragments

import com.halimjr11.luma.databinding.FragmentRegisterBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.auth.di.loadRegisterModule
import com.halimjr11.luma.view.feature.auth.viewmodel.RegisterViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel>(FragmentRegisterBinding::inflate),
    AndroidScopeComponent {
    override val viewModel: RegisterViewModel by viewModel()
    override val scope: Scope by fragmentScope()

    init {
        loadRegisterModule()
    }

    override fun setupListeners() = with(binding) {
        btnToLogin.setOnClickListener {
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        }
        btnSignUp.setOnClickListener {
            val name = edRegisterName.getText()
            val email = edRegisterEmail.getText()
            val password = edRegisterPassword.getText()
            viewModel.registerUser(name, email, password)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(registerState) {
            when (it) {
                is UiState.Success -> {
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(it.data)
                }

                else -> {}
            }
        }
        super.observeData()
    }
}