package com.example.project1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.project1.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity()  {

    companion object {
        private const val TAG: String = "HomeActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController


    private lateinit var auth: FirebaseAuth
    //private var m: ListenerRegistration? = null
    private var pathRef: DatabaseReference? = null
    private var mListener:  ValueEventListener? = null

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI and redirect accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        // Initialize Firebase Auth
        auth = Firebase.auth

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val header = binding.navView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.nav_user_name)
        val headerEmail = header.findViewById<TextView>(R.id.nav_user_email)
        auth.currentUser?.let {
            headerName.text = it.displayName
            headerEmail.text = it.email


            val db = Firebase.database.reference
            pathRef = db.child("users").child(it.uid)
            mListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name: String = snapshot.child("name").value as String
                        headerName.text = name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "get failed with ", error.toException())
                }
            }
            pathRef!!.addValueEventListener(mListener as ValueEventListener)

        }

        //val navView: NavigationView = binding.navView
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
        R.id.nav_income,R.id.nav_expenses,R.id.nav_savings,
            R.id.nav_feed_back,R.id.nav_rate_us,R.id.nav_about_us,
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.menu.findItem(R.id.nav_sign_out).setOnMenuItemClickListener   {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            val builder = AlertDialog.Builder(this)
            with(builder){
                setTitle("Sign Out")
                setMessage("Confirm action please")
                setIcon(R.drawable.baseline_login_24)

                setPositiveButton("log out"){ _, _ ->
                    auth.signOut()
                    val intent = Intent(this@HomeActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                setNeutralButton("Cancel"){_, _ -> }
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            false
        }


        navView.menu.findItem(R.id.nav_about_us).setOnMenuItemClickListener   {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            val builder = AlertDialog.Builder(this)
            with(builder){
                setTitle("About Us")
                setMessage("About us @copyright 2023")
                setIcon(R.drawable.baseline_info_24)
                setNeutralButton("Ok"){_, _ -> }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            false
        }


        //////////////

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.nav_savings) {
//                Toast.makeText(applicationContext,"nav_savings", Toast.LENGTH_LONG).show()
//            } else if (destination.id == R.id.nav_expenses) {
//                Toast.makeText(applicationContext,"nav_expenses", Toast.LENGTH_LONG).show()
//            }else {
//                Toast.makeText(applicationContext,"else", Toast.LENGTH_LONG).show()
//            }
//        }

        homeViewModel.setNav(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_profile) {
            val intent = Intent(this, UsesProActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            this.startActivity(intent)
            return true
        }
//        if (id == R.id.settings) {
//            Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show()
//            return true
//        }

//        if (id == R.id.nav_sign_out) {
//            Toast.makeText(this, "sign out", Toast.LENGTH_LONG).show()
//            return true
//        }
//
//        if(id == R.id.nav_rate_us){
//            Toast.makeText(applicationContext,"rate us", Toast.LENGTH_LONG).show()
//            return  true
//        }

        return super.onOptionsItemSelected(item)
    }



    override fun onDestroy() {
        super.onDestroy()
        //registration?.remove()
        mListener?.let { pathRef?.removeEventListener(it) }
    }


}