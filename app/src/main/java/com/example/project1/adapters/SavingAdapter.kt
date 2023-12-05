package com.example.project1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.models.Saving
import com.google.android.material.textfield.TextInputEditText


class SavingAdapter(var itemList: List<Saving>, onItemClickListener: MyOnItemClickListener) :
    RecyclerView.Adapter<SavingAdapter.SavingsViewHolder>() {

    private var mListener: MyOnItemClickListener? = null
   init {
       mListener = onItemClickListener;
   }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavingAdapter.SavingsViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.saving_item, parent, false)
        return SavingsViewHolder(v)
    }

    override fun onBindViewHolder(holder: SavingAdapter.SavingsViewHolder, position: Int) {
        val saving = itemList[position]
        holder.id.text = saving.id
        holder.title.setText(saving.title)
        holder.amount.setText(saving.amount.toString())
        holder.date.setText(saving.date)
        holder.category.setText(saving.category)


        holder.update.setOnClickListener {
            mListener?.onUpdateClick(position,it, Saving(saving.id,holder.title.text.toString(),holder.amount.text.toString().toDouble(),holder.date.text.toString(),holder.category.text.toString()))
        }

        holder.delete.setOnClickListener {
            mListener?.onDeleteClick(position,it, Saving(saving.id,holder.title.text.toString(),holder.amount.text.toString().toDouble(),holder.date.text.toString(),holder.category.text.toString()))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    inner class SavingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var id: TextView
        var title: TextInputEditText
        var amount: TextInputEditText
        var date: TextInputEditText
        var category: TextInputEditText

        var update: ImageButton
        var delete: ImageButton


        init {


            id = itemView.findViewById(R.id.textView9)
            title = itemView.findViewById(R.id.chip)
            amount = itemView.findViewById(R.id.chip2)
            date = itemView.findViewById(R.id.chip4)
            category = itemView.findViewById(R.id.chip3)

            update = itemView.findViewById(R.id.imageButton)
            delete = itemView.findViewById(R.id.imageButton2)



        }


    }

    interface MyOnItemClickListener {
        fun onUpdateClick(position: Int, v: View?,saving:Saving)
        fun onDeleteClick(position: Int, v: View?,saving:Saving)
    }
}