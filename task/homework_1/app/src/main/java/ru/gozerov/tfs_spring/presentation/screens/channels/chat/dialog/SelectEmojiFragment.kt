package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.gozerov.tfs_spring.core.utils.GridMarginItemDecoration
import ru.gozerov.tfs_spring.databinding.FragmentSelectEmojiBinding
import ru.gozerov.tfs_spring.domain.use_cases.GetReactionsUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.SelectEmojiActor
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.SelectEmojiReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiState
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

class SelectEmojiFragment : BottomSheetDialogFragment(),
    ElmDelegate<SelectEmojiEvent, SelectEmojiEffect, SelectEmojiState> {

    private lateinit var binding: FragmentSelectEmojiBinding

    override fun handleEffect(effect: SelectEmojiEffect) = when (effect) {
        is SelectEmojiEffect.ShowError -> Snackbar
            .make(requireView(), "Error", Snackbar.LENGTH_SHORT)
            .show()
    }

    override val initEvent: SelectEmojiEvent = SelectEmojiEvent.UI.LoadEmojiList

    override val storeHolder: StoreHolder<SelectEmojiEvent, SelectEmojiEffect, SelectEmojiState> by lazy {
        storeFactory()
    }

    @Suppress("LeakingThis", "UnusedPrivateMember")
    private val elm = ElmScreen(this, lifecycle) { requireActivity() }
    private fun storeFactory(): StoreHolder<SelectEmojiEvent, SelectEmojiEffect, SelectEmojiState> {
        return LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = SelectEmojiState(),
                reducer = SelectEmojiReducer(),
                actor = SelectEmojiActor(GetReactionsUseCase)
            )
        }
    }

    override fun render(state: SelectEmojiState) {
        state.reactions?.run {
            adapter.data = this
        }
    }


    private val adapter = SelectEmojiAdapter { emojiName, emojiCode ->
        val messageId = arguments?.getInt(ARG_MESSAGE_ID, 0) ?: 0
        parentFragmentManager.setFragmentResult(
            KEY_RESULT,
            bundleOf(
                KEY_EMOJI_NAME to emojiName,
                KEY_EMOJI_CODE to emojiCode,
                KEY_MESSAGE_ID to messageId
            )
        )
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(GridMarginItemDecoration(spanCount = SPAN_COUNT))
        binding.root.layoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)
    }

    companion object {

        private const val SPAN_COUNT = 7
        const val ARG_MESSAGE_ID = "messageIdArg"
        const val KEY_MESSAGE_ID = "messageIdKey"
        const val KEY_EMOJI_NAME = "emojiNameKey"
        const val KEY_EMOJI_CODE = "emojiCodeKey"
        const val KEY_RESULT = "result"

        fun newInstance(messageId: Int): SelectEmojiFragment {
            return SelectEmojiFragment().apply {
                this.arguments = bundleOf(ARG_MESSAGE_ID to messageId)
            }
        }

    }

}