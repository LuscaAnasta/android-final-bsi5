package com.empresa.vitalogfinal.view.menu  // ajuste se necessário

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.empresa.vitalogfinal.R

class MenuActivity : AppCompatActivity() {

    private val FRAG_TAG_DIARIO = "frag_diario"
    private val FRAG_TAG_AGUA = "frag_agua"
    private val FRAG_TAG_REL = "frag_rel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            showFragment(FRAG_TAG_DIARIO)
            bottomNav.selectedItemId = R.id.nav_diario
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_diario -> showFragment(FRAG_TAG_DIARIO)
                R.id.nav_agua -> showFragment(FRAG_TAG_AGUA)
                R.id.nav_relatorio -> showFragment(FRAG_TAG_REL)
            }
            true
        }
    }

    private fun showFragment(tag: String) {
        val fm = supportFragmentManager
        val containerId = R.id.menu_fragment_container
        val transaction = fm.beginTransaction()

        // esconder todos
        val tags = listOf(FRAG_TAG_DIARIO, FRAG_TAG_AGUA, FRAG_TAG_REL)
        for (t in tags) {
            val f = fm.findFragmentByTag(t)
            if (f != null && f.isAdded) transaction.hide(f)
        }

        var fragmentToShow = fm.findFragmentByTag(tag)
        if (fragmentToShow == null) {
            fragmentToShow = when (tag) {
                FRAG_TAG_DIARIO -> DiarioFragment()
                FRAG_TAG_AGUA -> AguaFragment()
                FRAG_TAG_REL -> RelatorioFragment()
                else -> DiarioFragment()
            }
            transaction.add(containerId, fragmentToShow, tag)
        } else {
            transaction.show(fragmentToShow)
        }
        transaction.commit()
    }
}
