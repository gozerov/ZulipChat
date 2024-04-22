package ru.gozerov.tfs_spring.presentation.screens.contacts.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.core.utils.dp
import ru.gozerov.tfs_spring.databinding.FragmentProfileBinding
import ru.gozerov.tfs_spring.domain.use_cases.GetContactByIdUseCase
import ru.gozerov.tfs_spring.presentation.activity.TitleGravity
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.ContactDetailsActor
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.ContactDetailsReducer
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ContactDetailsFragment :
    ElmFragment<ContactDetailsEvent, ContactDetailsEffect, ContactDetailsState>() {

    private lateinit var binding: FragmentProfileBinding

    private val args: ContactDetailsFragmentArgs by navArgs()

    override val initEvent: ContactDetailsEvent by lazy {
        ContactDetailsEvent.UI.Init(args.contactId)
    }

    override val storeHolder: StoreHolder<ContactDetailsEvent, ContactDetailsEffect, ContactDetailsState> by lazy {
        storeFactory()
    }

    private fun storeFactory(): StoreHolder<ContactDetailsEvent, ContactDetailsEffect, ContactDetailsState> {
        val zulipApi = (requireContext().applicationContext as TFSApp).zulipApi
        return LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = ContactDetailsState(),
                reducer = ContactDetailsReducer(),
                actor = ContactDetailsActor(GetContactByIdUseCase(zulipApi))
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        updateToolbar(
            ToolbarState.NavUpWithTitle(
                R.color.grey_secondary_background,
                getString(R.string.profile),
                TitleGravity.CENTER
            )
        )
        return binding.root
    }

    override fun render(state: ContactDetailsState) {
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