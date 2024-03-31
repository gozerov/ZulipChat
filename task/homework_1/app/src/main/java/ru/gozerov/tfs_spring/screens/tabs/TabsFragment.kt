package ru.gozerov.tfs_spring.screens.tabs

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.navigationHolder
import ru.gozerov.tfs_spring.app.router
import ru.gozerov.tfs_spring.databinding.FragmentTabsBinding
import ru.gozerov.tfs_spring.navigation.Screens
import ru.gozerov.tfs_spring.screens.chat.ChatFragment
import ru.gozerov.tfs_spring.screens.contacts.ContactsFragment
import ru.gozerov.tfs_spring.screens.profile.ProfileFragment

class TabsFragment : Fragment() {

    private lateinit var binding: FragmentTabsBinding

    private var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = AppNavigator(
            context as FragmentActivity,
            R.id.localFragmentContainer,
            parentFragmentManager
        )
        navigationHolder.setNavigator(navigator!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.nav_channels
            router.replaceScreen(Screens.Channels())
        }
        parentFragmentManager.addOnBackStackChangedListener {
            when (parentFragmentManager.fragments.lastOrNull()) {
                is ChatFragment -> {
                    if (binding.bottomNavView.selectedItemId != R.id.nav_channels)
                        binding.bottomNavView.selectedItemId = R.id.nav_channels
                }

                is ContactsFragment -> {
                    if (binding.bottomNavView.selectedItemId != R.id.nav_contacts)
                        binding.bottomNavView.selectedItemId = R.id.nav_contacts
                }

                is ProfileFragment -> {
                    if (binding.bottomNavView.selectedItemId != R.id.nav_profile)
                        binding.bottomNavView.selectedItemId = R.id.nav_profile
                }
            }
        }
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_channels -> {
                    if (parentFragmentManager.fragments.lastOrNull() !is ChatFragment)
                        router.navigateTo(Screens.Channels())
                    return@setOnItemSelectedListener true
                }

                R.id.nav_contacts -> {
                    val fragment = parentFragmentManager.findFragmentByTag("2")
                    if (fragment == null) {
                        if (parentFragmentManager.findFragmentById(R.id.localFragmentContainer) !is ContactsFragment) {
                            router.navigateTo(Screens.Contacts())
                        }
                    } else {
                        parentFragmentManager.beginTransaction().remove(fragment).commit()
                        parentFragmentManager.beginTransaction().replace(R.id.localFragmentContainer, fragment).addToBackStack("2").commit()
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.nav_profile -> {
                    Log.e("ETTR", "ALE3")
                    val fragment = parentFragmentManager.findFragmentByTag("3")
                    if (fragment == null) {
                        if (parentFragmentManager.findFragmentById(R.id.localFragmentContainer) !is ProfileFragment)
                            router.navigateTo(Screens.Profile())
                    } else {
                        parentFragmentManager.beginTransaction().remove(fragment).commitNow()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.localFragmentContainer, fragment).addToBackStack("3").commit()
                    }
                    return@setOnItemSelectedListener true
                }

                else -> error("Unexpected menu item")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

}