package com.example.project1.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.databinding.FragmentFeedbackBinding

class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var experienceSelected = "Good"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val feedbackViewModel =
            ViewModelProvider(this)[FeedbackViewModel::class.java]

        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val name = binding.editTextText3
        val email = binding.editTextText4
        val describe = binding.editTextTextMultiLine
        val experience = binding.radioGroup
        experience.check(binding.radioButton.id)
        experience.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId== binding.radioButton.id){
                experienceSelected = "Good"
            }else if(checkedId== binding.radioButton2.id){
                experienceSelected = "Excellent"
            }else if(checkedId== binding.radioButton3.id){
                experienceSelected = "Bad"
            }else{
                experienceSelected = "Fantastic"
            }

        }

        val btnFeedback = binding.button2
        btnFeedback.setOnClickListener {
            if(name.text.isBlank()||email.text.isBlank()||describe.text.isBlank()){
                Toast.makeText(
                    context,
                    "Please fill all fields...",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }
            feedbackViewModel.giveFeedback(name.text.toString(),email.text.toString(),describe.text.toString(),experienceSelected)
        }

        val progressBar = binding.progressBar3
        feedbackViewModel.isLoading.observe(viewLifecycleOwner) {
            if(it){
                progressBar.visibility = ProgressBar.VISIBLE
                btnFeedback.isEnabled = false
            }else{
                progressBar.visibility = ProgressBar.INVISIBLE
                btnFeedback.isEnabled = true
                name.setText("")
                email.setText("")
                describe.setText("")
                experience.check(binding.radioButton.id)
            }
        }


//    val textView: TextView = binding.textGallery
//      feedbackViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
//    }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}