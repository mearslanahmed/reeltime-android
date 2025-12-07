package com.arslan.reeltime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arslan.reeltime.R
import com.arslan.reeltime.databinding.ItemSeatBinding
import com.arslan.reeltime.model.Seat

class SeatListAdapter(private val seatList: List<Seat>,
                      private val context: Context,
                      private val selectedSeats: SelectedSeats

):
RecyclerView.Adapter<SeatListAdapter.ViewHolder>(){
    private val selectedSeatName = ArrayList<String>()
    class ViewHolder(val binding: ItemSeatBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemSeatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seat = seatList[position]
        holder.binding.seatTxt.text = seat.name

        holder.binding.seatTxt.isClickable = true

        when (seat.status) {
            Seat.SeatStatus.AVAILABLE -> {
                holder.binding.seatTxt.setBackgroundResource(R.drawable.ic_seat_available)
                holder.binding.seatTxt.setTextColor(context.getColor(R.color.white))
            }

            Seat.SeatStatus.SELECTED -> {
                holder.binding.seatTxt.setBackgroundResource(R.drawable.ic_seat_selected)
                holder.binding.seatTxt.setTextColor(context.getColor(R.color.black))
            }

            Seat.SeatStatus.UNAVAILABLE -> {
                holder.binding.seatTxt.setBackgroundResource(R.drawable.ic_seat_unavailable)
                holder.binding.seatTxt.setTextColor(context.getColor(R.color.grey))

                holder.binding.seatTxt.isEnabled = false
            }
        }

        holder.binding.seatTxt.setOnClickListener {
            when (seat.status) {
                Seat.SeatStatus.AVAILABLE -> {
                    seat.status = Seat.SeatStatus.SELECTED
                    selectedSeatName.add(seat.name)
                    notifyItemChanged(position)
                }

                Seat.SeatStatus.SELECTED -> {
                    seat.status = Seat.SeatStatus.AVAILABLE
                    selectedSeatName.remove(seat.name)
                    notifyItemChanged(position)
                }
                else -> {}
            }
            val selected = selectedSeatName.joinToString (",")
            selectedSeats.Return(selected, selectedSeatName.size)
        }
    }

    override fun getItemCount(): Int = seatList.size

    interface SelectedSeats{
        fun Return (slectedName: String, num: Int)
    }
}