package com.example.noteapp.ui.fragment

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.ItemAdapter
import com.example.noteapp.adapter.ProductListAdapter
import com.example.noteapp.databinding.FragmentProductListingBinding
import com.example.noteapp.model.Product
import com.example.noteapp.utils.Resource
import com.example.noteapp.viewModels.NeoProductViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Arrays


@AndroidEntryPoint
class ProductListingFragment : Fragment() {
    private var _binding: FragmentProductListingBinding? = null
    private val binding get() = _binding!!
    private val productViewModel by viewModels<NeoProductViewModel>()

    private lateinit var listAdapter: ProductListAdapter
    var productList:ArrayList<Product> = ArrayList()
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

        // Add options menu to the toolbar
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionSearch -> {

                        // getting search view of our item.
                        val searchView: SearchView = menuItem.actionView as SearchView
                        // below line is to call set on query text listener method.
                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                            android.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                return false
                            }
                            override fun onQueryTextChange(msg: String): Boolean {
                                // inside on query text change method we are
                                // calling a method to filter our recycler view.
                                Log.e("search: t1: ", msg)
                                listAdapter.filter.filter(msg)
                                return false
                            }
                        })
                        true
                    }

                    R.id.actionSort -> {
                        listAdapter.getSorted()
                        listAdapter.notifyDataSetChanged()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

    }



    private fun productClick(product: Product) {
        val bundle = Bundle()
        bundle.putString("productDetails", Gson().toJson(product))
        findNavController().navigate(
            R.id.action_productListingFragment_to_productDetailsFragment,
            bundle
        )
    }

    private fun bindObserverData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.productLit.collect {
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
                          //  productList.add(it.data?.data!!)
                            listAdapter.addProductData(it.data?.data!!)
                            listAdapter.notifyDataSetChanged()
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