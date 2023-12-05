package com.example.project1.ui.home.savings

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project1.adapters.SavingAdapter
import com.example.project1.models.Saving
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class SavingsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG: String = "SavingsViewModel"
    }
    private var auth: FirebaseAuth = Firebase.auth

    private val db: DatabaseReference = Firebase.database.reference

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _savings = MutableLiveData<List<Saving>>().apply {
        value = emptyList()
    }
    val savings: LiveData<List<Saving>> = _savings


    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading


    val adapter: SavingAdapter = SavingAdapter(
        emptyList(),
        object : SavingAdapter.MyOnItemClickListener {
            override fun onUpdateClick(position: Int, v: View?,saving: Saving) {
                val user = auth.currentUser
                if(user!=null){
                    _isLoading.value = true

                    //val db = Firebase.database.reference
                    db.child("savings").child(user.uid).child(saving.id)
                        .setValue(saving) //set instead update (for quick code)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                            Toast.makeText(
                                context,
                                "Saving updated successfully ...",
                                Toast.LENGTH_SHORT,
                            ).show()
                            _isLoading.value = false
                        }
                        .addOnFailureListener { e:Exception ->
                            Log.w(TAG, "Error saving document", e)
                            Toast.makeText(
                                context,
                                "Error updating ...",
                                Toast.LENGTH_SHORT,
                            ).show()
                            _isLoading.value = false
                        }

                }
            }

            override fun onDeleteClick(position: Int, v: View?,saving: Saving) {
                val user = auth.currentUser
                if(user!=null){
                    _isLoading.value = true

                   // val db = Firebase.firestore
                    db.child("savings").child(user.uid).child(saving.id)
                        .removeValue()
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot Deleted!")
                            Toast.makeText(
                                context,
                                "Saving deleted successfully ...",
                                Toast.LENGTH_SHORT,
                            ).show()
                            _isLoading.value = false
                        }
                        .addOnFailureListener { e:Exception ->
                            Log.w(TAG, "Error deleting document", e)
                            Toast.makeText(
                                context,
                                "Error deleting ...",
                                Toast.LENGTH_SHORT,
                            ).show()
                            _isLoading.value = false
                        }

                }
            }
        }
    )


    private lateinit var mRef: DatabaseReference
    private lateinit var mListener: ValueEventListener
    init {

        val user = auth.currentUser
        if(user!=null){
            //val db = Firebase.firestore


            mRef = db.child("savings").child(user.uid)
            mListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val tempSavings = ArrayList<Saving>()
                    for (shot in dataSnapshot.children) {
                        val save: Saving? = shot.getValue<Saving>()
                        if (save != null) {
                            save.id = shot.key.toString()
                            tempSavings.add(save)
                        }
                    }
                    Log.d(TAG, "Files done")
                    _savings.value = tempSavings
                    adapter.itemList = _savings.value as ArrayList<Saving>
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadata:onCancelled", databaseError.toException())
                }
            }
            mRef.addValueEventListener(mListener)

        }
    }

    override fun onCleared() {
        super.onCleared()
        mRef.removeEventListener(mListener)
    }
}