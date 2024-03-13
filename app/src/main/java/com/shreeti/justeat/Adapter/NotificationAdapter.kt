package com.shreeti.justeat.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shreeti.justeat.databinding.FragmentNotificationBottomBinding
import com.shreeti.justeat.databinding.NotificationItemBinding

class NotificationAdapter(
    private val notifyTextView:ArrayList<String>,
    private val notifyImageView:ArrayList<Int>
):RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    inner class NotificationViewHolder(private val binding: NotificationItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notificationTextView.text=notifyTextView[position]
                notificationImageView.setImageResource(notifyImageView[position])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifyTextView.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }
}