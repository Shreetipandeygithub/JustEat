package com.shreeti.justeat.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreeti.justeat.Adapter.MenusAdapter
import com.shreeti.justeat.R
import com.shreeti.justeat.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val originalMenuFoodName= listOf("Pasta","Kabaab Paratha","Fruit Custurd","Ice Cream","Avocado Salad","Food1","Food2","Food3","Food4","Food5","Food6","Food7")
    private val originalMenuItemPrices= listOf("₹250","₹120","₹180","₹100","₹200","₹100","₹160","₹80","₹90","₹110","₹180","₹150")
    private val originalMenuImage= listOf(R.drawable.menu5,R.drawable.menu6,R.drawable.menu7,R.drawable.menu3,R.drawable.menu2,R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5,R.drawable.menu6,R.drawable.menu7)
    private lateinit var adapter:MenusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val filteredMenuFoodName= mutableListOf<String>()
    private val filteredMenuItemPrice= mutableListOf<String>()
    private val filteredMenuImage= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSearchBinding.inflate(layoutInflater,container,false)

//        adapter=MenusAdapter(filteredMenuFoodName,filteredMenuItemPrice,filteredMenuImage,requireContext())
//        binding.menuRecyclerView.layoutManager=LinearLayoutManager(requireContext())
//        binding.menuRecyclerView.adapter=adapter


        //setup for search view
        setupSearchView()


        //show all menu items
        showAllMenu()
        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrices)
        filteredMenuImage.addAll(originalMenuImage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView(){
        binding.searchView1.setOnQueryTextListener( object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterMenuItems(p0)
                return true
            }
        } )
    }

    private fun filterMenuItems(query: String?) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()



        originalMenuFoodName.forEachIndexed{ index, foodName->
            if(foodName.contains(query.toString(),ignoreCase = true)){
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(originalMenuItemPrices[index])
                filteredMenuImage.add(originalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}