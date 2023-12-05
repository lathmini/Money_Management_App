package com.example.project1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.R
import com.example.project1.models.IncomeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etIncomeTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etIncomeTitle = findViewById(R.id.etIncomeTitle)
        etAmount = findViewById(R.id.etAmount)
        etDescription = findViewById(R.id.etDescription)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Income") //give database name as "Employee"

        btnSaveData.setOnClickListener {
            saveEmployeeData()  //call the function when click the button
        }
    }

    private fun saveEmployeeData() { // create a function to save data
        //getting values
        val ICTitle = etIncomeTitle.text.toString()
        val ICAmount = etAmount.text.toString()
        val ICDescription = etDescription.text.toString()

        //frontend validation
        if (ICTitle.isEmpty()) {
            etIncomeTitle.error = "Please enter name"
        }
        if (ICAmount.isEmpty()) {
            etAmount.error = "Please enter age"
        }
        if (ICDescription.isEmpty()) {
            etDescription.error = "Please enter salary"
        }

        //get database ganerated id
        val empId = dbRef.push().key!!
        val employee = IncomeModel(empId, ICTitle, ICAmount, ICDescription) //call the EmployeeModel and add data to it

        //tell database to create child according to provided id and set Value according to schema model we created
        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                //To clear data after save
                etIncomeTitle.text.clear()
                etAmount.text.clear()
                etDescription.text.clear()
            }.addOnFailureListener{err -> //if insert failure display error message
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }

}