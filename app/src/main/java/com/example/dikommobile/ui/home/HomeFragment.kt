package com.example.dikommobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dikommobile.adapter.ProductAdapter
import com.example.dikommobile.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProducts()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initProducts() {
        binding.pbProducts.visibility = View.VISIBLE
        homeViewModel.products.observe(viewLifecycleOwner, Observer {
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvProducts.adapter = ProductAdapter(it)
            binding.pbProducts.visibility =  View.GONE
        })
        homeViewModel.loadItems()
    }
}