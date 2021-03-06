package moe.pine.emoji.view.main

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import moe.pine.emoji.R
import moe.pine.emoji.activity.SettingActivity
import moe.pine.emoji.activity.WebViewActivity
import moe.pine.emoji.fragment.main.VersionInfoDialogFragment
import moe.pine.emoji.model.event.main.ShowMyHistoryEvent
import moe.pine.emoji.model.event.main.ShowOurHistoryEvent
import moe.pine.emoji.model.value.WebViewPage
import moe.pine.emoji.util.eventBus

/**
 * MainNavigationView
 * Created by pine on Apr 20, 2017.
 */
class MainNavigationView : NavigationView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var layout: DrawerLayout

    fun setupView(layout: DrawerLayout) {
        this.layout = layout

        layout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                this@MainNavigationView.clear()
            }
        })

        this.setNavigationItemSelectedListener {
            this.onNavigationItemSelected(it)
            true
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) {
        this.layout.closeDrawers()

        when (item.itemId) {
            R.id.menu_my_history -> {
                this.eventBus.post(ShowMyHistoryEvent())
            }
            R.id.menu_our_history -> {
                this.eventBus.post(ShowOurHistoryEvent())
            }
            R.id.menu_setting -> {
                this.context.startActivity(SettingActivity.createIntent(this.context))
            }
            R.id.menu_contact -> {
                val intent = WebViewActivity.createIntent(this.context, WebViewPage.CONTACT)
                this.context.startActivity(intent)
            }
            R.id.menu_privacy -> {
                val intent = WebViewActivity.createIntent(this.context, WebViewPage.PRIVACY)
                this.context.startActivity(intent)
            }
            R.id.menu_copyright -> {
                val intent = WebViewActivity.createIntent(this.context, WebViewPage.COPYRIGHT)
                this.context.startActivity(intent)
            }
            R.id.menu_version_info -> {
                val activity = this.context as? AppCompatActivity
                val dialog = VersionInfoDialogFragment.newInstance()
                activity?.supportFragmentManager?.let { dialog.show(it, null) }
            }
        }
    }

    private fun clear() {
        val skipIds = listOf(R.id.menu_my_history, R.id.menu_our_history)

        repeat(this.menu.size()) { i ->
            val item = this.menu.getItem(i)
            if (!skipIds.contains(i)) {
                item.isChecked = false
            }

            if (item.hasSubMenu()) {
                repeat(item.subMenu.size()) { j ->
                    if (!skipIds.contains(j)) {
                        item.subMenu.getItem(j).isChecked = false
                    }
                }
            }
        }
    }
}