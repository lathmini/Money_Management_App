package com.example.project1.ui.home.income

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.premoney.adapters.IncomeAdapter
import com.example.project1.activities.IncomeDetailsActivity
import com.example.project1.activities.InsertionActivity
import com.example.project1.databinding.ActivityFetchingBinding
import com.example.project1.models.IncomeModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener

class IncomeFragment : Fragment() {

    private var _binding: ActivityFetchingBinding? = null

    private val binding get() = _binding!!

    private lateinit var btnInsertData: FloatingActionButton

    private lateinit var IncomeRecyclerView: RecyclerView
    //private lateinit var tvLoadingData: TextView
    private lateinit var incomeList: ArrayList<IncomeModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityFetchingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnInsertData = binding.floatingActionButton3
        btnInsertData.setOnClickListener {
            val intent = Intent(context, InsertionActivity::class.java)
            startActivity(intent)
        }

        IncomeRecyclerView = binding.rvIncome
        IncomeRecyclerView.layoutManager = LinearLayoutManager(context)
        IncomeRecyclerView.setHasFixedSize(true)
        //tvLoadingData = findViewById(R.id.tvLoadingData)

        incomeList = arrayListOf()

        getIncomeData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getIncomeData() {
//        empRecyclerView.visibility = View.GONE
//        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Income")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                incomeList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(IncomeModel::class.java)
                        incomeList.add(empData!!)
                    }
                    val mAdapter = IncomeAdapter(incomeList)
                    IncomeRecyclerView.adapter = mAdapter

                    //Implementing method for edit card
                    mAdapter.setOnItemClickListener(object : IncomeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(context, IncomeDetailsActivity::class.java)

                            //put extras -> send data
                            intent.putExtra("ICID", incomeList[position].ICID)
                            intent.putExtra("ICTitle", incomeList[position].ICTitle)
                            intent.putExtra("ICAmount", incomeList[position].ICAmount)
                            intent.putExtra("ICDescription", incomeList[position].ICDescription)
                            startActivity(intent)
                        }

                    })
//                    empRecyclerView.visibility = View.VISIABLE
//                    tvLoadingData.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}