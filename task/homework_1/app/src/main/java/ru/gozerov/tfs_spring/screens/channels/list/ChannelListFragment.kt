package ru.gozerov.tfs_spring.screens.channels.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.activity.ToolbarState
import ru.gozerov.tfs_spring.activity.searchFieldFlow
import ru.gozerov.tfs_spring.activity.updateToolbar
import ru.gozerov.tfs_spring.app.TFSApp
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
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChannelListViewModel((requireContext().applicationContext as TFSApp).zulipApi) as T
            }
        })[ChannelListViewModel::class.java]
    }

    private val subscribedChannelsAdapter = ChannelsAdapter()
    private val allChannelsAdapter = ChannelsAdapter()

    private val channelPagerAdapter =
        ChannelPagerAdapter(listOf(subscribedChannelsAdapter, allChannelsAdapter))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelListBinding.inflate(inflater, container, false)
        updateToolbar(ToolbarState.Search(getString(R.string.search_and)))
        val channelDelegate = ChannelDelegate {
            viewModel.handleIntent(
                ChannelListIntent.ExpandItems(
                    it,
                    binding.categoryTabs.selectedTabPosition
                )
            )
        }
        val topicDelegate = TopicDelegate {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val channelName =
                        viewModel.getChannelById(it, binding.categoryTabs.selectedTabPosition)
                    val action =
                        ChannelListFragmentDirections.actionNavChannelsToChatFragment(
                            it.title,
                            channelName
                        )
                    findNavController().navigate(action)
                }
            }
        }
        subscribedChannelsAdapter.addDelegate(channelDelegate)
        subscribedChannelsAdapter.addDelegate(topicDelegate)
        allChannelsAdapter.addDelegate(channelDelegate)
        allChannelsAdapter.addDelegate(topicDelegate)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.channelsViewPager.post {
            binding.channelsViewPager.requestFocus()
        }
        binding.channelsViewPager.adapter = channelPagerAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    searchFieldFlow.collect {
                        viewModel.handleIntent(
                            ChannelListIntent.Search(
                                it,
                                binding.categoryTabs.selectedTabPosition
                            )
                        )
                    }
                }
                launch {
                    viewModel.viewState.collect { state ->
                        when (state) {
                            is ChannelListViewState.Empty -> {
                                viewModel.handleIntent(ChannelListIntent.LoadChannels)
                            }

                            is ChannelListViewState.LoadedChannels -> {
                                binding.shimmerLayout.stopShimmer()
                                binding.shimmerLayout.visibility = View.GONE
                                binding.channelsViewPager.visibility = View.VISIBLE
                                configureTabsMediator(state.channels.keys.toList())
                                channelPagerAdapter.data = state.channels.values.toList()
                            }

                            is ChannelListViewState.Error -> {
                                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                            }
                        }
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

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
    }

}