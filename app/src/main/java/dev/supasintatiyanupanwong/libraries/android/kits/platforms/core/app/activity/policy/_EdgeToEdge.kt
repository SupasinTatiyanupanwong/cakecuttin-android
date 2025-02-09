package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.app.activity.policy

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

private val DEFAULT_SCRIM_LIGHT = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DEFAULT_SCRIM_NIGHT = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@Suppress("DEPRECATION")
fun Activity.applyEdgeToEdge() {
    val view = window.decorView

    view.systemUiVisibility = view.systemUiVisibility or
            // Tells the system that the window wishes the content to
            // be laid out as if the navigation bar was hidden
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            // Tells the system that the window wishes the content to
            // be laid out fullscreen, behind the status bar
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            // Tells the system that the window wishes the content to
            // be laid out at the most extreme scenario of any other flags
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    if (Build.VERSION.SDK_INT >= 23) {
        val isNightMode = (view.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES

        window.statusBarColor =
            if (Build.VERSION.SDK_INT >= 29) {
                Color.TRANSPARENT
            } else {
                if (isNightMode) DEFAULT_SCRIM_NIGHT else DEFAULT_SCRIM_LIGHT
            }
        window.navigationBarColor =
            if (Build.VERSION.SDK_INT >= 29) {
                Color.TRANSPARENT
            } else {
                if (isNightMode) DEFAULT_SCRIM_NIGHT else DEFAULT_SCRIM_LIGHT
            }

        if (isNightMode) {
            view.systemUiVisibility = view.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            if (Build.VERSION.SDK_INT >= 26) {
                view.systemUiVisibility = view.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        } else {
            view.systemUiVisibility = view.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= 26) {
                view.systemUiVisibility = view.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    if (Build.VERSION.SDK_INT >= 29) {
        window.isStatusBarContrastEnforced = false
        window.isNavigationBarContrastEnforced = true
    }

    if (Build.VERSION.SDK_INT >= 28) {
        window.attributes.layoutInDisplayCutoutMode = if (Build.VERSION.SDK_INT >= 30) {
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        } else {
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
}
