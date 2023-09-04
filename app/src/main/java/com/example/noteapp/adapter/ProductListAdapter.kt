package com.example.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ProductListItemBinding
import com.example.noteapp.model.Product


class ProductListAdapter(private val productDetailsClick: (Product) -> Unit) :
    ListAdapter<Product, ProductListAdapter.MyViewHolder>(ComparatorDiffUtil()) {

    private lateinit var bindingProduct: ProductListItemBinding

    class MyViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
           fun bind(pData:Product){
               binding.product=pData
               binding.executePendingBindings()
           }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        bindingProduct = ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(bindingProduct)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val productData=getItem(position)
        holder.bind(productData)
        bindingProduct.root.setOnClickListener{
            productDetailsClick(productData)
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
}