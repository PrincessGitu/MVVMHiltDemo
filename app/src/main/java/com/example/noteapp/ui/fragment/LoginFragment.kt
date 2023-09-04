package com.example.noteapp.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentLoginBinding
import com.example.noteapp.utils.Constants
import com.example.noteapp.utils.Resource
import com.example.noteapp.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }//onCreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUser(email, password)
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnSignUp.setOnClickListener {
                 findNavController().popBackStack()
        }

        bindObserverData()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        var result = Pair(true, "")
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Constants.isValidEmail(email.toString())) {
            result = Pair(false, "Email is invalid")
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }

        return result

    }

    private fun bindObserverData() {
        authViewModel.userResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is Resource.Error -> {
                    binding.txtError.text = it.message
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}