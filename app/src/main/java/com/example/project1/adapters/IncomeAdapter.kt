package com.example.premoney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.models.IncomeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class IncomeAdapter(private val incomeList: ArrayList<IncomeModel>) :
    RecyclerView.Adapter<IncomeAdapter.ViewHolder>() {


//    val incomeList: ArrayList<IncomeModel> = arrayListOf<IncomeModel>()

//    dbRef.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            incomeList.clear()
//            if (snapshot.exists()) {
//                for (transactionSnap in snapshot.children) {
//                    val transactionData =
//                        transactionSnap.getValue(TransactionModel::class.java) //reference data class
//                    transactionList.add(transactionData!!)
//                }
//            }
//            //separate expanse amount and income amount, and show it based on the range date :
//            for ((i) in transactionList.withIndex()){
//                if (transactionList[i].type == 1 &&
//                    transactionList[i].date!! > dateStart-86400000 && //minus by 1 day
//                    transactionList[i].date!! <= dateEnd){
//                    amountExpenseTemp += transactionList[i].amount!!
//                }else if (transactionList[i].type == 2 &&
//                    transactionList[i].date!! > dateStart-86400000 &&
//                    transactionList[i].date!! <= dateEnd){
//                    amountIncomeTemp += transactionList[i].amount!!
//                }
//            }






    //Create click -> edit mode
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    //------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.income_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = incomeList[position]
        holder.tvIncomeTitle.text = currentData.ICTitle
        holder.tvIncomeAmount.text = currentData.ICAmount
    }

    override fun getItemCount(): Int {
        return incomeList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvIncomeTitle : TextView = itemView.findViewById(R.id.tvIncomeTitle)
        val tvIncomeAmount : TextView = itemView.findViewById(R.id.tvIncomeAmount)

        //for click inside the card
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}