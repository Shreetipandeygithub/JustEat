package com.shreeti.justeat.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shreeti.justeat.DetailsActivity
import com.shreeti.justeat.databinding.PopularItemListBinding

class PopularAdapter(
    private val items:List<String>,
    private val prices:List<String>,
    private val images:List<Int>,
    private val requiredContext:Context
): RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
       val item=items[position]
        val image=images[position]
        val price=prices[position]
        holder.bind(item,price,image)

        holder.itemView.setOnClickListener{

            //set on click listener to open details of the items
            val intent=Intent(requiredContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("MenuItemImage",image)
            requiredContext.startActivity(intent)
        }
    }

    class PopularViewHolder(private  val binding:PopularItemListBinding): RecyclerView.ViewHolder(binding.root){
        private val imagesView=binding.imageView
        fun bind(Item: String,Price: String, Images: Int) {
            binding.foodItems.text=Item
            binding.popularCost.text=Price
            imagesView.setImageResource(Images)
        }

    }
}