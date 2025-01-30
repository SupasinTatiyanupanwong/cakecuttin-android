@file:Suppress("NOTHING_TO_INLINE") // Aliases to Public APIs

package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.graphics

import androidx.core.graphics.Insets

inline operator fun Insets.plus(other: Insets): Insets = Insets.add(this, other)
