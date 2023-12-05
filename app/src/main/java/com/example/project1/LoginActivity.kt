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
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "LoginActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var vProgressBar: ProgressBar
    private lateinit var btnLogin: Button

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI and redirect accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            redirectToNextScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.no_Account)
        btnLogin = findViewById(R.id.loginButton)
        val vEmail = findViewById<EditText>(R.id.email)
        val vPassword = findViewById<EditText>(R.id.password)
        vProgressBar = findViewById(R.id.progressBar3)

        btnRegister.setOnClickListener{
            val intent = Intent(this,RegActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            vProgressBar.visibility = ProgressBar.VISIBLE
            btnLogin.isEnabled = false
            val email = vEmail.text.toString()
            val password = vPassword.text.toString()
            if(email.isBlank() || password.isBlank()){
                Toast.makeText(baseContext,
                    "Please enter details ...",
                    Toast.LENGTH_SHORT,
                ).show()
            }else{
                loginWithEmailPassword(email,password)
            }

        }
    }


    // login using email and password from firebase auth system
    private fun loginWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    onSignInResult(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    onSignInResult(null)
                }
            }
    }

    // on result from sign in
    private fun onSignInResult(user: FirebaseUser?){
        vProgressBar.visibility = ProgressBar.GONE
        btnLogin.isEnabled = true
        if(user != null){
            //success
            Toast.makeText(
                baseContext,
                "Authentication Success.",
                Toast.LENGTH_SHORT,
            ).show()
            redirectToNextScreen()
        }else{
            //error
            Toast.makeText(
                baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT,

            ).show()
        }
    }

    // redirects user to the Home page, if the user is logged in
    private fun redirectToNextScreen(){
        val intent = Intent(this,HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}