package ru.gozerov.tfs_spring.presentation.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.core.utils.dp
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.domain.use_cases.GetOwnUserUseCase
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.ProfileActor
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.ProfileReducer
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEffect
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEvent
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private lateinit var binding: FragmentProfileBinding

    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> by lazy {
        storeFactory()
    }

    override val initEvent: ProfileEvent = ProfileEvent.UI.Init

    private fun storeFactory(): StoreHolder<ProfileEvent, ProfileEffect, ProfileState> {
        val zulipApi = (requireContext().applicationContext as TFSApp).zulipApi
        return LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = ProfileState(),
                reducer = ProfileReducer(),
                actor = ProfileActor(GetOwnUserUseCase(zulipApi))
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        updateToolbar(ToolbarState.OnlyStatusColor(R.color.grey_background))
        return binding.root
    }

    override fun render(state: ProfileState) {
        state.userContact?.run {
            binding.imgAccount.load(imageUrl) {
                transformations(RoundedCornersTransformation(16f.dp(requireContext())))
            }
            binding.txtUsername.text = username
            if (state.userContact.isOnline) {
                binding.txtOnline.text = getString(R.string.online)
                binding.txtOnline.setTextColor(requireContext().getColor(R.color.green))
            } else {
                binding.txtOnline.text = getString(R.string.offline)
                binding.txtOnline.setTextColor(requireContext().getColor(R.color.red))
            }
        }
    }
}