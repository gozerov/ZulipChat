package ru.gozerov.tfs_spring.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ToolbarHolder {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = getColor(R.color.grey_secondary_background)
        window.navigationBarColor = getColor(R.color.black)

        setContentView(binding.root)

    }

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
                binding.startActionButton.setOnClickListener {
                    findNavController(R.id.localFragmentContainer).popBackStack()
                }
                when(toolbarState.gravity) {
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
                binding.startToolbarTitle.visibility = View.VISIBLE
                binding.startToolbarTitle.text = toolbarState.title
                binding.centerToolbarTitle.visibility = View.GONE
                window.statusBarColor = getColor(R.color.grey_secondary_background)
            }
        }
    }

}