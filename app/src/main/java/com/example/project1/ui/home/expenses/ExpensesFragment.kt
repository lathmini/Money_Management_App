package com.example.project1.ui.home.expenses

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.activities.ExpDetailsActivity
import com.example.project1.activities.InsertionExp
import com.example.project1.adapters.ExpAdapter
import com.example.project1.databinding.FragmentExpensesBinding
import com.example.project1.models.ExpensesModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var btnExpInsertData: FloatingActionButton
    private lateinit var ExpRecyclerView: RecyclerView
    private lateinit var ExpencesList: ArrayList<ExpensesModel>
    private lateinit var dbRef: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnExpInsertData = binding.floatingActionButton2

        btnExpInsertData.setOnClickListener {
            val intent = Intent(context, InsertionExp::class.java)
            startActivity(intent)
        }
//----------------------------------------------------

        ExpRecyclerView = binding.rvIncome
        ExpRecyclerView.layoutManager = LinearLayoutManager(context)
        ExpRecyclerView.setHasFixedSize(true)

        ExpencesList = arrayListOf()

        getExpensesData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getExpensesData() {
//        empRecyclerView.visibility = View.GONE
//        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Expences")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                ExpencesList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(ExpensesModel::class.java)
                        ExpencesList.add(empData!!)
                    }

                    val mAdapter = ExpAdapter(ExpencesList)
                    ExpRecyclerView.adapter = mAdapter

                    //Implementing method for edit card
                    mAdapter.setOnItemClickListener(object : ExpAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(context, ExpDetailsActivity::class.java)

                            //put extras -> send data
                            intent.putExtra("EXID", ExpencesList[position].EXID)
                            intent.putExtra("EXTitle", ExpencesList[position].EXTitle)
                            intent.putExtra("EXAmount", ExpencesList[position].EXAmount)
                            intent.putExtra("EXDescription", ExpencesList[position].EXDescription)
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