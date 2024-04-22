package ru.gozerov.tfs_spring.presentation.screens.channels.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.core.utils.VerticalMarginItemDecoration
import ru.gozerov.tfs_spring.databinding.FragmentChatBinding
import ru.gozerov.tfs_spring.domain.use_cases.AddReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.DeleteEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetEventsFromQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetMessagesUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RegisterEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RemoveReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SendMessageUseCase
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.MainChatAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.SelectEmojiFragment
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.ChatActor
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.ChatReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private lateinit var binding: FragmentChatBinding

    private val adapter = MainChatAdapter()

    private val args: ChatFragmentArgs by navArgs()

    override val initEvent: ChatEvent by lazy {
        ChatEvent.UI.Init(args.channel, args.topic)
    }

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

                storeHolder.store.accept(
                    ChatEvent.UI.SendMessage(
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
                storeHolder.store.accept(
                    ChatEvent.UI.AddReaction(
                        messageId,
                        result.getString(SelectEmojiFragment.KEY_EMOJI_NAME, ""),
                        result.getString(SelectEmojiFragment.KEY_EMOJI_CODE, "")
                    )
                )
            }
        }

        adapter.apply {
            addDelegate(DateDelegate())
            addDelegate(
                OwnMessageDelegate(
                    onLongClick = {
                        SelectEmojiFragment.newInstance(it).show(parentFragmentManager, null)
                    },
                    onReactionChanged = { id, reaction ->
                        storeHolder.store.accept(ChatEvent.UI.UpdateReaction(id, reaction))
                    }
                )
            )
            addDelegate(
                UserMessageDelegate(
                    onLongClick = {
                        SelectEmojiFragment.newInstance(it).show(parentFragmentManager, null)
                    },
                    onReactionChanged = { id, reaction ->
                        storeHolder.store.accept(ChatEvent.UI.UpdateReaction(id, reaction))
                    }
                )
            )
        }
        binding.messageList.addItemDecoration(VerticalMarginItemDecoration())
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.messageList.layoutManager = layoutManager
        binding.messageList.adapter = adapter
        binding.messageList.setHasFixedSize(true)
    }

    override fun render(state: ChatState) {
        state.items?.run { adapter.submitList(this) }
        state.positionToScroll?.run {
            binding.messageList.postDelayed({ binding.messageList.scrollToPosition(this) }, 50)
        }
    }

    override fun handleEffect(effect: ChatEffect) = when (effect) {
        is ChatEffect.ShowError -> Snackbar
            .make(requireView(), "Error", Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        parentFragmentManager.clearFragmentResultListener(SelectEmojiFragment.KEY_RESULT)
        super.onDestroy()
    }

    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> by lazy {
        storeFactory()
    }

    private fun storeFactory(): StoreHolder<ChatEvent, ChatEffect, ChatState> {
        val zulipApi = (requireContext().applicationContext as TFSApp).zulipApi
        val zulipLongPollingApi =
            (requireContext().applicationContext as TFSApp).zulipLongPollingApi
        return LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = ChatState(),
                reducer = ChatReducer(),
                actor = ChatActor(
                    GetMessagesUseCase(zulipApi),
                    SendMessageUseCase(zulipApi),
                    RegisterEventQueueUseCase(zulipLongPollingApi),
                    AddReactionUseCase(zulipApi),
                    RemoveReactionUseCase(zulipApi),
                    GetEventsFromQueueUseCase(zulipLongPollingApi),
                    DeleteEventQueueUseCase(zulipLongPollingApi)
                )
            )
        }
    }

    override fun onDetach() {
        storeHolder.store.accept(ChatEvent.UI.Exit)
        super.onDetach()
    }

}