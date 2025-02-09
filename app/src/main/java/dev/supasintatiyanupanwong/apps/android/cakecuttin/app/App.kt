package dev.supasintatiyanupanwong.apps.android.cakecuttin.app

import android.app.Application
import android.app.UiModeManager
import android.app.UiModeManager.MODE_NIGHT_YES
import com.google.android.gms.ads.MobileAds
import kotlin.concurrent.thread

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        (getSystemService(UI_MODE_SERVICE) as? UiModeManager)?.nightMode = MODE_NIGHT_YES

        thread { MobileAds.initialize(this) }
    }
}
