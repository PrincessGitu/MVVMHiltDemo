package com.example.noteapp.adapter

import android.widget.RatingBar
import androidx.databinding.BindingAdapter

    @BindingAdapter("ratingValue")
    fun setRating(ratingBar: RatingBar, mVoteAverage: Double){
        ratingBar.rating=mVoteAverage.toFloat()
    }