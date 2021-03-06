package moe.pine.emoji.components.common

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import moe.pine.emoji.R

/**
 * Component for ActionBarToggle
 * Created by pine on Apr 21, 2017.
 */
class ActionBarDrawerToggleComponent(
        val activity: AppCompatActivity,
        val drawerLayout: DrawerLayout
) {
    private val drawerToggle by lazy {
        object : ActionBarDrawerToggle(
                activity,
                drawerLayout,
                R.string.open,
                R.string.close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                activity.invalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                activity.invalidateOptionsMenu()
            }
        }
    }

    fun onCreate() {
        this.drawerToggle.isDrawerIndicatorEnabled = true
        this.drawerLayout.addDrawerListener(drawerToggle)
        this.drawerToggle.syncState()
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return this.drawerToggle.onOptionsItemSelected(item)
    }

    fun onPostCreate(savedInstanceState: Bundle?) {
        this.drawerToggle.syncState()
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        this.drawerToggle.onConfigurationChanged(newConfig)
    }
}