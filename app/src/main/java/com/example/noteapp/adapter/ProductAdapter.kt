package com.example.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.NoteItemBinding
import com.example.noteapp.model.ProductCategory
import com.example.noteapp.model.ProductImage

//class ProductAdapter(private val productClick: (ProductCategory) -> Unit) :RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

class ProductAdapter(private val productClick: (ProductCategory) -> Unit) :
    ListAdapter<ProductCategory, ProductAdapter.MyViewHolder>(ComparatorDiffUtil()) {

    private lateinit var noteItemBinding: NoteItemBinding

    class MyViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productCategory: ProductCategory) {
            binding.product=productCategory
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        noteItemBinding =
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(noteItemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        noteItemBinding.root.setOnClickListener {
             productClick(product)
        }
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<ProductCategory>() {


        override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductCategory,
            newItem: ProductCategory
        ): Boolean {
            return oldItem == newItem
        }

    }
}