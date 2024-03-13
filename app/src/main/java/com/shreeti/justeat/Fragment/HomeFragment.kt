package com.shreeti.justeat.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shreeti.justeat.Adapter.MenusAdapter
import com.shreeti.justeat.Adapter.PopularAdapter
import com.shreeti.justeat.MenuBottomSheetFragment
import com.shreeti.justeat.Model.MenuItemModel
import com.shreeti.justeat.R
import com.shreeti.justeat.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItemModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)


        binding.viewMenu.setOnClickListener{
            val bottomSheetDialog=MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }

        //retrieve menu items and display
        retrieveAndDiplayData()
        return binding.root
    }

    private fun retrieveAndDiplayData() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")
        menuItems= mutableListOf()

        //retrieve menu item from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(MenuItemModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //display random popular item
                randomPopularItem()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopularItem() {
        val index=menuItems.indices.toList().shuffled()
        val numItemToShow=6
        val subsetMenuItem=index.take(numItemToShow).map { menuItems[it] }

        //set popular item to recycler view
        setPopularItemAdapter(subsetMenuItem)
    }

    private fun setPopularItemAdapter(subsetMenuItem: List<MenuItemModel>) {

        val adapter=MenusAdapter(subsetMenuItem,requireContext())
        binding.popularRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter=adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList=ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3,ScaleTypes.FIT))

        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object:ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })
//        val foodName= listOf("Pasta","Kabaab Paratha","Fruit Custurd","Ice Cream","Avocado Salad","Food1","Food2","Food3","Food4","Food5","Food6","Food7")
//        val foodPrice= listOf("₹250","₹120","₹180","₹100","₹200","₹100","₹160","₹80","₹90","₹110","₹180","₹150")
//        val foodImages= listOf(R.drawable.menu5,R.drawable.menu6,R.drawable.menu7,R.drawable.menu3,R.drawable.menu2,R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5,R.drawable.menu6,R.drawable.menu7)
//        val adapter=PopularAdapter(foodName,foodPrice,foodImages,requireContext())
//        binding.popularRecyclerView.layoutManager=LinearLayoutManager(requireContext())
//        binding.popularRecyclerView.adapter=adapter
    }

    companion object{

    }

}