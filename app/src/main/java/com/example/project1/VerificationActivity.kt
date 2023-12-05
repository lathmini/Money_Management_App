package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VerificationActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "VerificationActivity"
    }

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI and redirect accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        auth.addAuthStateListener {fAuth ->
            fAuth.currentUser?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reloadedUser = auth.currentUser
                    reloadedUser?.let {
                        if(it.isEmailVerified){
                            Toast.makeText(baseContext, "Verification Success " + reloadedUser.email, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val vInfo = findViewById<TextView>(R.id.textView3)

        val user = auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Verification email sent to " + user.email, Toast.LENGTH_SHORT).show()

                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        vInfo.text = "Verification email sent to,\n"+user.email+"\nPlease check your inbox and verify your email"
                    }

                }else {
                    Log.e(TAG, "send EmailVerification fail", task.exception);
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }




        val btnCheckVerification = findViewById<Button>(R.id.button)
        btnCheckVerification.setOnClickListener {
            checkVerification(user)
        }

    }



    private fun checkVerification(user: FirebaseUser){
        user.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reloadedUser = auth.currentUser
                reloadedUser?.let {
                    if(it.isEmailVerified){
                        Toast.makeText(baseContext, "Verification Success " + user.email, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }


}