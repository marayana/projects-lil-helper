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


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // bottom navigation
        val bottomNav: BottomNavigationView = binding.bottomNav

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.fragmentHome, R.id.fragmentCheckLists, R.id.fragmentNotes, R.id.fragmentRecipes))
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
        /*bottomNav.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually

            Log.e("nav", (item.itemId == R.id.fragmentRecipeDetails).toString())
            when(item.itemId){
                R.id.fragmentAddNote -> navController.popBackStack(R.id.fragmentNotes, false)
                R.id.fragmentEditNote -> navController.popBackStack(R.id.fragmentNotes, false)
                R.id.fragmentAddCheckList -> navController.popBackStack(R.id.fragmentCheckLists, false)
                R.id.fragmentRecipeDetails -> navController.popBackStack(R.id.fragmentRecipes,false)
            }

            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }*/

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean{
        return when(item.itemId){
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
*  TODO: styles
*  TODO: settings (-> ask again before deleting note/list + excluded ingredients)
*  TODO: Error handling in recipes (errorDB vs errorAPI)
*  TODO: Clean up gradle scripts (proper versioning)
*  TODO: Check if loops in recipe repo are even necessary
*  TODO: Check why notes repo doesn't need withContext()
*  */
