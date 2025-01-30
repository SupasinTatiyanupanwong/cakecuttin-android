package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets.navigationBars

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import dev.supasintatiyanupanwong.libraries.jvm.kits.platforms.core.bitwise.contains

@Suppress("DEPRECATION")
inline var Window.isLightNavigationBarsForeground: Boolean
    get() =
        if (Build.VERSION.SDK_INT >= 26) {
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR !in decorView.systemUiVisibility
        } else {
            false
        }
    set(value) =
        if (Build.VERSION.SDK_INT >= 26) {
            if (value) {
                decorView.systemUiVisibility = decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            } else {
                attributes.flags = attributes.flags or
                        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS and
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION.inv()
                decorView.systemUiVisibility = decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        } else {
            // No-op
        }

inline var Activity.isLightNavigationBarsForeground: Boolean
    get() = window?.isLightNavigationBarsForeground ?: false
    set(value) {
        window?.isLightNavigationBarsForeground = value
    }
