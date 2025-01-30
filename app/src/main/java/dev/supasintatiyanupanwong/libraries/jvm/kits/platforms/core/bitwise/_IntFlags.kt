@file:Suppress("NOTHING_TO_INLINE") // Aliases to Public APIs

package dev.supasintatiyanupanwong.libraries.jvm.kits.platforms.core.bitwise

inline operator fun Int.contains(flag: Int): Boolean {
    return this and flag == flag
}
