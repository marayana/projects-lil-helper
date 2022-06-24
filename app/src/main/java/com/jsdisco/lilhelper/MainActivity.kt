package com.jsdisco.lilhelper

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jsdisco.lilhelper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // custom toolbar (NoActionBar)
        setSupportActionBar(binding.toolbar)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // bottom navigation
        val bottomNav: BottomNavigationView = binding.bottomNav

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentHome,
                R.id.fragmentNotes,
                R.id.fragmentRecipes,
                R.id.fragmentSettings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { item ->

            NavigationUI.onNavDestinationSelected(item, navController)

            val graph = navController.graph.findNode(item.itemId) as? NavGraph?

            graph?.startDestinationId?.let {
                navController.popBackStack(it, false)
            }

            return@setOnItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_settings -> {
                Log.e("MainActivity", "Settings clicked")
                true
            }
            R.id.toolbar_cloud -> {
                Log.e("MainActivity", "Download clicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

/*
*  Possibility? One global setup function in MainActivity/Home Screen when the app starts, which fills the database once if it's empty
*
*  TODO: REMOVE LISTS
*  TODO: Input validation when adding/editing notes
*  TODO: styles
*  TODO: settings (-> ask again before deleting note/list)
*  TODO: Error handling in recipes (errorDB vs errorAPI)
*  TODO: Check if loops in recipe repo are even necessary
*  TODO: Check why notes repo doesn't need withContext() (probably because DAO returns LiveData?)
*  */
