package ru.gozerov.tfs_spring.presentation.screens.contacts.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.core.utils.VerticalMarginItemDecoration
import ru.gozerov.tfs_spring.databinding.FragmentContactsBinding
import ru.gozerov.tfs_spring.di.application.appComponent
import ru.gozerov.tfs_spring.di.features.contacts.list.DaggerContactListComponent
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.searchFieldFlow
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.adapter.ContactsAdapter
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class ContactsFragment : ElmFragment<ContactListEvent, ContactListEffect, ContactListState>() {

    private lateinit var binding: FragmentContactsBinding

    private val contactsAdapter = ContactsAdapter {
        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailsFragment(it)
        findNavController().navigate(action)
    }
    override val initEvent: ContactListEvent = ContactListEvent.UI.Init

    override val storeHolder: StoreHolder<ContactListEvent, ContactListEffect, ContactListState> by lazy {
        storeFactory
    }

    @Inject
    lateinit var storeFactory: StoreHolder<ContactListEvent, ContactListEffect, ContactListState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = DaggerContactListComponent.factory().create(lifecycle, context.appComponent)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        updateToolbar(ToolbarState.Search(getString(R.string.users_and)))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.contactsList.adapter = contactsAdapter
        binding.contactsList.addItemDecoration(
            VerticalMarginItemDecoration(R.dimen._8dp, R.dimen._16dp)
        )
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchFieldFlow.collect {
                        storeHolder.store.accept(
                            ContactListEvent.UI.Search(it)
                        )
                    }
                }
            }
        }
    }

    override fun render(state: ContactListState) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                state.contacts?.collect { users ->
                    contactsAdapter.data = users
                }
            }
        }
    }

}