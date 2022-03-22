package com.example.hw16.ui.signin

import android.content.Context
import android.graphics.Color.BLACK
import android.graphics.Color.RED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.hw16.R
import com.example.hw16.data.local.FileType
import com.example.hw16.databinding.FragmentSignInBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.User
import com.example.hw16.ui.App
import com.example.hw16.ui.ViewModelMain
import com.example.hw16.utils.createImageLauncher

class FragmentSignIn : Fragment() {
    private lateinit var imageDialog: AlertDialog
    private val navController by lazy {
        findNavController()
    }
    private val model: ViewModelMain by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private lateinit var cameraLauncher: ActivityResultLauncher<Void>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        dialogInit()
        with(binding) {
            signInBtn.setOnClickListener {
                if (check()) {
                    model.signIn(User(signInUsername.text.toString(), signInPass.text.toString()))
                    val navOption = NavOptions.Builder()
                        .setPopUpTo(R.id.fragmentLogin, true)
                        .build()
                    navController.navigate(
                        FragmentSignInDirections.actionFragmentSignInToFragmentHome(),
                        navOptions = navOption
                    )
                }
            }
            signInLogin.setOnClickListener {
                requireActivity().onBackPressed()
            }
            val red = RED
            val black = BLACK
            signInRepass.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let {
                        if (it.toString() != signInPass.text.toString()) {
                            setTextColor(red)
                        } else {
                            setTextColor(black)
                        }
                    }
                }
            }
            signInPass.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let {
                        if (it.length < 8) {
                            setTextColor(red)
                        } else {
                            setTextColor(black)
                        }
                    }
                    signInRepass.also {
                        if (text.toString() != it.text.toString()) {
                            it.setTextColor(red)
                        } else {
                            it.setTextColor(black)
                        }
                    }
                }
            }
            signInImagePicker.setOnClickListener {
                imageDialog.show()
            }
            signInImageRetry.setOnClickListener {
                imageDialog.show()
            }
            signInImageRemover.setOnClickListener {
                model.removeFile(uri!!)
                uri = null
            }
        }
    }

    private fun dialogInit() {
        imageDialog = AlertDialog.Builder(requireContext())
            .setItems(arrayOf("Camera", "Gallery")) { _, position ->
                if (position == 0) {
                    cameraLauncher.launch(null)
                } else {
                    galleryLauncher.launch("image/*")
                }
            }.create()
    }

    private fun check(): Boolean {
        var result = true
        fun setError(editText: EditText, boolean: Boolean) {
            editText.error = if (boolean) {
                result = false
                editText.requestFocus()
                "Invalid!!"
            } else {
                null
            }
        }

        val red = RED
        with(binding) {
            setError(signInUsername, signInUsername.text!!.isBlank())
            setError(signInPass, signInPass.currentTextColor == red)
            setError(signInRepass, signInRepass.currentTextColor == red)
        }
        return result
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val (camera, gallery) = createImageLauncher({ bitmap ->
            if (bitmap != null) {
                binding.uri = model.saveToFile(requireContext(), FileType.IMAGE_FILE, bitmap)
            }
        }) {
            if (it != null) {
                binding.uri = it.path
            }
        }
        cameraLauncher = camera
        galleryLauncher = gallery
    }
}