package ru.gozerov.tfs_spring.screens.channels.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.databinding.FragmentChannelListBinding
import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelPagerAdapter
import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelsAdapter
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegate
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegate
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListIntent
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListViewState

class ChannelListFragment : Fragment() {

    private lateinit var binding: FragmentChannelListBinding

    private val viewModel: ChannelListViewModel by lazy {
        ViewModelProvider(this)[ChannelListViewModel::class.java]
    }

    private val channelsAdapter = ChannelsAdapter()

    private val channelPagerAdapter = ChannelPagerAdapter(channelsAdapter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelListBinding.inflate(inflater, container, false)
        updateToolbar(ToolbarState.Search(getString(R.string.search_and)))
        channelsAdapter.addDelegate(
            ChannelDelegate { viewModel.handleIntent(ChannelListIntent.ExpandItems(it, binding.categoryTabs.selectedTabPosition)) })
        channelsAdapter.addDelegate(
            TopicDelegate {
                val channelName = viewModel.getChannelById(it)
                val action =
                ChannelListFragmentDirections.actionNavChannelsToChatFragment(id, channelName)
                findNavController().navigate(action) }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.channelsViewPager.adapter = channelPagerAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.handleIntent(ChannelListIntent.LoadChannels)
                viewModel.viewState.collect { state ->
                    when (state) {
                        is ChannelListViewState.Empty -> {}
                        is ChannelListViewState.LoadedChannels -> {
                            configureTabsMediator(state.channels.keys.toList())
                            channelPagerAdapter.data = state.channels.values.toList()
                        }

                        is ChannelListViewState.Error -> {}
                    }
                }
            }
        }
    }

    private fun configureTabsMediator(categories: List<String>) {
        TabLayoutMediator(binding.categoryTabs, binding.channelsViewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
    }

}