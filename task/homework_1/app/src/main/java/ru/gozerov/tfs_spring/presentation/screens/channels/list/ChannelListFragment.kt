package ru.gozerov.tfs_spring.presentation.screens.channels.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.databinding.FragmentChannelListBinding
import ru.gozerov.tfs_spring.domain.use_cases.ExpandTopicsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelByIdUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SearchChannelsUseCase
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.searchFieldFlow
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.ChannelPagerAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.ChannelsAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.ChannelListActor
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.ChannelListReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ChannelListFragment : ElmFragment<ChannelListEvent, ChannelListEffect, ChannelListState>() {

    private lateinit var binding: FragmentChannelListBinding

    private val subscribedChannelsAdapter = ChannelsAdapter()
    private val allChannelsAdapter = ChannelsAdapter()

    private val channelPagerAdapter =
        ChannelPagerAdapter(listOf(subscribedChannelsAdapter, allChannelsAdapter))

    override val initEvent: ChannelListEvent = ChannelListEvent.UI.LoadChannels

    override val storeHolder: StoreHolder<ChannelListEvent, ChannelListEffect, ChannelListState> by lazy {
        storeFactory()
    }

    private fun storeFactory(): StoreHolder<ChannelListEvent, ChannelListEffect, ChannelListState> {
        val zulipApi = (requireContext().applicationContext as TFSApp).zulipApi
        return LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = ChannelListState(),
                reducer = ChannelListReducer(),
                actor = ChannelListActor(
                    GetChannelsUseCase(zulipApi),
                    ExpandTopicsUseCase,
                    SearchChannelsUseCase,
                    GetChannelByIdUseCase
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelListBinding.inflate(inflater, container, false)
        updateToolbar(ToolbarState.Search(getString(R.string.search_and)))
        val channelDelegate = ChannelDelegate {
            storeHolder.store.accept(
                ChannelListEvent.UI.ExpandItems(
                    it,
                    binding.categoryTabs.selectedTabPosition
                )
            )
        }
        val topicDelegate = TopicDelegate {
            storeHolder.store.accept(
                ChannelListEvent.UI.LoadChannel(
                    it,
                    binding.categoryTabs.selectedTabPosition
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchFieldFlow.collect {
                        storeHolder.store.accept(ChannelListEvent.UI.Search(it))
                    }
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
    }

    override fun render(state: ChannelListState) {
        state.channels?.run {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.channelsViewPager.visibility = View.VISIBLE
            configureTabsMediator(keys.toList())
            channelPagerAdapter.data = values.toList()
        }
    }

    override fun handleEffect(effect: ChannelListEffect) = when (effect) {
        is ChannelListEffect.ShowError -> {
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }

        is ChannelListEffect.LoadedChannel -> {
            val action =
                ChannelListFragmentDirections.actionNavChannelsToChatFragment(
                    effect.title,
                    effect.channelName
                )
            findNavController().navigate(action)
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