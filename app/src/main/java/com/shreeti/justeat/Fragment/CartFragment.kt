package com.shreeti.justeat.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.shreeti.justeat.Adapter.CartAdapter
import com.shreeti.justeat.Model.CartItemModel
import com.shreeti.justeat.PayOutActivity
import com.shreeti.justeat.R
import com.shreeti.justeat.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames:MutableList<String>
    private lateinit var foodPrices:MutableList<String>
    private lateinit var foodDescriptions:MutableList<String>
    private lateinit var foodImageUri:MutableList<String>
    private lateinit var foodIngredients:MutableList<String>
    private lateinit var quantity:MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCartBinding.inflate(inflater,container,false)

        auth=FirebaseAuth.getInstance()
        retrieveCartItem()



        binding.proceedbutton.setOnClickListener{

            //get order item details
            getOrderItemsDetail()

        }


        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdRef:DatabaseReference=database.reference.child("user").child(userId).child("cartItems")

        val foodName:MutableList<String> = mutableListOf<String>()
        val foodDescription:MutableList<String> = mutableListOf<String>()
        val foodIngredient:MutableList<String> = mutableListOf<String>()
        val foodPrice:MutableList<String> = mutableListOf<String>()
        val foodImage:MutableList<String> = mutableListOf<String>()

        //get item quantity
        val foodQuantities=cartAdapter.getUpdatedItemsQuantity()

        orderIdRef.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){

                    //get the cartItem to respective list
                    val orderItems=foodSnapshot.getValue(CartItemModel::class.java)
                    //add items details to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                }

                orderItemNow(foodName,foodDescription,foodIngredient,foodImage,foodPrice,foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Order making failed. Please Try Again",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun orderItemNow(
        foodName: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodImage: MutableList<String>,
        foodPrice: MutableList<String>,
        foodQuantities: MutableList<Int>
    ){
        if (isAdded && context!=null){
            val intent=Intent(requireContext(),PayOutActivity::class.java)
            intent.putExtra("foodItemName",foodName as ArrayList<String>)
            intent.putExtra("foodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("foodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("foodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("foodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("foodItemQuantity",foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItem() {
        database=FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        val foodRef:DatabaseReference=database.reference.child("user").child(userId).child("cartItems")

        //list to store cart items
        foodNames= mutableListOf()
        foodPrices= mutableListOf()
        foodIngredients= mutableListOf()
        foodDescriptions= mutableListOf()
        foodImageUri= mutableListOf()
        quantity= mutableListOf()
        //fetch data from database

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    //get the cartItem object from the child node
                    val cartItem=foodSnapshot.getValue(CartItemModel::class.java)

                    //add cart item to the list
                    cartItem?.foodName?.let { foodNames.add(it) }
                    cartItem?.foodPrice?.let { foodPrices.add(it) }
                    cartItem?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItem?.foodImage?.let { foodImageUri.add(it) }
                    cartItem?.foodQuantity?.let { quantity.add(it) }
                    cartItem?.foodIngredient?.let { foodIngredients.add(it) }

                }

                setAdapter()
            }

            private fun setAdapter() {

                cartAdapter=CartAdapter(requireContext(),foodNames,foodPrices,foodImageUri,foodDescriptions,foodIngredients,quantity)
                binding.recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.recyclerView.adapter=cartAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Data not fetch",Toast.LENGTH_SHORT).show()
            }

        })


    }

    companion object {

    }
}