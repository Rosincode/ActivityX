package nl.thairosi.activityx.Robots

import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.thairosi.activityx.R
import nl.thairosi.activityx.ui.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * This test class is a small demo for future Robot tests in the application
 */
class HomeRobot : RobotTemplate() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    fun clickOnGo() = clickButton(R.id.home_go_button)
    fun matchGoButtonText() = matchText(R.id.home_go_button, "GO!")

    @Test
    fun navigationFragmentOpened() {
        clickOnGo()
    }

    @Test
    fun checkGoButtonText() {
        matchGoButtonText()
    }

}

