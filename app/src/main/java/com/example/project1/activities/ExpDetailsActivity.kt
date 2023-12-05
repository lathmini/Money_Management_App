package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.project1.R
import com.example.project1.models.ExpensesModel
import com.google.firebase.database.FirebaseDatabase

class ExpDetailsActivity : AppCompatActivity() {
    private lateinit var tvexpID: TextView
    private lateinit var tvexpTitle: TextView
    private lateinit var tvexpAmount: TextView
    private lateinit var tvexpDescription: TextView
    private lateinit var btnexpUpdate: Button
    private lateinit var btnexpDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp_details)
        // Hide the action bar and set the layout to fullscreen
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        supportActionBar?.hide()

        initView()
        setValuesToViews()

        btnexpUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("EXID").toString(),
                intent.getStringExtra("EXTitle").toString()
            )
        }

        btnexpDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("EXID").toString()
            )
        }


    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Expences").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Expences data deleted", Toast.LENGTH_LONG).show()

//            val intent = Intent(this, FetchingActivity::class.java)
            finish()
//            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        ExID: String,
        ExTitle: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_expdialog, null)

        mDialog.setView(mDialogView)

        val expTitle = mDialogView.findViewById<EditText>(R.id.expTitle)
        val expAmount = mDialogView.findViewById<EditText>(R.id.expAmount)
        val expDescription = mDialogView.findViewById<EditText>(R.id.expDescription)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnexpUpdateData)

        expTitle.setText(intent.getStringExtra("EXTitle").toString())
        expAmount.setText(intent.getStringExtra("EXAmount").toString())
        expDescription.setText(intent.getStringExtra("EXDescription").toString())

        mDialog.setTitle("Updating $ExTitle Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                ExID,
                expTitle.text.toString(),
                expAmount.text.toString(),
                expDescription.text.toString()
            )

            Toast.makeText(applicationContext, "Expenses Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvexpTitle.text = expTitle.text.toString()
            tvexpAmount.text = expAmount.text.toString()
            tvexpDescription.text = expDescription.text.toString()

            alertDialog.dismiss()

        }

    }


    private fun updateEmpData(
        EXID: String,
        EXTitle: String,
        EXAmount: String,
        EXDescription: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Expences").child(EXID)
        val empInfo = ExpensesModel(EXID, EXTitle, EXAmount, EXDescription)
        dbRef.setValue(empInfo)
    }


    private fun initView() {
        tvexpID = findViewById(R.id.tvexpID)
        tvexpTitle = findViewById(R.id.tvexpTitle)
        tvexpAmount = findViewById(R.id.tvexpAmount)
        tvexpDescription = findViewById(R.id.tvexpDescription)

        btnexpUpdate = findViewById(R.id.btnexpUpdate)
        btnexpDelete = findViewById(R.id.btnexpDelete)
    }

    //get passed data from FetchingActivity and assign them
    private fun setValuesToViews() {
        tvexpID.text = intent.getStringExtra("EXID")
        tvexpTitle.text = intent.getStringExtra("EXTitle")
        tvexpAmount.text = intent.getStringExtra("EXAmount")
        tvexpDescription.text = intent.getStringExtra("EXDescription")
    }

}