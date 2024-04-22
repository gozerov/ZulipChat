package ru.gozerov.tfs_spring.presentation.screens.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.FragmentTabsBinding

class TabsFragment : Fragment() {

    private lateinit var binding: FragmentTabsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomNavView.setupWithNavController((childFragmentManager.findFragmentById(R.id.localFragmentContainer) as NavHostFragment).navController)
    }

}