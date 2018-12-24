package com.nibrasco.freshksa

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.nibrasco.freshksa.Model.Session
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        //Fragment f;
        //if (Session.getInstance().Cart().GetCount() > 0)
        //{
        //    f = new CartFragment();
        //}
        //else {
        //    f = new OrderItemFragment();
        //}
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.homeContainer, f);
        //ft.commit();
    }

    override fun onStart() {
        super.onStart()
        val f = OrderItemFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.homeContainer, f)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawer(GravityCompat.END)
        }

        //else {
        //    //super.onBackPressed();
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        drawer_layout.closeDrawer(GravityCompat.END)
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        var fragment: Fragment? = null
        when (id) {
            R.id.navUser -> fragment = UserInfoFragment()
            R.id.navCart -> fragment = CartFragment()
            R.id.navAbout -> fragment = AppInfoFragment()
        }
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.homeContainer, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
