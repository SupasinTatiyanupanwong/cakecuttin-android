package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.app.activity

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

inline val Activity.rootView: ViewGroup
    get() = findViewById(android.R.id.content)

inline fun Activity.setOnApplyWindowInsetsListener(
    crossinline action: (view: View, insets: WindowInsetsCompat) -> WindowInsetsCompat
) {
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets -> action(view, insets) }
}
