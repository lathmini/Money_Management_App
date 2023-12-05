package com.example.project1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.models.ExpensesModel

class ExpAdapter(private val ExpensesList: ArrayList<ExpensesModel>) :
    RecyclerView.Adapter<ExpAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }
    //------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = ExpensesList[position]
        holder.tvexpTitle.text = currentEmp.EXTitle
        holder.tvexpAmount.text = currentEmp.EXAmount
    }

    override fun getItemCount(): Int {
        return ExpensesList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvexpTitle : TextView = itemView.findViewById(R.id.tvexpTitle)
        val tvexpAmount : TextView = itemView.findViewById(R.id.tvexpAmount)
        //for click inside the card
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}