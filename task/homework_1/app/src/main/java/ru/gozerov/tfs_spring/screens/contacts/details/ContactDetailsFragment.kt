package ru.gozerov.tfs_spring.screens.contacts.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.TitleGravity
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsIntent
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsViewState
import ru.gozerov.tfs_spring.utils.dp

class ContactDetailsFragment : Fragment() {

    private val viewModel: ContactDetailsViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ContactDetailsViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ContactDetailsViewModel::class.java]
    }

    private lateinit var binding: FragmentProfileBinding

    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.handleIntent(ContactDetailsIntent.LoadContact(args.contactId))
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is ContactDetailsViewState.Empty -> {}
                        is ContactDetailsViewState.LoadedContact -> {
                            binding.imgAccount.load(state.userContact.imageUrl) {
                                transformations(RoundedCornersTransformation(16f.dp(view.context)))
                            }
                            binding.txtUsername.text = state.userContact.username
                            if (state.userContact.isOnline) {
                                binding.txtOnline.text = getString(R.string.online)
                                binding.txtOnline.setTextColor(view.context.getColor(R.color.green))
                            } else {
                                binding.txtOnline.text = getString(R.string.offline)
                                binding.txtOnline.setTextColor(view.context.getColor(R.color.red))
                            }
                        }

                        is ContactDetailsViewState.Error -> {}
                    }
                }
            }
        }
    }

}