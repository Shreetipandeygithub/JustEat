package com.shreeti.justeat.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreeti.justeat.Adapter.BuyAgainAdapter
import com.shreeti.justeat.R
import com.shreeti.justeat.databinding.BuyAgainItemBinding
import com.shreeti.justeat.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

private lateinit var binding: FragmentHistoryBinding
private lateinit var buyAgainAdapter:BuyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHistoryBinding.inflate(layoutInflater,container,false)

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView(){
        val buyAgainFoodName= arrayListOf("Food1","Food2","Food3","Food4","Food5","Food6","Food7")
        val buyAgainFoodPrice= arrayListOf("₹100","₹160","₹80","₹90","₹110","₹180","₹150")
        val buyAgainFoodImage= arrayListOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5,R.drawable.menu6,R.drawable.menu7)

        buyAgainAdapter= BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage)
        binding.recyclerViewHistory.adapter=buyAgainAdapter
        binding.recyclerViewHistory.layoutManager=LinearLayoutManager(requireContext())
    }
    companion object {

    }
}