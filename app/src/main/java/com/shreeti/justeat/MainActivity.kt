package com.shreeti.justeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shreeti.justeat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //controlling all the navigations on the bottom of the main page
        var NavController=findNavController(R.id.fragmentContainerView)
        var bottomNav=findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setupWithNavController(NavController)

        //binding notification bells with the notification fragment
        binding.notificationBell.setOnClickListener{
            val bottomSheetDialog=NotificationBottomFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }
}