package ru.gozerov.tfs_spring.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ToolbarHolder {

    private lateinit var binding: ActivityMainBinding

    private val _searchFieldFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    override val searchFieldFlow: StateFlow<String>
        get() = _searchFieldFlow
            .debounce(200)
            .stateIn(lifecycleScope, SharingStarted.Lazily, "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = getColor(R.color.grey_secondary_background)
        window.navigationBarColor = getColor(R.color.black)

        binding.searchEditText.addTextChangedListener { text: Editable? ->
            text?.let { _searchFieldFlow.tryEmit(text.toString()) }
        }

    }

    @SuppressLint("RestrictedApi")
    override fun updateToolbar(toolbarState: ToolbarState) {
        when (toolbarState) {
            is ToolbarState.None -> {
                binding.toolbar.visibility = View.GONE
            }

            is ToolbarState.OnlyStatusColor -> {
                binding.toolbar.visibility = View.GONE
                window.statusBarColor = getColor(toolbarState.color)
            }

            is ToolbarState.NavUpWithTitle -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbar.setBackgroundColor(getColor(toolbarState.backgroundColor))
                window.statusBarColor = getColor(toolbarState.backgroundColor)
                binding.startActionButton.visibility = View.VISIBLE
                binding.startActionButton.setImageResource(R.drawable.ic_arrow_back_24)
                binding.endActionButton.visibility = View.GONE
                binding.searchField.visibility = View.GONE
                binding.startActionButton.setOnClickListener {
                    findNavController(R.id.localFragmentContainer).popBackStack()
                }
                when (toolbarState.gravity) {
                    TitleGravity.START -> {
                        binding.startToolbarTitle.visibility = View.VISIBLE
                        binding.startToolbarTitle.text = toolbarState.title
                        binding.centerToolbarTitle.visibility = View.GONE
                    }

                    TitleGravity.CENTER -> {
                        binding.centerToolbarTitle.visibility = View.VISIBLE
                        binding.centerToolbarTitle.text = toolbarState.title
                        binding.startToolbarTitle.visibility = View.GONE
                    }
                }
            }

            is ToolbarState.Search -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbar.setBackgroundColor(getColor(R.color.grey_secondary_background))
                binding.startActionButton.visibility = View.GONE
                binding.endActionButton.setImageResource(R.drawable.ic_search_24)
                binding.endActionButton.visibility = View.VISIBLE
                binding.startToolbarTitle.visibility = View.GONE
                binding.searchField.visibility = View.VISIBLE
                binding.centerToolbarTitle.visibility = View.GONE
                window.statusBarColor = getColor(R.color.grey_secondary_background)
                binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
                    hideKeyboard(binding.root)
                    return@setOnEditorActionListener true
                }
                binding.searchField.post {
                    binding.searchEditText.hint = toolbarState.title
                    binding.searchEditText.editableText.clear()
                    if (!toolbarState.isFocused) {
                        binding.searchField.clearFocus()
                        binding.globalFragmentContainer.requestFocus()
                    }
                }
                binding.endActionButton.setOnClickListener {
                    hideKeyboard(binding.root)
                    binding.globalFragmentContainer.requestFocus()
                }
            }

            is ToolbarState.SearchWithText -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbar.setBackgroundColor(getColor(R.color.grey_secondary_background))
                binding.startActionButton.visibility = View.GONE
                binding.endActionButton.setImageResource(R.drawable.ic_search_24)
                binding.endActionButton.visibility = View.VISIBLE
                binding.startToolbarTitle.visibility = View.GONE
                binding.searchField.visibility = View.VISIBLE
                binding.centerToolbarTitle.visibility = View.GONE
                window.statusBarColor = getColor(R.color.grey_secondary_background)
                binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
                    hideKeyboard(binding.root)
                    return@setOnEditorActionListener true
                }
                binding.searchField.post {
                    binding.searchEditText.setText(toolbarState.text)
                    binding.searchEditText.setSelection(toolbarState.text.length)
                }
                binding.endActionButton.setOnClickListener {
                    hideKeyboard(binding.root)
                    binding.globalFragmentContainer.requestFocus()
                }
            }
        }
    }

}