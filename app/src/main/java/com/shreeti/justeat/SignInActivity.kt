package com.shreeti.justeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.R
import com.shreeti.justeat.Model.UserModel
import com.shreeti.justeat.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding:ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= Firebase.auth

        database= Firebase.database.reference

//        val googleSignInOption=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.)).build()

        binding.createAccountButton.setOnClickListener{
            userName=binding.personName.toString()
            email=binding.email.toString()
            password=binding.password.toString().trim()
            if (email.isBlank() || password.isBlank() || userName.isBlank()){
                Toast.makeText(this,"Please fill details", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
            }
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.alreadyHaveAccount.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
            //if user is already sign in
            if (task.isSuccessful){
                val user=auth.currentUser
                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Authentication Failure", task.exception)

            }
        }
    }
    override fun onStart() {
        super.onStart()
        val currUser=auth.currentUser
        if (currUser!=null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun saveUserData() {
        userName=binding.personName.toString()
        email=binding.email.text.toString()
        password=binding.password.text.toString().trim()

        val user=UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
        Toast.makeText(this,"Save data to database",Toast.LENGTH_SHORT).show()
    }
}