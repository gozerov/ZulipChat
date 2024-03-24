package ru.gozerov.homework_1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import ru.gozerov.homework_1.SecondActivity.Companion.ARG_CONTACTS

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = mutableListOf<String>()
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        cursor?.use { c ->
            while (c.moveToNext()) {
                val nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                if (nameIndex >= 0)
                    data.add(c.getString(nameIndex))
            }
        }
        val newIntent = Intent(SecondActivity.MY_ACTION).putExtra(
            ARG_CONTACTS,
            data.toTypedArray()
        )
        sendBroadcast(newIntent)
        stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

}