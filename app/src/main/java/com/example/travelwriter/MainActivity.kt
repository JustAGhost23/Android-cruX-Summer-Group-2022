package com.example.travelwriter

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.travelwriter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        drawerLayout = binding.mainFragmentDrawerLayout

        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        val user = sharedPrefs.getString("user", null)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.mainFragmentNavView, navController)

        if(user == null) {
            navController.navigate(MainFragmentDirections.actionMainFragmentToFirstTimeFragment())
        }

        navController.addOnDestinationChangedListener { navC, navD, args ->
            if(navD.id == navC.graph.startDestinationId) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            if(navD.id == R.id.firstTimeFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setHomeButtonEnabled(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainFragmentNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}