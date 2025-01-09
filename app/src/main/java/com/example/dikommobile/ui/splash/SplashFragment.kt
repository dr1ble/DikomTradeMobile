package com.example.dikommobile.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dikommobile.R
import com.example.dikommobile.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private lateinit var splashViewModel: SplashViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashViewModel =
            ViewModelProvider(this).get(SplashViewModel::class.java)

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Запускаем проверку авторизации
        splashViewModel.checkUserAuthorization(findNavController())

        // Наблюдаем за изменением состояния навигации
        splashViewModel.navigateTo.observe(viewLifecycleOwner) { destinationId ->
            destinationId?.let {
                findNavController().navigate(it)
                splashViewModel.onNavigationComplete() // Сбрасываем состояние навигации
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
