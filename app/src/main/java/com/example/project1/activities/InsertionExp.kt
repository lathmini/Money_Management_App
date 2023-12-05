package com.example.project1.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.R
import com.example.project1.models.ExpensesModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionExp : AppCompatActivity() {

    private lateinit var expTitle: EditText
    private lateinit var expAmount: EditText
    private lateinit var expDescription: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addexpemses)
        // Hide the action bar and set the layout to fullscreen
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        supportActionBar?.hide()

        expTitle = findViewById(R.id.expTitle)
        expAmount = findViewById(R.id.expAmount)
        expDescription = findViewById(R.id.expDescription)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Expences") //give database name as "Employee"

        btnSaveData.setOnClickListener {
            saveExpData()  //call the function when click the button
        }
    }

    private fun saveExpData() { // create a function to save data
        //getting values
        val EXTitle = expTitle.text.toString()
        val EXAmount = expAmount.text.toString()
        val EXDescription = expDescription.text.toString()

        //frontend validation
        if (EXTitle.isEmpty()) {
            expTitle.error = "Please enter Title"
        }
        if (EXAmount.isEmpty()) {
            expAmount.error = "Please enter Amount"
        }
        if (EXDescription.isEmpty()) {
            expDescription.error = "Please enter Discription"
        }

        //get database ganerated id
        val empId = dbRef.push().key!!
        val exp = ExpensesModel(empId, EXTitle, EXAmount, EXDescription) //call the EmployeeModel and add data to it

        //tell database to create child according to provided id and set Value according to schema model we created
        dbRef.child(empId).setValue(exp)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                //To clear data after save
                expTitle.text.clear()
                expAmount.text.clear()
                expDescription.text.clear()
            }.addOnFailureListener{err -> //if insert failure display error message
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }

}