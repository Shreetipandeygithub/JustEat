package com.shreeti.justeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shreeti.justeat.Adapter.NotificationAdapter
import com.shreeti.justeat.databinding.FragmentNotificationBottomBinding


class NotificationBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notifications= listOf("Your order has been Cancelled successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val images= listOf(R.drawable.baseline_cancel_24,R.drawable.baseline_directions_car_filled_24,R.drawable.baseline_check_circle_24)
        val adapter=NotificationAdapter(ArrayList(notifications), ArrayList(images))

        binding.notificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}