package ru.gozerov.tfs_spring.screens.contacts.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.TitleGravity
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsIntent
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsViewState
import ru.gozerov.tfs_spring.utils.dp

class ContactDetailsFragment : Fragment() {

    private val viewModel: ContactDetailsViewModel by lazy {
        ViewModelProvider(this)[ContactDetailsViewModel::class.java]
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.handleIntent(ContactDetailsIntent.LoadContact(requireArguments().getInt(ARG_ID)))
        updateToolbar(
            ToolbarState.NavUpWithTitle(
                R.color.grey_secondary_background,
                getString(R.string.profile),
                TitleGravity.CENTER
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.logOutButton.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is ContactDetailsViewState.Empty -> {}
                        is ContactDetailsViewState.LoadedContact -> {
                            binding.imgAccount.load(state.imageUrl) {
                                transformations(RoundedCornersTransformation(16f.dp(view.context)))
                            }
                            binding.txtUsername.text = state.username
                            binding.txtStatus.text = state.status
                            if (state.isOnline) {
                                binding.txtOnline.visibility = View.VISIBLE
                                binding.txtOnline.text = getString(R.string.online)
                            } else {
                                binding.txtOnline.visibility = View.GONE
                            }
                        }

                        is ContactDetailsViewState.Error -> {}
                    }
                }
            }
        }
    }

    companion object {

        private const val ARG_ID = "id"

        fun newInstance(id: Int): ContactDetailsFragment =
            ContactDetailsFragment().apply { arguments = bundleOf(ARG_ID to id) }

    }

}