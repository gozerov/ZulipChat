package ru.gozerov.tfs_spring.screens.channels.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentChatBinding
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.MainChatAdapter
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.date.DateDelegate
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.MessageDelegate
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message.UserMessageDelegate
import ru.gozerov.tfs_spring.screens.channels.chat.dialog.SelectEmojiFragment
import ru.gozerov.tfs_spring.screens.channels.chat.models.ChatIntent
import ru.gozerov.tfs_spring.screens.channels.chat.models.ChatViewState
import ru.gozerov.tfs_spring.utils.VerticalMarginItemDecoration

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val adapter = MainChatAdapter()

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ChatViewModel::class.java]
    }

    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateToolbar(ToolbarState.NavUpWithTitle(R.color.teal_400, args.channel))

        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.messageInputField.addTextChangedListener { editable ->
            val imgRes = if (editable?.toString().isNullOrEmpty())
                R.drawable.ic_add_outline_32 else R.drawable.ic_send_32
            binding.inputActionButton.setImageResource(imgRes)
        }
        binding.inputActionButton.setOnClickListener {
            if (binding.messageInputField.editableText.isNotEmpty()) {
                viewModel.handleIntent(
                    ChatIntent.SendMessage(
                        args.channel,
                        args.topic,
                        binding.messageInputField.editableText.toString()
                    )
                )
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

                viewModel.handleIntent(
                    ChatIntent.AddReaction(
                        messageId,
                        result.getString(SelectEmojiFragment.KEY_EMOJI_NAME, ""),
                        result.getString(SelectEmojiFragment.KEY_EMOJI_TYPE, ""),
                        result.getString(SelectEmojiFragment.KEY_EMOJI_CODE, "")
                    )
                )
            }
        }


        adapter.apply {
            addDelegate(DateDelegate())
            addDelegate(
                UserMessageDelegate(
                    onLongClick = {
                        SelectEmojiFragment.newInstance(it).show(parentFragmentManager, null)
                    },
                    onReactionChanged = { id, reaction ->
                        //viewModel.handleIntent(ChatIntent.UpdateReaction(id, reaction))
                    }
                )
            )
            addDelegate(
                MessageDelegate(
                    onLongClick = {
                        SelectEmojiFragment.newInstance(it).show(parentFragmentManager, null)
                    },
                    onReactionChanged = { id, reaction ->
                        viewModel.handleIntent(ChatIntent.UpdateReaction(id, reaction))
                    }
                )
            )
        }

        lifecycleScope.launch {
            viewModel.handleIntent(ChatIntent.LoadMessages(args.channel, args.topic))
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is ChatViewState.Empty -> {
                            viewModel.handleIntent(ChatIntent.RegisterEventQueue(args.topic))
                        }

                        is ChatViewState.LoadedChat -> {
                            adapter.submitList(state.items)
                            state.positionToScroll?.let {
                                binding.messageList.post {
                                    binding.messageList.scrollToPosition(state.positionToScroll)
                                }
                            }
                        }

                        is ChatViewState.Error -> {}
                    }
                }
            }
        }
        binding.messageList.addItemDecoration(VerticalMarginItemDecoration())
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.messageList.layoutManager = layoutManager
        binding.messageList.adapter = adapter
    }

    override fun onDestroy() {
        parentFragmentManager.clearFragmentResultListener(SelectEmojiFragment.KEY_RESULT)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

    }

}