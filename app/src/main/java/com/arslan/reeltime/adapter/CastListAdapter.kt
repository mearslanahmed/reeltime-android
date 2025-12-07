package com.arslan.reeltime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.databinding.ViewholderCastBinding
import com.arslan.reeltime.model.Cast
import com.bumptech.glide.Glide

class CastListAdapter(private  val cast: ArrayList<Cast> ):
RecyclerView.Adapter<CastListAdapter.ViewHolder>(){

    private  var context: Context?= null
    inner class ViewHolder (private  val binding: ViewholderCastBinding):
    RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            context?.let {
                Glide.with(it)
                    .load(cast.PicUrl)
                    .into(binding.actorimage)

            }
            binding.nameTxt.text = cast.Actor
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding = ViewholderCastBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int = cast.size
}