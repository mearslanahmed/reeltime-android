package com.arslan.reeltime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.R
import com.arslan.reeltime.databinding.ItemDateBinding

class DateAdapter(private val timeSlots: List<String>):
    RecyclerView.Adapter<DateAdapter.ViewHolder>(){
        private  var selectedPosition = -1
        private var lastSelectedPosition = -1

    inner class ViewHolder(private var binding: ItemDateBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            val dateParts = date.split("/")
            if (dateParts.size == 3) {
                binding.dayTxt.text = dateParts[0]
                binding.dayMonthTxt.text = dateParts[1] + " " + dateParts[2]

                if (selectedPosition == bindingAdapterPosition) {
                    binding.mainLayout.setBackgroundResource(R.drawable.orange_bg)
                    binding.dayTxt.setTextColor(binding.root.context.getColor(R.color.black))
                    binding.dayMonthTxt.setTextColor(binding.root.context.getColor(R.color.black))

                } else {
                    binding.mainLayout.setBackgroundResource(R.drawable.light_black_bg)
                    binding.dayTxt.setTextColor(binding.root.context.getColor(R.color.white))
                    binding.dayMonthTxt.setTextColor(binding.root.context.getColor(R.color.white))
                }

                binding.root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        lastSelectedPosition = selectedPosition
                        selectedPosition = position
                        notifyItemChanged(lastSelectedPosition)
                        notifyItemChanged(selectedPosition)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount(): Int = timeSlots.size

    fun getSelectedDate(): String? {
        return if (selectedPosition != -1) timeSlots[selectedPosition] else null
    }
}