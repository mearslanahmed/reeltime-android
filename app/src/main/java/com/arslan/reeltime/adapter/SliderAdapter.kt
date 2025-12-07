package com.arslan.reeltime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.arslan.reeltime.databinding.ViewholderSliderBinding
import com.arslan.reeltime.model.SliderItems
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class SliderAdapter(
    private var SliderItems: MutableList<SliderItems>, private val viewPager2: ViewPager2
): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private var context: Context? = null
    private val runnable: Runnable = Runnable{
        SliderItems.addAll(elements = SliderItems)
        notifyDataSetChanged()
    }
    inner class SliderViewHolder(private val binding: ViewholderSliderBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(sliderItems: SliderItems){
            context?.let{
                Glide.with(it)
                    .load(sliderItems.image)
                    .apply(
                        RequestOptions().transform(CenterCrop(),RoundedCorners(60))
                    )
                    .into(binding.imageSlider)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderViewHolder {
        context = parent.context
        val binding = ViewholderSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(SliderItems[position])
        if (position == SliderItems.size - 2){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = SliderItems.size
}