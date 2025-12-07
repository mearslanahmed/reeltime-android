package com.arslan.reeltime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.R
import com.arslan.reeltime.databinding.ItemTimeBinding

class TimeAdapter(private val timeSlots: List<String>) :
    RecyclerView.Adapter<TimeAdapter.ViewHolder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(private var binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {

            binding.TextViewTime.text = time

            if (selectedPosition == bindingAdapterPosition) {
                binding.TextViewTime.setBackgroundResource(R.drawable.yellow_bg)
                binding.TextViewTime.setTextColor(binding.root.context.getColor(R.color.black))


            } else {
                binding.TextViewTime.setBackgroundResource(R.drawable.light_black_bg)
                binding.TextViewTime.setTextColor(binding.root.context.getColor(R.color.white))
            }

            binding.root.setOnClickListener {
                val currentPosition = bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition
                    selectedPosition = currentPosition
                    if (lastSelectedPosition != -1) {
                        notifyItemChanged(lastSelectedPosition)
                    }
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount(): Int = timeSlots.size

    fun getSelectedTime(): String? {
        return if (selectedPosition != -1) timeSlots[selectedPosition] else null
    }
}