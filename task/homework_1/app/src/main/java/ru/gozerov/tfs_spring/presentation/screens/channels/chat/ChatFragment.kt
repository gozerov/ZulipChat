package ru.gozerov.tfs_spring.presentation.screens.channels.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.core.utils.VerticalMarginItemDecoration
import ru.gozerov.tfs_spring.databinding.FragmentChatBinding
import ru.gozerov.tfs_spring.di.application.appComponent
import ru.gozerov.tfs_spring.di.features.channels.chat.DaggerChatComponent
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.MainChatAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.date.DateDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message.OwnMessageDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message.UserMessageDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.SelectEmojiFragment
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private lateinit var binding: FragmentChatBinding

    private val adapter = MainChatAdapter()

    private val args: ChatFragmentArgs by navArgs()

    override val initEvent: ChatEvent by lazy {
        ChatEvent.UI.Init(args.channel, args.topic)
    }

    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> by lazy {
        storeFactory
    }

    @Inject
    lateinit var storeFactory: StoreHolder<ChatEvent, ChatEffect, ChatState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = DaggerChatComponent.factory().create(lifecycle, context.appComponent)
        component.inject(this)
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
        viewLifecycleOwner.lifecycleScope.launch {
            state.flowItems?.cachedIn(this)?.collectLatest {
                if (state.fromCache)
                    store.accept(ChatEvent.UI.Init(args.channel, args.topic, false))
                store.accept(ChatEvent.UI.SaveMessages(it))
                adapter.submitData(it)
            }
            state.positionToScroll?.run {
                binding.messageList.postDelayed({
                    binding.messageList.scrollToPosition(
                        binding.messageList.adapter?.itemCount?.minus(
                            1
                        ) ?: 0
                    )
                }, 50)
            }
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

    override fun onDetach() {
        storeHolder.store.accept(ChatEvent.UI.Exit)
        super.onDetach()
    }

}