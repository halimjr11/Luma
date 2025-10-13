package com.halimjr11.luma.data.utils

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Float?.orZero(): Float {
    return this ?: 0f
}

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}