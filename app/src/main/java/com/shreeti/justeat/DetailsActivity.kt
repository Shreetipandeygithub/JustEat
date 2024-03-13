package com.shreeti.justeat

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.shreeti.justeat.Model.CartItemModel
import com.shreeti.justeat.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsBinding

    private var foodNames:String?=null
    private var foodImage:String?=null
    private var foodDescription:String?=null
    private var foodIngredient:String?=null
    private var foodPrice:String?=null
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth=FirebaseAuth.getInstance()

//        val foodName=intent.getStringExtra("MenuItemName")
//        val foodImage=intent.getIntExtra("MenuItemImage",0)

//        binding.detailFoodName.text=foodName
//        binding.detailFoodImage.setImageResource(foodImage)

        foodNames=intent.getStringExtra("MenuItemName")
        foodDescription=intent.getStringExtra("MenuItemDescription")
        foodPrice=intent.getStringExtra("MenuItemPrice")
        foodIngredient=intent.getStringExtra("MenuItemIngredient")
        foodImage=intent.getStringExtra("MenuItemImageUri")
        with(binding){
            detailFoodName.text=foodNames
            descriptionTextView.text=foodDescription
            ingredientTextView.text=foodIngredient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)

        }



        binding.imageView7.setOnClickListener{
            finish()
        }

        binding.buttonAddToCart.setOnClickListener{
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        //create a cart item object
        val cartItem = CartItemModel(
            foodNames.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodImage.toString(),
            foodIngredient.toString(),1
        )

        //save data to cart item to firebase

        database.child("user").child(userId).child("cartItems").push().setValue(cartItem)
            .addOnSuccessListener {0
                Toast.makeText(this, "Items added to Cart Successfully. ", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Items not added.", Toast.LENGTH_SHORT).show()
            }
    }
}