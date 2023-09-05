package com.example.noteapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.ProductListAdapter
import com.example.noteapp.databinding.FragmentProductListingBinding
import com.example.noteapp.model.Product
import com.example.noteapp.utils.Resource
import com.example.noteapp.viewModels.NeoProductViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductListingFragment : Fragment() {
    private var _binding: FragmentProductListingBinding? = null
    private val binding get() = _binding!!
    private val productViewModel by viewModels<NeoProductViewModel>()

    private lateinit var listAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductListingBinding.inflate(inflater, container, false)
        listAdapter = ProductListAdapter(::productClick)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonProduct = arguments?.getString("product")
        if (jsonProduct != null) {
            val id = Gson().fromJson<Product>(jsonProduct, Product::class.java)
            id.let {
                productViewModel.getProductList(it.id.toString())
            }
        }
        binding.productList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        bindObserverData()
    }


    private fun productClick(product: Product){
        val bundle=Bundle()
        bundle.putString("productDetails",Gson().toJson(product))
        findNavController().navigate(R.id.action_productListingFragment_to_productDetailsFragment,bundle)
    }

    private fun bindObserverData() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                productViewModel.productLit.collect{
                    binding.productProgressBar.visibility = View.GONE
                    when (it) {
                        is Resource.Error -> {
                            Log.e("Error", "=" + it.message)
                        }

                        is Resource.Loading -> {
                            binding.productProgressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            Log.e("Data", "===" + it.data?.data)
                            listAdapter.submitList(it.data?.data)
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