package com.halimjr11.luma.view.feature.auth.fragments

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.halimjr11.luma.databinding.FragmentLoginBinding
import com.halimjr11.luma.ui.base.BaseFragment
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.auth.di.loadLoginModule
import com.halimjr11.luma.view.feature.auth.viewmodel.LoginViewModel
import com.halimjr11.luma.view.feature.main.MainActivity
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate),
    AndroidScopeComponent {
    override val viewModel: LoginViewModel by viewModel()
    override val scope: Scope by fragmentScope()

    init {
        loadLoginModule()
    }

    override fun setupListeners() = with(binding) {
        btnRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        btnSignIn.setOnClickListener {
            val email = edLoginEmail.getText()
            val password = edLoginPassword.getText()
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.doLogin(email, password)
            }
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(loginState) {
            when (it) {
                is UiState.Success -> {
                    activity?.startActivity(Intent(activity, MainActivity::class.java))
                }

                is UiState.Error -> Snackbar.make(
                    binding.root,
                    it.message,
                    Snackbar.LENGTH_SHORT
                ).show()

                else -> {}
            }
        }
        super.observeData()
    }
}