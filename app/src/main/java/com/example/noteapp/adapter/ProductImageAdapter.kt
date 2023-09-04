package com.example.noteapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.databinding.ProductDetailsImageItemBinding
import com.example.noteapp.model.ProductImage
import kotlin.reflect.KFunction2

class ProductImageAdapter(
    private val imageClick: KFunction2<ProductImage, Int, Unit>,
    private val productImageList: List<ProductImage>
) :
    RecyclerView.Adapter<ProductImageAdapter.MyViewHolder>() {

    private lateinit var imageItemBinding: ProductDetailsImageItemBinding
    private var selectedPosition = -1

    class MyViewHolder(private val binding: ProductDetailsImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productImage: ProductImage) {
            binding.imgae = productImage
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        imageItemBinding =
            ProductDetailsImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(imageItemBinding)
    }

    override fun getItemCount(): Int {
        return productImageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productImageList[position])
        imageItemBinding.root.setOnClickListener {
            imageClick(productImageList[position], position)
            if (selectedPosition != holder.adapterPosition) {
                selectedPosition=holder.adapterPosition
                notifyDataSetChanged()
            }


        }
        if(selectedPosition==position){
            imageItemBinding.productImgaeItem.setBackgroundResource(R.drawable.img_broder_red)
        }else{
            imageItemBinding.productImgaeItem.setBackgroundResource(R.drawable.img_broder_gray)
        }
    }


}