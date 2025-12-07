package com.arslan.reeltime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.databinding.ViewholderGenreBinding

class GenreEachFilmAdapter(private  val items: List<String>):
    RecyclerView.Adapter<GenreEachFilmAdapter.ViewHolder>(){
    class ViewHolder (val binding: ViewholderGenreBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ViewholderGenreBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTxt.text = items[position]
    }

    override fun getItemCount(): Int =items.size

}