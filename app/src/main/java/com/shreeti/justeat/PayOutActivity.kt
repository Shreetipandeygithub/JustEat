package com.shreeti.justeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.shreeti.justeat.Fragment.CartFragment
import com.shreeti.justeat.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {
    private lateinit var name:String
    private lateinit var add:String
    private lateinit var phone:String
    private lateinit var totalAmt:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var foodItemIngredient:ArrayList<String>
    private lateinit var foodItemQuantity:ArrayList<Int>
    private lateinit var auth: FirebaseAuth
    private lateinit var userId:String
    private lateinit var databaseReference: DatabaseReference


    private lateinit var binding:ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize auth and firebase
        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference

        //set user data
        setUserData()


        binding.placeOrder.setOnClickListener{
            val bottomSheetDialog=CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
        binding.backbutton.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setUserData() {
        val user=auth.currentUser
        if (user!=null) {
            val userId=user.uid
            val userRef=databaseReference.child("user").child(userId)
            userRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val name=snapshot.child("name").getValue(String::class.java)?:""
                        val address=snapshot.child("address").getValue(String::class.java)?:""
                        val phoneNum=snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            adminName.setText(name)
                            adminAddress.setText(address)
                            phoneNumber.setText(phoneNum)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}