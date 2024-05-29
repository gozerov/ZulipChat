package ru.gozerov.tfs_spring.data.cache.storage

interface AppStorage {

    fun getUserId(): Int

    fun saveUserId(id: Int)

}