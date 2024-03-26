package ru.gozerov.tfs_spring.screens.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.FragmentChatBinding
import ru.gozerov.tfs_spring.screens.chat.adapters.MainChatAdapter
import ru.gozerov.tfs_spring.screens.chat.adapters.date.DateDelegate
import ru.gozerov.tfs_spring.screens.chat.adapters.message.MessageDelegate
import ru.gozerov.tfs_spring.screens.chat.adapters.user_message.UserMessageDelegate
import ru.gozerov.tfs_spring.screens.chat.dialog.SelectEmojiFragment
import ru.gozerov.tfs_spring.utils.VerticalMarginItemDecoration

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val adapter = MainChatAdapter()

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.messageInputField.addTextChangedListener { editable ->
            val imgRes = if (editable?.toString().isNullOrEmpty())
                R.drawable.ic_add_outline_32 else R.drawable.ic_send_32
            binding.inputActionButton.setImageResource(imgRes)
        }
        binding.inputActionButton.setOnClickListener {
            if (binding.messageInputField.editableText.isNotEmpty()) {
                viewModel.sendMessage(binding.messageInputField.editableText.toString())
                binding.messageInputField.setText("")
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.setFragmentResultListener(
            SelectEmojiFragment.KEY_RESULT, viewLifecycleOwner
        ) { requestKey, result ->
            if (requestKey == SelectEmojiFragment.KEY_RESULT) {
                val messageId = result.getInt(SelectEmojiFragment.KEY_MESSAGE_ID, 0)
                viewModel.addReaction(
                    messageId,
                    result.getString(SelectEmojiFragment.KEY_EMOJI, "")
                )
            }
        }


        adapter.apply {
            addDelegate(DateDelegate())
            addDelegate(UserMessageDelegate())
            addDelegate(
                MessageDelegate(
                    onLongClick = {
                        SelectEmojiFragment.newInstance(it).show(parentFragmentManager, null)
                    },
                    onReactionChanged = { id, reaction ->
                        viewModel.updateReaction(id, reaction)
                    }
                )
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.messagesWithDate.collect { items ->
                    adapter.submitList(items)
                }
            }
        }
        binding.messageList.addItemDecoration(VerticalMarginItemDecoration())
        binding.messageList.adapter = adapter
    }

    override fun onDestroy() {
        parentFragmentManager.clearFragmentResultListener(SelectEmojiFragment.KEY_RESULT)
        super.onDestroy()
    }

}