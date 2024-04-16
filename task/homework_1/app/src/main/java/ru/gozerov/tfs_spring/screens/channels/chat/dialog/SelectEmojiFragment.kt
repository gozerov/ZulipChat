package ru.gozerov.tfs_spring.screens.channels.chat.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentSelectEmojiBinding
import ru.gozerov.tfs_spring.screens.channels.chat.ChatViewModel
import ru.gozerov.tfs_spring.utils.GridMarginItemDecoration

class SelectEmojiFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSelectEmojiBinding

    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ChatViewModel::class.java]
    }

    private val adapter = SelectEmojiAdapter { emojiName, emojiType, emojiCode ->
        val messageId = arguments?.getInt(ARG_MESSAGE_ID, 0) ?: 0
        parentFragmentManager.setFragmentResult(
            KEY_RESULT,
            bundleOf(
                KEY_EMOJI_NAME to emojiName,
                KEY_EMOJI_TYPE to emojiType,
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.reactions.collect { reactions ->
                    adapter.data = reactions
                }
            }
        }

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
        const val KEY_EMOJI_TYPE = "emojiTypeKey"
        const val KEY_EMOJI_CODE = "emojiCodeKey"
        const val KEY_RESULT = "result"

        fun newInstance(messageId: Int): SelectEmojiFragment {
            return SelectEmojiFragment().apply {
                this.arguments = bundleOf(ARG_MESSAGE_ID to messageId)
            }
        }

    }

}