package com.example.project1.ui.feedback

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FeedbackViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG: String = "FeedbackViewModel"
    }
    private var auth: FirebaseAuth = Firebase.auth

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading


    fun giveFeedback(name:String, email:String, description:String, experience:String){

        val user = auth.currentUser ?: return
        val db = Firebase.database.reference
        _isLoading.value = true



        val docData = hashMapOf(
            "uid" to user.uid,
            "time" to ServerValue.TIMESTAMP,
            "name" to name,
            "email" to email,
            "description" to description,
            "experience" to experience,
        )

        db.child("feedbacks").push().setValue(docData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                Toast.makeText(
                    context,
                    "Feedback sent successfully ...",
                    Toast.LENGTH_SHORT,
                ).show()
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(
                    context,
                    "Feedback error ...",
                    Toast.LENGTH_SHORT,
                ).show()
                _isLoading.value = false
            }

    }
}