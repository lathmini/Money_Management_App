package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.project1.R
import com.example.project1.models.IncomeModel
import com.google.firebase.database.FirebaseDatabase

class IncomeDetailsActivity : AppCompatActivity() {
    private lateinit var tvIncomeID: TextView
    private lateinit var tvIncomeTitle: TextView
    private lateinit var tvIncomeAmount: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("ICID").toString(),
                intent.getStringExtra("ICTitle").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("ICID").toString()
            )
        }


    }
    //Create delete function
    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Income").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Income data deleted", Toast.LENGTH_LONG).show()

//            val intent = Intent(this, FetchingActivity::class.java)
            finish()
//            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    //Create update function
    private fun openUpdateDialog(
        ICID: String,
        ICTitle: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etIncomeTitle = mDialogView.findViewById<EditText>(R.id.etIncomeTitle)
        val etAmount = mDialogView.findViewById<EditText>(R.id.etAmount)
        val etDescription = mDialogView.findViewById<EditText>(R.id.etDescription)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etIncomeTitle.setText(intent.getStringExtra("ICTitle").toString())
        etAmount.setText(intent.getStringExtra("ICAmount").toString())
        etDescription.setText(intent.getStringExtra("ICDescription").toString())

        mDialog.setTitle("Updating $ICTitle Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                ICID,
                etIncomeTitle.text.toString(),
                etAmount.text.toString(),
                etDescription.text.toString()
            )

            Toast.makeText(applicationContext, "Income Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvIncomeTitle.text = etIncomeTitle.text.toString()
            tvIncomeAmount.text = etAmount.text.toString()
            tvDescription.text = etDescription.text.toString()

            alertDialog.dismiss()

        }

    }
    private fun updateEmpData(
        ICID: String,
        ICTitle: String,
        ICAmount: String,
        ICDescription: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(ICID)
        val empInfo = IncomeModel(ICID, ICTitle, ICAmount, ICDescription)
        dbRef.setValue(empInfo)
    }

//---------------------------------------------

    private fun initView() {
        tvIncomeID = findViewById(R.id.tvIncomeID)
        tvIncomeTitle = findViewById(R.id.tvIncomeTitle)
        tvIncomeAmount = findViewById(R.id.tvIncomeAmount)
        tvDescription = findViewById(R.id.tvDescription)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    //get passed data from FetchingActivity and assign them
    private fun setValuesToViews() {
        tvIncomeID.text = intent.getStringExtra("ICID")
        tvIncomeTitle.text = intent.getStringExtra("ICTitle")
        tvIncomeAmount.text = intent.getStringExtra("ICAmount")
        tvDescription.text = intent.getStringExtra("ICDescription")
    }


}