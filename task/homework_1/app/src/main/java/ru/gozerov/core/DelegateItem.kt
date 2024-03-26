package ru.gozerov.core

interface DelegateItem {
    fun content(): Any
    fun id(): Int
    fun compareToOther(other: DelegateItem): Boolean
}