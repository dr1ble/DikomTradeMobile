package com.example.dikommobile.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.dikommobile.R

class SplashViewModel : ViewModel() {

    private val _navigateTo = MutableLiveData<Int?>()
    val navigateTo: LiveData<Int?> get() = _navigateTo

    fun checkUserAuthorization(navController: NavController) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPrefsHelper.isUserAuthorized()) {
                // Переход к MainFragment, если пользователь авторизован
                _navigateTo.value = R.id.action_splashFragment_to_mainFragment
            } else {
                // Переход к WelcomeFragment, если пользователь не авторизован
                _navigateTo.value = R.id.action_splashFragment_to_navigation_welcome
            }
        }, 1500) // Задержка 1 секунды
    }

    fun onNavigationComplete() {
        _navigateTo.value = null
    }
}
