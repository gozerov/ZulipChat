package ru.gozerov.tfs_spring.core.utils

fun getEmojiByUnicode(reactionCode: String, type: String): String {
    return if (type == "unicode_emoji") {
        if (reactionCode.contains('-')) {

            val points = reactionCode.split('-').map { it.toInt(16) }.toIntArray()
            String(points, 0, points.size)
        } else String(Character.toChars(reactionCode.toInt(16)))
    } else ""
}