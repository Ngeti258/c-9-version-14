package com.example.oliver

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.oliver.Announcements.AnnouncementCreationFragment
import com.example.oliver.Announcements.AnnouncementsFragment
import com.example.oliver.Produce.ProductsFragment


import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class FarmerActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val headerView = navView.getHeaderView(0)
        auth = FirebaseAuth.getInstance()
        val logoutButton = headerView.findViewById<Button>(R.id.btn_logout)

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(AnnouncementsFragment(),it.title.toString())
                R.id.nav_enquiries -> replaceFragment(MainActivityFragment(),it.title.toString())
                R.id.nav_make_orders-> replaceFragment(MakeOrdersFragment(),it.title.toString())
                R.id.nav_history-> replaceFragment(HistoryFragment(),it.title.toString())
                R.id.nav_balance-> replaceFragment(BalanceFragment(),it.title.toString())
                R.id.nav_profile -> replaceFragment(ProfileFragment(),it.title.toString())
            }
            true
        }
        // Set the home fragment as the default fragment
        val defaultFragment = AnnouncementsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, defaultFragment).commit()
        title = "Farmer Home"
    }
    private fun replaceFragment(fragment: Fragment, title : String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}

