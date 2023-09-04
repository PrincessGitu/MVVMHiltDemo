package com.example.noteapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.databinding.ProductDetailsImageItemBinding
import com.example.noteapp.model.ProductImage


class ItemAdapter(
  private val favoriteListener: (ProductImage, Boolean,Int) -> Unit
) : ListAdapter<ProductImage, ItemViewHolder>(ItemDiffCallback()) {

  lateinit var binding:ProductDetailsImageItemBinding
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
     binding = ProductDetailsImageItemBinding.inflate(
      LayoutInflater.from(parent.context), parent, false)

    return ItemViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    Log.e("onBindViewHolder","onBindViewHolder")
    holder.bind(getItem(position))

  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {

    if (payloads.isEmpty()) {
      super.onBindViewHolder(holder, position, payloads)
    } else {
      if (payloads[0] == true) {
        holder.bindFavoriteState(getItem(position).isSelected)
      }
    }
  }
}

class ItemViewHolder(private val binding: ProductDetailsImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

  fun bind(item: ProductImage) {
    binding.imgae = item


  }

  fun bindFavoriteState(isSelected: Boolean) {
   // binding.favoriteIcon.isSelected = isFavorite
    if(isSelected){
      binding.productImgaeItem.setBackgroundResource(R.drawable.img_broder_red)
    }else{
      binding.productImgaeItem.setBackgroundResource(R.drawable.img_broder_gray)
    }

  }
}

class ItemDiffCallback : DiffUtil.ItemCallback<ProductImage>() {
  override fun areItemsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
    return oldItem == newItem
  }

  override fun getChangePayload(oldItem: ProductImage, newItem: ProductImage): Any? {
    return if (oldItem.isSelected != newItem.isSelected) true else null
  }
}

