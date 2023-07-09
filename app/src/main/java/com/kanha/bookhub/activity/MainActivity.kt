package com.kanha.bookhub.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.kanha.bookhub.R
import com.kanha.bookhub.fragment.AboutAppFragment
import com.kanha.bookhub.fragment.DashboardFragment
import com.kanha.bookhub.fragment.FavouriteFragment
import com.kanha.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    //declaring the variables
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialising the view with their ids
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        //setting up the action bar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //opening the dashboard fragment as default
        openFragment(DashboardFragment(), "Dashboard", R.id.dashboard)

        //for initialising the drawer layout
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.closed_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //setting the onclick listener for the menu items
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    openFragment(DashboardFragment(), "Dashboard", R.id.dashboard)
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
                    openFragment(FavouriteFragment(), "Favourite", R.id.favourite)
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    openFragment(ProfileFragment(), "Profile", R.id.profile)
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    openFragment(AboutAppFragment(), "About App", R.id.aboutApp)
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    //for checking the item which is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    //simple function to fragment to open fragments
    private fun openFragment(frag: Fragment, title: String, id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, frag)
            .commit()
        supportActionBar?.title = title
        navigationView.setCheckedItem(id)
    }

    //modifying the back button
    override fun onBackPressed() {
        var frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is DashboardFragment -> openFragment(DashboardFragment(), "Dashboard", R.id.dashboard)
            else -> super.onBackPressed()
        }
    }

}