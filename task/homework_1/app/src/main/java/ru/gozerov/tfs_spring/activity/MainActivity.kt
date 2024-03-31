package ru.gozerov.tfs_spring.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.app.navigationHolder
import ru.gozerov.tfs_spring.app.router
import ru.gozerov.tfs_spring.databinding.ActivityMainBinding
import ru.gozerov.tfs_spring.navigation.Screens

class MainActivity : AppCompatActivity(), ToolbarHolder {

    private val navigator = AppNavigator(this, R.id.globalFragmentContainer)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = getColor(R.color.grey_secondary_background)
        window.navigationBarColor = getColor(R.color.black)

        setContentView(binding.root)


        if (savedInstanceState == null) {
            router.newRootScreen(Screens.Tabs())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
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
            is ToolbarState.NavUpWithTitle -> {}
            is ToolbarState.Search -> {}
        }
    }

}