package com.example.noteapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ProductListItemBinding
import com.example.noteapp.model.Product
import java.util.Collections


class ProductListAdapter(private val productDetailsClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductListAdapter.MyViewHolder>(), Filterable {

    private lateinit var bindingProduct: ProductListItemBinding
     var productList:ArrayList<Product> = ArrayList()
     var productFilterList:ArrayList<Product> = ArrayList()

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

    override fun getItemCount(): Int {
        Log.e("ProductSize","=="+productFilterList.size)
        return productFilterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productFilterList[position])

        bindingProduct.root.setOnClickListener{
            productDetailsClick(productFilterList[holder.adapterPosition])
        }

    }

    fun getSorted(){
//        // sort by high to low
//        productFilterList.sortByDescending {
//                it.cost
//        }

        //sort by low to high
        productFilterList.sortBy {
            it.cost
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addProductData(list: List<Product>) {
        productList = list as ArrayList<Product>
        productFilterList = productList
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
       return object :Filter(){
           override fun performFiltering(constraint: CharSequence?): FilterResults {
               val charString = constraint?.toString() ?: ""

//               val charSearch = constraint.toString()
//               if (charSearch.isEmpty()) {
//                   productFilterList = productList
//               } else {
//                   val resultList = ArrayList<Product>()
//                   for (row in productList) {
//                       if((row.name.contains(constraint!!)) or(row.producer.contains(constraint))){
//                           resultList.add(row)
//                       }
//                   }
//                   productFilterList = resultList
//               }
//               val filterResults = FilterResults()
//               filterResults.values = productFilterList
//               return filterResults

               if (charString.isEmpty()) productFilterList = productList else {
                   val filteredList = ArrayList<Product>()
                   productList.filter {
                       (it.name.lowercase().contains(charString.lowercase())) or(it.producer.lowercase().contains(charString.lowercase()))
                   }.forEach {
                       filteredList.add(it)
                   }
                   productFilterList = filteredList



               }
               return FilterResults().apply { values = productFilterList }

           }
           @SuppressLint("NotifyDataSetChanged")
           @Suppress("UNCHECKED_CAST")
           override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               productFilterList = if (results?.values == null)
                   ArrayList()
               else {
                   results.values as ArrayList<Product>
               }
               notifyDataSetChanged()

           }
       }
    }
}