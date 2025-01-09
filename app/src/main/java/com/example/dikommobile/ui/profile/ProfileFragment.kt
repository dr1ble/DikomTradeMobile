package com.example.dikommobile.ui.profile

import SharedPrefsHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dikommobile.R
import com.example.dikommobile.databinding.FragmentProfileBinding
import com.example.dikommobile.ui.login.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUIBasedOnAuthState()

        viewModel.email.observe(viewLifecycleOwner) {
            binding.tvProfileEmail.text = it
        }

        viewModel.profileName.observe(viewLifecycleOwner) {
            binding.tvProfileName.text = it
        }

        binding.ivLogout.setOnClickListener{
            SharedPrefsHelper.clear()
            Firebase.auth.signOut()
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
            navController.navigate(R.id.action_mainFragment_to_navigation_welcome)
        }

        binding.btnGoAuthFromProfile.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
            navController.navigate(R.id.action_mainFragment_to_navigation_login)
        }
    }

    private fun updateUIBasedOnAuthState() {
        if (SharedPrefsHelper.isUserAuthorized()) {
            // Пользователь авторизован

        } else {
            // Пользователь не авторизован
            binding.flGoAuthorizeSection.visibility = View.VISIBLE
            binding.llProfileSection.visibility = View.INVISIBLE
            binding.ivLogout.visibility = View.GONE
            binding.ivProfileIcon.visibility = View.INVISIBLE
//            binding.ivProfileIcon.visibility = View.INVISIBLE
//            binding.llProfileSection.visibility = View.INVISIBLE
//            binding.llMenuFaq.visibility = View.INVISIBLE
//            binding.llMenuUseTerms.visibility = View.INVISIBLE
//            binding.llMenuFaq.visibility = View.INVISIBLE

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}