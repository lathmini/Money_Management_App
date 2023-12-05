package com.example.project1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UsesProActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "UsesProActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var btnDelete: Button
    private lateinit var btnChangePassword: Button

    private lateinit var pathRef:DatabaseReference
    private lateinit var mListener:ValueEventListener

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI and redirect accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } else if (!currentUser.isEmailVerified) {
            val intent = Intent(this,VerificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uses_pro)


        // Initialize Firebase Auth
        auth = Firebase.auth

        val btnUpdate = findViewById<Button>(R.id.updateButton)
        btnDelete = findViewById(R.id.deleteButton)
        btnChangePassword = findViewById(R.id.changePassButton)

        val vProName = findViewById<EditText>(R.id.profile_username)
        val vProEmail = findViewById<EditText>(R.id.profile_email)
        val vProContact = findViewById<EditText>(R.id.profile_contact)

        val currentUser = auth.currentUser
        val db = Firebase.database.reference
        if(currentUser!= null && currentUser.isEmailVerified){
            vProEmail.setText(currentUser.email)

            pathRef = db.child("users").child(currentUser.uid)
            mListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val name: String = snapshot.child("name").value as String
                                val contact: String = snapshot.child("contact").value as String
                                val email: String = snapshot.child("email").value as String
                                vProContact.setText(contact)
                                vProName.setText(name)
                                vProEmail.setText(email)
                            }else{
                                Log.d(TAG, "No such data")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(TAG, "get failed with ", error.toException())
                        }
            }
            pathRef.addValueEventListener(mListener)

        }


        btnChangePassword.setOnClickListener {
            btnChangePassword.isEnabled = false

            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_change_pass, null)
            val vOld = dialogView.findViewById<EditText>(R.id.pass1)
            val vNew = dialogView.findViewById<EditText>(R.id.pass2)
            val vConfirm = dialogView.findViewById<EditText>(R.id.pass3)

            val builder = AlertDialog.Builder(this)
            with(builder){
                setTitle("Password Change")
                setMessage("Enter details to change your password")
                setIcon(R.drawable.baseline_lock_24)

                setView(dialogView)

                setPositiveButton("Change"){ _, _ ->
                    val old = vOld.text.toString()
                    val new = vNew.text.toString()
                    val confirm = vConfirm.text.toString()
                    if(new != confirm){
                        btnChangePassword.isEnabled = true
                        Toast.makeText(applicationContext,"Passwords doesn't match",Toast.LENGTH_LONG).show()
                        return@setPositiveButton
                    }

                    currentUser?.let {
                        changePassword(currentUser, old,new)
                        return@setPositiveButton
                    }
                    btnChangePassword.isEnabled = true
                }
                setNeutralButton("Cancel"){_, _ ->
                    Toast.makeText(applicationContext,"Cancelled",Toast.LENGTH_LONG).show()
                    btnChangePassword.isEnabled = true
                }
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        btnUpdate.setOnClickListener {
            // Change user's display name
            val profileUpdates = userProfileChangeRequest {
                displayName = vProName.text.toString()
            }

            currentUser?.let {
                it.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile updated.")
                        }
                    }
            }


            val userMap:HashMap<String,Any> = hashMapOf(
                "name" to vProName.text.toString(),
                "email" to vProEmail.text.toString(),
                "contact" to vProContact.text.toString(),
            )

            currentUser?.let {
                db.child("users")
                    .child(it.uid)
                    .setValue(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(
                            baseContext,
                            "Profile updated successfully.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }

        btnDelete.setOnClickListener {
            btnDelete.isEnabled = false

            /////////////////////////////////////
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_password, null)
            val vPasswordDelete = dialogView.findViewById<EditText>(R.id.editTextTextPassword)

            val builder = AlertDialog.Builder(this)
            with(builder){
                setTitle("Authentication")
                setMessage("Enter Password to delete your account")
                setIcon(R.drawable.baseline_lock_24)

                setView(dialogView)


                setPositiveButton("Delete"){ _, _ ->
                    currentUser?.let {
                        if(vPasswordDelete.text.toString().isNotBlank()){
                            deleteAcc(currentUser, vPasswordDelete.text.toString())
                        }else{
                            btnDelete.isEnabled = true
                            Toast.makeText(applicationContext,"Please enter correct password",Toast.LENGTH_LONG).show()
                        }
                        return@setPositiveButton
                    }
                    btnDelete.isEnabled = true
                }
                setNeutralButton("Cancel"){_, _ ->
                    Toast.makeText(applicationContext,"Cancelled",Toast.LENGTH_LONG).show()
                    btnDelete.isEnabled = true
                }
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }



    }


    private fun changePassword(user:FirebaseUser, old:String, new:String){
        if(user.email!=null){
            val credential = EmailAuthProvider.getCredential(user.email!!, old)
            user.reauthenticate(credential)
                .addOnCompleteListener {
                    Log.d(TAG, "User re-authenticated.")

                    ////////////////////////////
                    user.updatePassword(new).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User password updated.")

                            Toast.makeText(
                                baseContext,
                                "Password updated successfully.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }else{

                            Toast.makeText(
                                baseContext,
                                "Account delete failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        btnChangePassword.isEnabled = true
                    }
                    //////////////////////////////
                }
        }
    }

    private fun deleteAcc(user: FirebaseUser,password: String){
        if(user.email!=null){
            val credential = EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(credential)
                .addOnCompleteListener {
                    Log.d(TAG, "User re-authenticated.")

                    ////////////////////////////
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")

                            Toast.makeText(
                                baseContext,
                                "Account deleted successfully.",
                                Toast.LENGTH_SHORT,
                            ).show()


                            ///////////////////////////////
                            // Delete Data from realtime database
                            val db = Firebase.database.reference
                            val docRef = db.child("users").child(user.uid)
                            docRef.removeValue()
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                }
                            ///////////////////////////////

                            val intent = Intent(this,LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }else{
                            btnDelete.isEnabled = true
                            Toast.makeText(
                                baseContext,
                                "Account delete failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                    //////////////////////////////
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pathRef.addValueEventListener(mListener)
    }
}