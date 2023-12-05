package com.example.project1.ui.home.bottomhome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.databinding.FragmentBottomHomeBinding
import com.example.project1.models.Saving
import com.example.project1.ui.home.savings.SavingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class BottomHomeFragment : Fragment() {

    private var _binding: FragmentBottomHomeBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = Firebase.auth

    private val db: DatabaseReference = Firebase.database.reference

    private lateinit var mRefIncome: DatabaseReference
    private lateinit var mListenerIncome: ValueEventListener

    private lateinit var mRefExpense :DatabaseReference
    private lateinit var mListenerExpense: ValueEventListener

    private lateinit var mRefSaving: DatabaseReference
    private lateinit var mListenerSaving: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomHomeViewModel =
            ViewModelProvider(this)[BottomHomeViewModel::class.java]

        _binding = FragmentBottomHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val income = binding.income
        val expense = binding.expense
        val saving = binding.saving

        var countIncome = 0.0
        var countExpense = 0.0
        var countSaving = 0.0

        val user = auth.currentUser ?: return root

        mRefSaving = db.child("savings").child(user.uid)
        mListenerSaving = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (shot in dataSnapshot.children) {
                    val save: Saving? = shot.getValue<Saving>()
                    if (save != null) {
                        if (save.amount != null) {
                            countSaving += save.amount!!
                        }
                    }
                }

                saving.text = countSaving.toString()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mRefSaving.addValueEventListener(mListenerSaving)



//        mRefIncome = db.child("Income")
//        mListenerIncome = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for (shot in dataSnapshot.children) {
//                    val amount = shot.child("ICAmount").getValue<Double>()
//                    if (amount != null) {
//                        countIncome += amount
//                    }
//
//                }
//
//                income.text = countIncome.toString()
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        }
//        mRefIncome.addValueEventListener(mListenerIncome)
//
//
//
//        mRefExpense = db.child("Expences")
//        mListenerExpense = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for (shot in dataSnapshot.children) {
//                    val amount = shot.child("EXAmount").getValue<Double>()
//                    if (amount != null) {
//                        countExpense += amount
//                    }
//
//                }
//
//                expense.text = countIncome.toString()
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        }
//        mRefExpense.addValueEventListener(mListenerExpense)





//        bottomHomeViewModel.isLoading.observe(viewLifecycleOwner) {
//            if(it){
//                progressBar.visibility = ProgressBar.VISIBLE
//                btnAdd.isEnabled = false
//            }else{
//                progressBar.visibility = ProgressBar.INVISIBLE
//                btnAdd.isEnabled = true
//                title.setText("")
//                amount.setText("")
//                date.setText("")
//            }
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        //mRefIncome.removeEventListener(mListenerIncome)
        //mRefExpense.removeEventListener(mListenerExpense)
       // mRefSaving.removeEventListener(mListenerSaving)
    }

}