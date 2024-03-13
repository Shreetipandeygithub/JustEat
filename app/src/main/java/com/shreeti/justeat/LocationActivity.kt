package com.shreeti.justeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.shreeti.justeat.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private val binding:ActivityLocationBinding by lazy {
        ActivityLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val listOfLocation= arrayOf("Jaipur","Mumbai","Delhi")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,listOfLocation)
        val autoCompleteTextView=binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}