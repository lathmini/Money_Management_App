package com.example.project1.ui.home.savings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project1.R
import com.example.project1.databinding.ActivitySavingAddBinding
import com.example.project1.databinding.FragmentBottomHomeBinding
import com.example.project1.ui.home.bottomhome.BottomHomeViewModel

class SavingAddActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener{

    private lateinit var binding: ActivitySavingAddBinding

    var categories = arrayOf("Food", "Education", "Transport","Medicine")
    private var selectedCategory = "Food"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavingAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val savingsAddViewModel = ViewModelProvider(this)[SavingsAddViewModel::class.java]

        val spin : Spinner = binding.spinner
        val aa = ArrayAdapter(baseContext, android.R.layout.simple_spinner_item, categories)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = aa
        spin.onItemSelectedListener =  this@SavingAddActivity


        val title = binding.editTextText
        val amount = binding.editTextNumberDecimal
        val date = binding.editTextDate
        val progressBar = binding.progressBar

        val btnAdd = binding.button
        btnAdd.setOnClickListener {
            if(title.text.isBlank()||amount.text.isBlank()||date.text.isBlank()){
                Toast.makeText(
                    baseContext,
                    "Please fill all fields...",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }
            savingsAddViewModel.addSavingToDatabase(title.text.toString(), amount.text.toString().toDouble(),  date.text.toString() ,selectedCategory)
        }


        savingsAddViewModel.isLoading.observe(this) {
            if(it){
                progressBar.visibility = ProgressBar.VISIBLE
                btnAdd.isEnabled = false
            }else{
                progressBar.visibility = ProgressBar.INVISIBLE
                btnAdd.isEnabled = true
                title.setText("")
                amount.setText("")
                date.setText("")
            }
        }



    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategory = categories[position]
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}