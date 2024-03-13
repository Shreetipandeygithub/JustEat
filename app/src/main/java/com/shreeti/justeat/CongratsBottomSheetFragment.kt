package com.shreeti.justeat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shreeti.justeat.databinding.FragmentCongratsBottomSheetBinding


class CongratsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCongratsBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCongratsBottomSheetBinding.inflate(layoutInflater,container,false)

        binding.goHome.setOnClickListener{
            val intent=Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {

    }
}