@file:Suppress("NOTHING_TO_INLINE") // Aliases to Public APIs

package dev.supasintatiyanupanwong.libraries.android.kits.platforms.core.graphics

import androidx.core.graphics.Insets

inline infix fun Insets.or(other: Insets): Insets = Insets.max(this, other)
