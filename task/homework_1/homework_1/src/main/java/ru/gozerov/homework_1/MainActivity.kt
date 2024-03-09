package ru.gozerov.homework_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.gozerov.homework_2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchActivityButton = findViewById<Button>(R.id.launchActivityButton)
        val secondActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.getStringArrayExtra(SecondActivity.ARG_CONTACTS)
                    data?.let {
                        Toast.makeText(
                            this,
                            getString(R.string.total_contacts, it.size),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        launchActivityButton.setOnClickListener {
            secondActivityResult.launch(Intent(this, SecondActivity::class.java))
        }
    }

}