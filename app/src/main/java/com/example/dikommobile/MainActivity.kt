package com.example.dikommobile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dikommobile.databinding.ActivityMainBinding
import com.example.dikommobile.databinding.FragmentMainBinding
import com.example.dikommobile.ui.cart.CartFragment
import com.example.dikommobile.ui.home.HomeFragment
import com.example.dikommobile.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


//    private fun replaceFragment(fragment: Fragment){
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.nav_host, fragment)
//        fragmentTransaction.commit()
//    }

}