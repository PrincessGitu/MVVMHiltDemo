package com.example.noteapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.ProductAdapter
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.model.ProductCategory
import com.example.noteapp.utils.Resource
import com.example.noteapp.viewModels.NeoUserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    private val neoUserViewModel by viewModels<NeoUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        productAdapter = ProductAdapter(::productClickHandel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        neoUserViewModel.getUserDetails()
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = productAdapter
        bindObserverData()

    }


    private fun productClickHandel(productData: ProductCategory) {
        val bundle=Bundle()
        bundle.putString("product",Gson().toJson(productData))
       findNavController().navigate(R.id.action_mainFragment_to_productListingFragment,bundle)
    }

    private fun bindObserverData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                neoUserViewModel.neoUserData.collect {
                    binding.progressBar.visibility = View.GONE
                    when (it) {
                        is Resource.Error -> {
                            Log.e("Error", "=" + it.message)
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            Log.e("Data", "===" + it.data?.data?.product_categories)
                            productAdapter.submitList(it.data?.data?.product_categories)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}