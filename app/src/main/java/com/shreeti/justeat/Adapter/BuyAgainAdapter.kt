package com.shreeti.justeat.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shreeti.justeat.databinding.BuyAgainItemBinding

class BuyAgainAdapter(
    private val buyAgainFoodName:ArrayList<String>,
    private val buyAgainFoodPrice:ArrayList<String>,
    private val buyAgainFoodImage:ArrayList<Int>
):RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding=BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position])
    }


    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodNames: String, foodPrices: String, foodImages: Int) {
            binding.buyAgainfoodName.text=foodNames
            binding.buyAgainPrice.text=foodPrices
            binding.buyAgainImage.setImageResource(foodImages)
        }

    }
}