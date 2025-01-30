package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.view.insets

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat

inline val WindowInsetsCompat.systemBars: Insets
    get() = getInsets(WindowInsetsCompat.Type.systemBars())

inline val WindowInsetsCompat.displayCutouts: Insets
    get() = getInsets(WindowInsetsCompat.Type.displayCutout())
