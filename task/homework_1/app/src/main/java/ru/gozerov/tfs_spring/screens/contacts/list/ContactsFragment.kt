package ru.gozerov.tfs_spring.screens.contacts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentContactsBinding
import ru.gozerov.tfs_spring.screens.contacts.list.adapter.ContactsAdapter
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsIntent
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsViewState
import ru.gozerov.tfs_spring.utils.VerticalMarginItemDecoration

class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ContactsViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ContactsViewModel::class.java]
    }

    private val contactsAdapter = ContactsAdapter {
        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailsFragment(it)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        viewModel.handleIntent(ContactsIntent.LoadContacts)
        updateToolbar(ToolbarState.Search(getString(R.string.users_and)))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.contactsList.adapter = contactsAdapter
        binding.contactsList.addItemDecoration(
            VerticalMarginItemDecoration(
                R.dimen._8dp,
                R.dimen._16dp
            )
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                when (state) {
                    is ContactsViewState.Empty -> {}
                    is ContactsViewState.ContactsList -> {
                        contactsAdapter.data = state.contacts
                    }

                    is ContactsViewState.Error -> {}
                }
            }
        }
    }

}