package ru.gozerov.tfs_spring.presentation.screens.channels.list

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import ru.gozerov.tfs_spring.databinding.FragmentChannelListBinding
import ru.gozerov.tfs_spring.di.application.appComponent
import ru.gozerov.tfs_spring.di.features.channels.list.DaggerChannelListComponent
import ru.gozerov.tfs_spring.presentation.activity.ToolbarState
import ru.gozerov.tfs_spring.presentation.activity.searchFieldFlow
import ru.gozerov.tfs_spring.presentation.activity.updateToolbar
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.ChannelPagerAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.ChannelsAdapter
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegate
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class ChannelListFragment : ElmFragment<ChannelListEvent, ChannelListEffect, ChannelListState>() {

    private lateinit var binding: FragmentChannelListBinding

    private val subscribedChannelsAdapter = ChannelsAdapter()
    private val allChannelsAdapter = ChannelsAdapter()

    private val channelPagerAdapter =
        ChannelPagerAdapter(listOf(subscribedChannelsAdapter, allChannelsAdapter))

    override val initEvent: ChannelListEvent = ChannelListEvent.UI.LoadChannels

    override val storeHolder: StoreHolder<ChannelListEvent, ChannelListEffect, ChannelListState> by lazy {
        storeFactory
    }

    @Inject
    lateinit var storeFactory: StoreHolder<ChannelListEvent, ChannelListEffect, ChannelListState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = DaggerChannelListComponent.factory()
            .create(lifecycle, findNavController(), context.appComponent)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelListBinding.inflate(inflater, container, false)
        val channelDelegate = ChannelDelegate { channel ->
            val position = binding.channelsViewPager.computeScroll()
            Log.e("AAA", position.toString())
            store.accept(
                ChannelListEvent.UI.ExpandItems(
                    channel,
                    binding.channelsViewPager.scrollState,
                    binding.categoryTabs.selectedTabPosition
                )
            )
        }
        val topicDelegate = TopicDelegate {
            store.accept(
                ChannelListEvent.UI.LoadChannel(it, binding.categoryTabs.selectedTabPosition)
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchFieldFlow.collect { query ->
                        store.accept(ChannelListEvent.UI.Search(query))
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
            if (!state.isNavigating && state.query.isEmpty())
                updateToolbar(ToolbarState.Search(getString(R.string.search_and)))

        }
    }

    override fun handleEffect(effect: ChannelListEffect) = when (effect) {

        is ChannelListEffect.ShowError -> {
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }

        is ChannelListEffect.LoadedChannel -> {

        }

        is ChannelListEffect.UpdateToolbar -> {
            updateToolbar(ToolbarState.SearchWithText(effect.text))
            store.accept(ChannelListEvent.UI.EnableSearch)
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