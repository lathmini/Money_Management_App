package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "RegActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var vProgressBar : ProgressBar
    private lateinit var btnRegister : Button

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI and redirect accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(baseContext,
                "Please Logout before registering ...",
                Toast.LENGTH_SHORT,
            ).show()

            val intent = Intent(this,UsesProActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        // Initialize Firebase Auth
        auth = Firebase.auth


        val vName = findViewById<EditText>(R.id.username)
        val vEmail = findViewById<EditText>(R.id.email)
        val vContact = findViewById<EditText>(R.id.contact)
        val vPassword = findViewById<EditText>(R.id.password)
        val vConfirmPassword = findViewById<EditText>(R.id.password1)
        vProgressBar = findViewById(R.id.progressBar)


        btnRegister = findViewById(R.id.loginButton)

        btnRegister.setOnClickListener {
            val name = vName.text.toString()
            val email = vEmail.text.toString()
            val contact = vContact.text.toString()
            val password = vPassword.text.toString()
            val confirmPassword = vConfirmPassword.text.toString()
            if(name.isBlank() ||
                email.isBlank() ||
                contact.isBlank() ||
                password.isBlank() ||
                confirmPassword.isBlank()
            ){
                Toast.makeText(baseContext,
                    "Please enter details ...",
                    Toast.LENGTH_SHORT,
                ).show()
            }else{
                if(password.length < 6){
                    Toast.makeText(baseContext,
                        "Week password. Password must contain at least 6 characters.",
                        Toast.LENGTH_LONG,
                    ).show()
                    return@setOnClickListener
                }

                // Check password matching
                if(password == confirmPassword){
                    registerUser(name,email,password,contact)
                }else {
                    Toast.makeText(baseContext,
                        "Passwords mismatch ...",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

    }

    private fun registerUser(name: String, email: String, password: String, contact: String){
        vProgressBar.visibility = ProgressBar.VISIBLE
        btnRegister.isEnabled = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                vProgressBar.visibility = ProgressBar.INVISIBLE
                btnRegister.isEnabled = true
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    Toast.makeText(baseContext,
                        "Registration successful ...",
                        Toast.LENGTH_SHORT,
                    ).show()
                    setUserDataAndRedirectToVerifyEmail(name, contact , user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Registration failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
    }

    // Create User and add Data to the fireStore database
    private fun setUserDataAndRedirectToVerifyEmail(name: String, contact: String, user: FirebaseUser?){
        if(user == null){
            return
        }

        val db = Firebase.database.reference

        val userMap = hashMapOf(
            "uid" to user.uid,
            "name" to name,
            "email" to user.email,
            "contact" to contact,
        )

        // Add a new document with a generated ID
        db.child("users")
            .child(user.uid)
            .setValue(userMap)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        // Change user's display name
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

        // Redirect to verify email
        val intent = Intent(this,VerificationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

    }



}