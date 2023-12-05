package com.example.project1.ui.home.savings

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project1.models.Saving
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@SuppressLint("StaticFieldLeak")
class SavingsAddViewModel(application: Application) : AndroidViewModel (application) {
    companion object {
        private const val TAG: String = "BottomHomeViewModel"
    }
    private var auth: FirebaseAuth = Firebase.auth


    private val context = getApplication<Application>().applicationContext

    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading

    fun addSavingToDatabase(title:String, amount:Double, date:String, category:String){
        _isLoading.value = true
        val user = auth.currentUser ?: return
        val db = Firebase.database.reference
        db.child("savings").child(user.uid).push()
            .setValue(Saving("",title,amount,date,category))
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                Toast.makeText(
                    context,
                    "Saving added successfully ...",
                    Toast.LENGTH_SHORT,
                ).show()
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                _isLoading.value = false
            }

    }
}