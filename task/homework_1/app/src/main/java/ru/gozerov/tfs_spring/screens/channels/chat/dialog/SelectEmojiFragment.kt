package ru.gozerov.tfs_spring.screens.channels.chat.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.databinding.FragmentSelectEmojiBinding
import ru.gozerov.tfs_spring.screens.channels.chat.ChatViewModel
import ru.gozerov.tfs_spring.utils.GridMarginItemDecoration

class SelectEmojiFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSelectEmojiBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val adapter = SelectEmojiAdapter {
        val messageId = arguments?.getInt(ARG_MESSAGE_ID, 0) ?: 0
        parentFragmentManager.setFragmentResult(
            KEY_RESULT,
            bundleOf(KEY_EMOJI to it, KEY_MESSAGE_ID to messageId)
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
        const val KEY_EMOJI = "emojiKey"
        const val KEY_RESULT = "result"

        fun newInstance(messageId: Int): SelectEmojiFragment {
            return SelectEmojiFragment().apply {
                this.arguments = bundleOf(ARG_MESSAGE_ID to messageId)
            }
        }

    }

}