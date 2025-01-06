package com.example.dikommobile.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dikommobile.R
import com.example.dikommobile.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer для отображения сообщений об ошибке или успехе
        registerViewModel.registrationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is RegisterViewModel.RegistrationStatus.Success -> {
                    Toast.makeText(requireContext(), "Регистрация успешна", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.action_navigation_register_to_mainFragment)
                }

                is RegisterViewModel.RegistrationStatus.Error -> {
                    Toast.makeText(requireContext(), status.message, Toast.LENGTH_LONG).show()
                }

                is RegisterViewModel.RegistrationStatus.Loading -> {
                    // Здесь можно отобразить индикатор загрузки, если нужно
                }
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Не все обязательные поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                registerViewModel.registerUser(name, email, password)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
