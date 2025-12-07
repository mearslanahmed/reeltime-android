package com.arslan.reeltime.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.activity.DetailFilmActivity
import com.arslan.reeltime.databinding.ViewholderFilmBinding
import com.arslan.reeltime.model.Film
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class FilmListAdapter(private  val items: ArrayList<Film>):
RecyclerView.Adapter<FilmListAdapter.ViewHolder>(){
    private var context:Context?=null
    inner class ViewHolder (private val binding: ViewholderFilmBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: Film){
            binding.nameTxt.text = item.Title
            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            Glide.with(context!!)
                .load(item.Poster)
                .apply(requestOptions)
                .into(binding.pic)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailFilmActivity::class.java)
                intent.putExtra("object", item)
                context!!.startActivity(intent)

            }
        }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding = ViewholderFilmBinding.inflate(
            LayoutInflater.from(context), parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}