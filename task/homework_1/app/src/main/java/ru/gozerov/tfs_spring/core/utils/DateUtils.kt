package ru.gozerov.tfs_spring.core.utils

fun mapMonth(index: Int): String {
    return when (index) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> error("Unexpected month index: $index")
    }
}