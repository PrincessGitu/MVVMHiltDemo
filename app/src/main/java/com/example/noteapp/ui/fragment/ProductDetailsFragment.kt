package com.example.noteapp.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

import java.io.IOException
import java.net.URL


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding get() = _binding!!

    private lateinit var imageAdapter: ProductImageAdapter

    //picked image uri will be saved in it
    private var imageUri: Uri? = null
    private lateinit var imgUrl:String
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
            startStoragePermissionRequest()
        }
    }


    // Function to check and request permission.
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getUri()
        } else {
            Toast.makeText(requireContext(),"Permission Denied by user",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startStoragePermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                        imgUrl=pData.product_images[0].image
                        Glide.with(requireContext()).load(url).into(binding.productDetailImage)

                    }
                }
            }
        })
    }


    private fun onImageClick(imageData: ProductImage, position: Int) {
        imgUrl=imageData.image
        Glide.with(requireContext()).load(imageData.image).into(binding.productDetailImage)

    }

    private fun getUri(){
        lifecycleScope.launch(Dispatchers.IO){
            try {
                val url = URL(imgUrl)
                val loadedImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                val path: String = Images.Media.insertImage(requireContext().contentResolver, loadedImage, "", null)
                imageUri = Uri.parse(path)
                shareImage()
            } catch (e: IOException) {
                Log.e("shareImage","="+e.message)
                Toast.makeText(requireContext(), "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image")
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.type = "image/*"
        startActivity(Intent.createChooser(intent, "Share image via..."))

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}