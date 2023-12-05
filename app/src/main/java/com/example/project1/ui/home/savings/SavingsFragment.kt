package com.example.project1.ui.home.savings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project1.HomeActivity
import com.example.project1.databinding.FragmentSavingsBinding

class SavingsFragment : Fragment() {

private var _binding: FragmentSavingsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val savingsViewModel =
        ViewModelProvider(this)[SavingsViewModel::class.java]

    _binding = FragmentSavingsBinding.inflate(inflater, container, false)
    val root: View = binding.root

//    slideshowViewModel.adapter.observe(viewLifecycleOwner) {
//      textView.text = it
//    }

      val recyclerView = binding.recycler
      val progressBar = binding.progressBar2
      val btnFloating = binding.floatingActionButton
      recyclerView.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
      recyclerView.setHasFixedSize(true)
      recyclerView.adapter = savingsViewModel.adapter


      savingsViewModel.isLoading.observe(viewLifecycleOwner) {
          if(it){
              progressBar.visibility = ProgressBar.VISIBLE
          }else{
              progressBar.visibility = ProgressBar.INVISIBLE
          }
      }

      btnFloating.setOnClickListener {
          val intent = Intent(context, SavingAddActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
          startActivity(intent)
      }

    return root
  }

override fun onDestroyView() {
    super.onDestroyView()
        _binding = null
    }
}