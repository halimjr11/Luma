package com.halimjr11.luma.view.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.color.MaterialColors
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.ActivityMainBinding
import com.halimjr11.luma.utils.Constants.DETAIL_ID_KEY
import com.halimjr11.luma.view.feature.favorite.FavoriteActivity
import com.halimjr11.luma.view.feature.main.fragments.MainStoryFragmentDirections
import com.halimjr11.luma.view.feature.settings.SettingsActivity
import com.google.android.material.R as MaterialRes

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainStory) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.mainAppBar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostMain.id) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        checkIntent()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val iconTint =
                MaterialColors.getColor(binding.mainAppBar, MaterialRes.attr.colorOnPrimary, 0)
            when (destination.id) {
                R.id.detailStoryFragment -> {
                    binding.mainAppBar.menu.clear()
                    binding.mainAppBar.navigationIcon?.setTint(iconTint)
                }

                else -> {
                    binding.mainAppBar.menu.clear()
                    menuInflater.inflate(R.menu.luma_main_menu, binding.mainAppBar.menu)
                    binding.mainAppBar.navigationIcon?.setTint(iconTint)
                }
            }
        }
    }

    private fun checkIntent() {
        intent.getStringExtra(DETAIL_ID_KEY)?.let {
            val action =
                MainStoryFragmentDirections.actionMainStoryFragmentToDetailStoryFragment(it, true)
            navController.navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.luma_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menuFavorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}