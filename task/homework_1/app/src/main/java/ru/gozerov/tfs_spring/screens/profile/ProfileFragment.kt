package ru.gozerov.tfs_spring.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.utils.dp

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.handleIntent(ProfileIntent.LoadProfile)
        updateToolbar(ToolbarState.OnlyStatusColor(R.color.grey_background))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewState.collect { state ->
                    when(state) {
                        is ProfileViewState.Empty -> {}
                        is ProfileViewState.LoadedProfile -> {
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
                        is ProfileViewState.Error -> {}
                    }
                }
            }
        }
    }
}