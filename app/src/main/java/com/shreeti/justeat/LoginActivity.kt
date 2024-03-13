package com.shreeti.justeat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.*
import com.google.firebase.ktx.Firebase
import com.shreeti.justeat.Model.UserModel
import com.shreeti.justeat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var userName:String?=null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= Firebase.auth

        database=Firebase.database.reference

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.app_name)).requestEmail().build()

        binding.googleButton.setOnClickListener {
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }


        binding.Loginbtn.setOnClickListener{

            //get data from text field
            email=binding.editTextTextEmailAddress.toString().trim()
            password=binding.editTextTextPassword.toString().trim()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill details", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
                Toast.makeText(this,"Login Successfully", Toast.LENGTH_SHORT).show()
            }

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.donthavebtn.setOnClickListener{
            val intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            //if user is already sign in
            if (task.isSuccessful) {
                val user = auth.currentUser
//                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Create User & Login Successfully", Toast.LENGTH_SHORT).show()
                        updateUI(user)
                        saveUserData()
                    } else {
                        Toast.makeText(this, "SignIn failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Authentication Failure", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email=binding.editTextTextEmailAddress.toString().trim()
        password=binding.editTextTextPassword.toString().trim()

        //user's email and password will automatically get saved in database
        val user=UserModel(userName,email,password)
        val userid=FirebaseAuth.getInstance().currentUser?.uid
        userid?.let {
            database.child("user").child(it).setValue(user)
        }
    }
    private val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode== Activity.RESULT_OK){
            val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account: GoogleSignInAccount =task.result
                val credential= GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask->
                    if (authTask.isSuccessful){

                        //successfully sign in with google
                        Toast.makeText(this,"Successfully sign in with Google",Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this,MainActivity::class.java))

                    }else{
                        Toast.makeText(this,"Google sign in Failed",Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                Toast.makeText(this,"Google sign in Failed",Toast.LENGTH_SHORT).show()
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

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}