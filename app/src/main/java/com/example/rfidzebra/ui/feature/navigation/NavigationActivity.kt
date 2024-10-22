package com.example.rfidzebra.ui.feature.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rfidzebra.R
import com.example.rfidzebra.databinding.ActivityNavigationBinding
import com.example.rfidzebra.ui.feature.navigation.fragment.AccountFragment
import com.example.rfidzebra.ui.feature.navigation.fragment.FunnelFragment
import com.example.rfidzebra.ui.feature.navigation.fragment.HomeFragment
import com.example.rfidzebra.ui.feature.navigation.fragment.ToolsFragment

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        val homeFragment = HomeFragment()
        val toolsFragment = ToolsFragment()
        val funnelFragment = FunnelFragment()
        val accountFragment = AccountFragment()

        setCurrentFragment(homeFragment)

        binding.bottomAppBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setCurrentFragment(homeFragment)
                R.id.navigation_tools -> setCurrentFragment(toolsFragment)
                R.id.navigation_funnel -> setCurrentFragment(funnelFragment)
                R.id.navigation_akun -> setCurrentFragment(accountFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_main, fragment)
            commit()
        }
}