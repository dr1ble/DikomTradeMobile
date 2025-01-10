package com.example.dikommobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dikommobile.R
import com.example.dikommobile.databinding.FragmentMainBinding
import com.example.dikommobile.ui.cart.CartFragment
import com.example.dikommobile.ui.home.HomeFragment
import com.example.dikommobile.ui.profile.ProfileFragment

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onStart() {
        super.onStart()

        val navController = Navigation.findNavController(requireActivity(), R.id.mainContainerFragment)

        // Настроить BottomNavigationView с NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Обработчик для кнопок навигации
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Когда выбран домашний фрагмент, сбрасываем стек и навигацию
                    navController.popBackStack(R.id.homeFragment, false)  // Удаляем все фрагменты до главного
                    navController.navigate(R.id.homeFragment)  // Переход к главной странице
                    return@setOnItemSelectedListener true
                }
                R.id.cartFragment-> {
                    // Переключаемся на корзину
                    navController.navigate(R.id.cartFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    // Переключаемся на профиль
                    navController.navigate(R.id.profileFragment)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Здесь можно добавить логику для других элементов UI, если требуется
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
