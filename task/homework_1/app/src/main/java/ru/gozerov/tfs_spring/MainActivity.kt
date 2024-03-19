package ru.gozerov.tfs_spring

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

private val emojiList =
    mutableListOf(Emoji("\uD83D\uDE00", 1), Emoji("\uD83D\uDE00", 1), Emoji("\uD83D\uDE00", 1))

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chatCardLayout = findViewById<ChatCardLayout>(R.id.chatCardLayout)

        val setAvatar = findViewById<Button>(R.id.setAvatar)
        val setName = findViewById<Button>(R.id.setName)
        val nameInputField = findViewById<EditText>(R.id.nameInputField)
        val setMessage = findViewById<Button>(R.id.setMessage)
        val messageInputField = findViewById<EditText>(R.id.messageInputField)
        val addEmoji = findViewById<Button>(R.id.addEmoji)

        chatCardLayout.name = "Title"
        chatCardLayout.date = "12:00"
        chatCardLayout.message =
            "messi messi messi messi messi messi messi messi messi messi messi messi messi messi messi messi messi"
        chatCardLayout.addEmoji(emojiList)
        chatCardLayout.addOnAddButtonClickListener {
            val emoji = Emoji("\uD83D\uDE00", Random.nextInt(1, 1000), Random.nextBoolean())
            emojiList.add(emoji)
            chatCardLayout.addEmoji(emoji)
        }
        chatCardLayout.addOnEmojiChangedListener { view ->
            Log.e(view.emoji, view.count.toString())
        }

        setAvatar.setOnClickListener {
            chatCardLayout.imageDrawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
        }

        setName.setOnClickListener {
            chatCardLayout.name = nameInputField.editableText.toString()
        }

        setMessage.setOnClickListener {
            chatCardLayout.message = messageInputField.editableText.toString()
        }

        addEmoji.setOnClickListener {
            val emoji = Emoji("\uD83D\uDE00", Random.nextInt(1, 1000), Random.nextBoolean())
            emojiList.add(emoji)
            chatCardLayout.addEmoji(emoji)
        }

    }

}