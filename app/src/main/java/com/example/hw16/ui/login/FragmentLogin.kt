package com.example.hw16.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.hw16.R
import com.example.hw16.databinding.FragmentLoginBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.ui.App
import com.example.hw16.ui.ProgressResult.*
import com.example.hw16.ui.ViewModelMain
import com.example.hw16.utils.popUpToNavigate
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FragmentLogin : Fragment() {
    private val model: ViewModelMain by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    lateinit var binding: FragmentLoginBinding
    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        with(binding) {
            with(model) {
                error.observe(viewLifecycleOwner) {
                    if (it == null) return@observe
                    error.value = null
                    if (it == SUCCESS) {
                        navController.popUpToNavigate(
                            R.id.fragmentLogin,
                            true,
                            FragmentLoginDirections.actionFragmentLoginToFragmentHome()
                        )
                    }
                    hasError = it != SUCCESS
                }
            }
            loginBtn.setOnClickListener {
                if (check()) {
                    model.login(loginUsername.text.toString(), loginPass.text.toString())
                }
            }
            loginSignIn.setOnClickListener {
                val navOption = NavOptions.Builder()
                    .setPopUpTo(R.id.fragmentLogin, false)
                    .build()
                navController.navigate(
                    FragmentLoginDirections.actionFragmentLoginToFragmentSignIn(),
                    navOptions = navOption
                )
            }
        }
    }

    private fun check(): Boolean {
        var result = true
        fun innerCheck(layout: TextInputLayout, editText: TextInputEditText) {
            layout.helperText = if (editText.text!!.isBlank()) {
                result = false
                "*Required!!"
            } else {
                null
            }
        }
        with(binding) {
            innerCheck(loginUsernameLayout, loginUsername)
            innerCheck(loginPassLayout, loginPass)
        }
        return result
    }
}