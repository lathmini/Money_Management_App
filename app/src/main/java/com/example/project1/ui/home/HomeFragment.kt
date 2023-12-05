package com.example.project1.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.project1.HomeActivity
import com.example.project1.HomeViewModel
import com.example.project1.R
import com.example.project1.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root




        val navView: BottomNavigationView = binding.bottomInclude.navViewBottom
        val navHostFragment = childFragmentManager.findFragmentById(R.id.container_x) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_savings, R.id.navigation_up, R.id.navigation_down, R.id.navigation_home
            )
        )
        navView.setupWithNavController(navController)



//        arguments?.getString("name").let {
//            if(it == "income"){
//                Toast.makeText(context,"icome navigate", Toast.LENGTH_LONG).show()
//                navController.navigate(R.id.navigation_up)
//            }
//        }
//
//
//        homeViewModel.navController.observe(viewLifecycleOwner) {
//            if(it == null){
//                Toast.makeText(context,"nggggggggggggg", Toast.LENGTH_LONG).show()
//            }else{
//                Toast.makeText(context,"navvvvvvvvvvv", Toast.LENGTH_LONG).show()
//            }
//        }



//        homeViewModel.navController?.let {
//                    it.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.nav_savings) {
//                Toast.makeText(context,"nav_savings", Toast.LENGTH_LONG).show()
//            } else if (destination.id == R.id.nav_expenses) {
//                Toast.makeText(context,"nav_expenses", Toast.LENGTH_LONG).show()
//            }else {
//                Toast.makeText(context,"else", Toast.LENGTH_LONG).show()
//            }
//        }
//        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}