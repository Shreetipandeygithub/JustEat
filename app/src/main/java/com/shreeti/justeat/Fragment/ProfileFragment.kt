package com.shreeti.justeat.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shreeti.justeat.Model.UserModel
import com.shreeti.justeat.R
import com.shreeti.justeat.databinding.FragmentCartBinding
import com.shreeti.justeat.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment



        setUserData()

        binding.saveInformation.setOnClickListener {
            val names=binding.profilename.text.toString()
            val emails=binding.profileemail.text.toString()
            val address=binding.profileAddress.text.toString()
            val phoneNum=binding.profilephone.text.toString()

            updateUserData(names,emails,address,phoneNum)
        }

        return binding.root
    }

    private fun updateUserData(names: String, emails: String, address: String, phoneNum: String) {
        val userId=auth.currentUser?.uid
        if (userId!=null){
            val userReference=database.getReference("user").child(userId)
            val userData= hashMapOf("name" to names,
                "address" to address,
                "phone" to phoneNum,
                "email" to emails
            )
            userReference.setValue(userData)
                .addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile updated Successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Profiled Failed to update",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val usersId=auth.currentUser?.uid
        if (usersId!=null){
            val userReference=database.getReference("user").child(usersId)

            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if (userProfile!=null){

                            // binding the profile data with the UserModel data class
                            binding.profilename.setText(userProfile.name).toString()
                            binding.profileAddress.setText(userProfile.address)
                            binding.profileemail.setText(userProfile.email).toString()
                            binding.profilephone.setText(userProfile.phoneNumber)
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