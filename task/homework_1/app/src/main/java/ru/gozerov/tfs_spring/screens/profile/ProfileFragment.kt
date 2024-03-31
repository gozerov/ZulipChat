package ru.gozerov.tfs_spring.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.utils.dp

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewState.collect { state ->
                    when(state) {
                        is ProfileViewState.Empty -> {}
                        is ProfileViewState.LoadedProfile -> {
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
                        is ProfileViewState.Error -> {}
                    }
                }
            }
        }
    }
}