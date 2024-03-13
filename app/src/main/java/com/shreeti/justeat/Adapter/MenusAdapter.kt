package com.shreeti.justeat.Adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shreeti.justeat.DetailsActivity
import com.shreeti.justeat.Model.MenuItemModel
import com.shreeti.justeat.databinding.MenuItemsBinding


class MenusAdapter(
    private val menuItems: List<MenuItemModel>,
    private val requiredContext: Context
):RecyclerView.Adapter<MenusAdapter.MenuViewHolder>() {

    private val itemClickListener:OnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=MenuItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class MenuViewHolder(private val binding: MenuItemsBinding):RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION){
//                    itemClickListener?.onItemClick(position)
                    openDetailActivity(position)
                }

                //set on click listener to open details of the items
//                val intent=Intent(requiredContext,DetailsActivity::class.java)
//                intent.putExtra("MenuItemName",menuItem.get(position))
//                intent.putExtra("MenuItemImage",menuItem.get(position))
//                requiredContext.startActivity(intent)
            }
        }
        private fun openDetailActivity(position: Int) {
            val menuItem=menuItems[position]
            val intent= Intent(requiredContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName",menuItem.foodName)
                putExtra("MenuItemPrice",menuItem.foodPrice)
                putExtra("MenuItemDescription",menuItem.foodDescription)
                putExtra("MenuItemIngredient",menuItem.foodIngredient)
                putExtra("MenuItemImageUri",menuItem.foodImages)
            }
            requiredContext.startActivity(intent)

        }
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuFoodName.text = menuItem.foodName
                menuCost.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImages)
                Glide.with(requiredContext).load(uri).into(menuImage)
            }

        }
    }
    interface OnClickListener{
        fun onItemClick(position: Int)
    }
}