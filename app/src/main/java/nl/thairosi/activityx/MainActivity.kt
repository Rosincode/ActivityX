package nl.thairosi.activityx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import nl.thairosi.activityx.databinding.ActivityMainBinding
import nl.thairosi.activityx.ui.about.AboutFragment
import nl.thairosi.activityx.ui.home.HomeFragment
import nl.thairosi.activityx.ui.navigation.CriteriaFragment
import nl.thairosi.activityx.ui.navigation.VisitedPlacesFragment

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        //// Fragments container
        val homeFragment = HomeFragment()
        val aboutFragment = AboutFragment()
        val visitedPlacesFragment = VisitedPlacesFragment()
        val criteriaFragment = CriteriaFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, homeFragment)
            commit()
        }


        //// Drawer Menu
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, homeFragment)
                        commit()
                    }
                }
                R.id.miAbout -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, aboutFragment)
                        commit()
                    }
                }
                R.id.miVisitedPlaces -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, visitedPlacesFragment)
                        commit()
                    }
                }
                R.id.miCriteria -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, criteriaFragment)
                        commit()
                    }
                }
            }
            // Close drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}