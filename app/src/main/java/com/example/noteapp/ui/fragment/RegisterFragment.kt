package com.example.noteapp.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentRegisterBinding
import com.example.noteapp.model.NeoUserRequest
import com.example.noteapp.utils.Constants
import com.example.noteapp.utils.Resource
import com.example.noteapp.utils.TokenManager
import com.example.noteapp.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View ?{
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

//        if (tokenManager.getToken() != null) {
//            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
//        }

        return binding.root
    }//onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {

            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
//            val validationResult = validateUserInput()
//            if(validationResult.first){
//                authViewModel.registerUser(getUserRequest())
//            }else{
//                binding.txtError.text = validationResult.second
//            }

        }
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObserverData()

    }//onViewCreated

    private fun bindObserverData(){
        authViewModel.userResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility= View.GONE
            when(it){
                is Resource.Error ->{
                    Log.e("Token",""+it.data!!.data.access_token)
                    tokenManager.saveToken(it.data!!.data.access_token)
                    binding.txtError.text=it.message
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
            }
        })
    }

    private fun validateUserInput():Pair<Boolean,String>{
        var result=Pair(true,"")
        val request= getUserRequest()
        if(TextUtils.isEmpty(request.email) || TextUtils.isEmpty(request.password) || TextUtils.isEmpty(request.first_name)){
            result = Pair(false, "Please provide the credentials")
        }
        else if(!Constants.isValidEmail(request.email)){
            result = Pair(false, "Email is invalid")
        }
        else if(!TextUtils.isEmpty(request.password) && request.password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        else if(!request.password.equals(request.confirm_password)){
            result = Pair(false, "Password & Confirm Password must be same")
        }
        return result

    }

    private fun getUserRequest(): NeoUserRequest {
        return binding.run {
            NeoUserRequest(
                txtUsername.text.toString(),
                "pawar",
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                txtPassword.text.toString(),
                "F",
                "1234567890"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}