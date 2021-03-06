package moe.pine.emoji.activity

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.annotation.UiThread
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*
import moe.pine.emoji.BuildConfig
import moe.pine.emoji.fragment.splash.StartupErrorDialogFragment
import moe.pine.emoji.lib.emoji.ApiCallback
import moe.pine.emoji.lib.emoji.ApiClient
import moe.pine.emoji.lib.emoji.JsonSerializer
import moe.pine.emoji.lib.emoji.model.Font
import moe.pine.emoji.value.LogicValues
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Activity for splash
 * Created by pine on Apr 18, 2017.
 */
class SplashActivity : AppCompatActivity(), ApiCallback<List<Font>> {
    companion object {
        val SPLASH_DELAY_MS = 600L
        val PROGRESS_DELAY_MS = 1000L
    }

    private val handler: Handler = Handler()
    private val client by lazy { ApiClient() }

    private val progressing = AtomicBoolean(false)
    private var startTime: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(moe.pine.emoji.R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        this.startTime = System.nanoTime()
        this.fetchFonts()

        this.handler.postDelayed({
            this.progress_bar.visibility = View.VISIBLE
        }, PROGRESS_DELAY_MS)
    }

    @UiThread
    private fun fetchFonts() {
        if (!this.progressing.compareAndSet(false, true)) return
        this.client.fetchFonts(this)
    }

    @UiThread
    override fun onFailure(e: IOException?) {
        this.progressing.set(false)
        if (BuildConfig.DEBUG) e?.printStackTrace()

        val dialog = StartupErrorDialogFragment.newInstance()
        this.supportFragmentManager?.let { dialog.show(it, null) }
    }

    @UiThread
    override fun onResponse(response: List<Font>) {
        this.progressing.set(false)
        this.saveFonts(response)
        this.startApp()
    }

    @UiThread
    private fun saveFonts(fonts: List<Font>) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString(LogicValues.SharedPreferences.FONTS_KEY, JsonSerializer.fontsToJson(fonts))
        editor.apply()
    }

    @UiThread
    private fun startApp() {
        val currentTime = System.nanoTime()
        val diffMs: Long = this.startTime?.let { (currentTime - it) / 1000_000 } ?: Long.MAX_VALUE

        if (diffMs > SPLASH_DELAY_MS) {
            this.startActivity(MainActivity.createIntent(this))
        } else {
            this.handler.postDelayed({
                this.startActivity(MainActivity.createIntent(this))
            }, SPLASH_DELAY_MS - diffMs)
        }
    }
}
