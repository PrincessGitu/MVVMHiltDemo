package com.example.noteapp.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.noteapp.adapter.ProductImageAdapter
import com.example.noteapp.databinding.FragmentProductDetailsBinding
import com.example.noteapp.model.Product
import com.example.noteapp.model.ProductImage
import com.example.noteapp.utils.Resource
import com.example.noteapp.viewModels.NeoProductDetailsViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {


    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding get() = _binding!!

    private lateinit var imageAdapter: ProductImageAdapter

    lateinit var imageUrl:String


    private val productDetailsViewModel by viewModels<NeoProductDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonProduct = requireArguments().getString("productDetails")
        val productData = Gson().fromJson<Product>(jsonProduct, Product::class.java)
        productData.let {
            productDetailsViewModel.getProductDetails(it.id.toString())
        }


        bindObserverData()

        binding.productDetailSharing.setOnClickListener {
            val uri = Uri.parse(imageUrl)

              shareImage(imageUrl)
        }
    }


    private fun shareImage(uriToImage: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
      //  shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage)
       // shareIntent.type = "image/jpeg"
        shareIntent. putExtra(Intent.EXTRA_TEXT, uriToImage)
        shareIntent. type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, null))
    }



    private fun bindObserverData() {
        productDetailsViewModel.productDetailsList.observe(viewLifecycleOwner, Observer {
            binding.productProgressBar.visibility = View.GONE
            when (it) {
                is Resource.Error -> {
                    Log.e("Error", "=" + it.message)
                }

                is Resource.Loading -> {
                    binding.productProgressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    Log.e("DetailsData", "===" + it.data?.data)
                    val pData = it.data?.data!!
                    pData.let {
                        binding.apply {
                            ProductDetailName.text = pData.name
                            productDetailPrice.text = "Rs." + pData.cost.toString()
                            productDetailDescription.text = pData.description
                            productDetailProducer.text = pData.producer
                            productDetailRatingBar.rating = pData.rating.toFloat()
                        }

                        imageAdapter = ProductImageAdapter(::onImageClick, pData.product_images)
                        binding.productDetailRecyclerview.apply {
                            layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                            adapter = imageAdapter
                        }
                        val url = pData.product_images[0].image
                        imageUrl=pData.product_images[0].image
                        Glide.with(requireContext()).load(url).into(binding.productDetailImage)


                    }


                }
            }
        })
    }


    private fun onImageClick(imageData: ProductImage, position: Int) {
        imageUrl=imageData.image
        Glide.with(requireContext()).load(imageData.image).into(binding.productDetailImage)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}