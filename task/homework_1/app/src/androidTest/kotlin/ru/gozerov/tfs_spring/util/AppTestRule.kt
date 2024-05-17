package ru.gozerov.tfs_spring.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AppTestRule<T : Fragment>(
    private val configuration: Application.() -> Unit
) : TestRule {

    val wiremockRule = WireMockRule() // default: localhost:8080
    private val intentsRule = IntentsRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(intentsRule)
            .around(wiremockRule)
            .apply { configuration(ApplicationProvider.getApplicationContext()) }
            .apply(base, description)
    }
}
