package ru.gozerov.homework_1

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.gozerov.homework_2.R

class SecondActivity : AppCompatActivity() {

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val data = Intent().putExtra(ARG_CONTACTS, intent?.getStringArrayExtra(ARG_CONTACTS))
            setResult(RESULT_OK, data)
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, IntentFilter(MY_ACTION), RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, IntentFilter(MY_ACTION))
        }

        val startServiceButton = findViewById<Button>(R.id.startServiceButton)
        startServiceButton.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_CODE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            startService(Intent(this, MyService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {

        const val MY_ACTION = "MY_ACTION"
        const val ARG_CONTACTS = "CONTACTS"
        private const val PERMISSION_CODE = 1

    }

}