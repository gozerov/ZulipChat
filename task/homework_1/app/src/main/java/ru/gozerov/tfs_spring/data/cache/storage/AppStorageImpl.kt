package ru.gozerov.tfs_spring.data.cache.storage

import android.content.SharedPreferences
import javax.inject.Inject

class AppStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): AppStorage {

    override fun getUserId(): Int = sharedPreferences.getInt(KEY_ID, -1)

    override fun saveUserId(id: Int) {
        sharedPreferences
            .edit()
            .putInt(KEY_ID, id)
            .apply()
    }


    companion object {

        const val APP_SHARED_PREFS = "TFSPrefs"

        private const val KEY_ID = "userId"

    }

}