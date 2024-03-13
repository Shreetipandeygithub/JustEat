package com.shreeti.justeat.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shreeti.justeat.Model.CartItemModel
import com.shreeti.justeat.databinding.AddToCartRecyclerviewBinding

class CartAdapter(
    private val context: Context,
    private val CartItem:MutableList<String>,
    private val CartItemPrice:MutableList<String>,
    private var CartImage:MutableList<String>,
    private val CartIngredient:MutableList<String>,
    private var CartDescription:MutableList<String>,
    private val CartQuantity:MutableList<Int>
):RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    //initialize firebase auth
    private val auth=FirebaseAuth.getInstance()

    init {
        val database=FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber=CartItem.size

        itemQuantities= IntArray(cartItemNumber){1}
        cartItemReference=database.reference.child("user").child(userId).child("cartItems")
    }
    companion object{
        private var  itemQuantities:IntArray= intArrayOf()
        private lateinit var cartItemReference:DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=AddToCartRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int =CartItem.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    //get updated quantity
    fun getUpdatedItemsQuantity(): MutableList<Int> {
        val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(CartQuantity)
        return itemQuantity
    }

    inner class CartViewHolder(private val binding: AddToCartRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
//                cartFoodName.text=CartItem[position]
//                cartTotalCost.text=CartItemPrice[position]
//                imageView.setImageResource(CartImage[position])
//                quantity.text=itemQuantities[position].toString()

                val quantity= itemQuantities[position]
                cartFoodName.text=CartItem[position]
                cartQuantity.text=CartItemPrice[position]
                //load image using glide
                val uriString=CartImage[position]
                Log.d("image","food Url: $uriString")

                val uri=Uri.parse(uriString)
                Glide.with(context).load(uri).listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading Failed")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading Success")
                        return false

                    }
                }).into(cartImage)

                minusBtn.setOnClickListener{
                    decreaseQuantity(position)
                }
                addBtn.setOnClickListener{
                    increaseQuantity(position)
                }
                deleteBtn.setOnClickListener{
                    val itemPosition=adapterPosition
                    if(itemPosition!=RecyclerView.NO_POSITION){
                        deleteItem(itemPosition)
                    }
                }
            }
        }
        private fun decreaseQuantity(position: Int){
            if(itemQuantities[position]>1){
                itemQuantities[position]--
                binding.cartQuantity.text=itemQuantities[position].toString()
            }
        }
        private fun increaseQuantity(position: Int){
            if(itemQuantities[position]<10){
                itemQuantities[position]++
                binding.cartQuantity.text=itemQuantities[position].toString()
            }
        }
        private fun deleteItem(position: Int){
//            CartItem.removeAt(position)
//            CartImage.removeAt(position)
//            CartItemPrice.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position,CartItem.size)

            //retrieve position
            val positionRetrieve=position
            getUniquePositionRetrieve(positionRetrieve){uniqueKey->
                if (uniqueKey!=null){
                    removeItem(position,uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey!=null){
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    CartItem.removeAt(position)
                    CartImage.removeAt(position)
                    CartDescription.removeAt(position)
                    CartQuantity.removeAt(position)
                    CartItemPrice.removeAt(position)
                    CartIngredient.removeAt(position)
                    Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show()


                    //update itemQuantities
                    itemQuantities= itemQuantities.filterIndexed { index, i -> index!=position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,CartItem.size)
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniquePositionRetrieve(positionRetrieve: Int,onComplete:(String?)->Unit) {
            cartItemReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey:String?=null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index==positionRetrieve){
                            uniqueKey=dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }
}

