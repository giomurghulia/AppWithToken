package com.example.appwithtoken.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.appwithtoken.Resource
import com.example.appwithtoken.User
import com.example.appwithtoken.WelcomeFragmentDirections
import com.example.appwithtoken.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LogInViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateErrorStates()
        checkSession()

        binding.logInButton.setOnClickListener {
            val user = checkForm()
            if (user != null) {
                viewModel.logIn(user)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logInState.collect {
                    when (it) {
                        is Resource.Success -> {
                            Toast.makeText(
                                context,
                                "Success " + it.items.token,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(
                                LogInFragmentDirections.actionLoginFragmentToHomeFragment(checkForm()!!)
                            )
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }


    }

    private fun checkForm(): User? {
        val email = binding.emailInput.text?.toString()
        val pass = binding.passInput.text?.toString()

        var checker = true

        if (email.isNullOrEmpty()) {
            binding.emailLayout.error = "Email Is Empty"
            checker = false
        } else if (!isValidEmail(email)) {
            binding.emailLayout.error = "Incorrect Email"
            checker = false
        }

        if (pass.isNullOrBlank()) {
            binding.passLayout.error = "Fill Password"
            checker = false
        }

        if (checker) return User(email!!, pass!!)

        return null
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun updateErrorStates() = with(binding) {
        emailInput.doAfterTextChanged {
            emailLayout.error = null
        }

        passInput.doAfterTextChanged {
            passLayout.error = null
        }

    }

    private fun checkSession() {
        if (viewModel.hasSession()) {
            findNavController().navigate(
                LogInFragmentDirections.actionLoginFragmentToHomeFragment(checkForm()!!)
            )
        }
    }
}