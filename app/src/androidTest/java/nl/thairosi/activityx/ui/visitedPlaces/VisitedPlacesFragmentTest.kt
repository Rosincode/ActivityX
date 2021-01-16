package nl.thairosi.activityx.ui.visitedPlaces

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.test.runBlockingTest
import nl.thairosi.activityx.R
import nl.thairosi.activityx.ServiceLocator
import nl.thairosi.activityx.adapters.VisitedPlacesAdapter
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.models.PlaceApiModel.Location
import nl.thairosi.activityx.repository.FakeTestPlaceRepository
import nl.thairosi.activityx.repository.Repository
import nl.thairosi.activityx.ui.MainActivity
import nl.thairosi.activityx.utils.Utils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VisitedPlacesFragmentTest {

    private lateinit var repository: Repository

    @Before
    fun setup() = runBlockingTest {
        repository = FakeTestPlaceRepository()
        ServiceLocator.Repository = repository

        // GIVEN - Add place to DB
        val apiLocation = Location(37.4220996, -122.0819686)
        val location = Utils.locationAdapter(apiLocation)
        val date = Utils.getDateTime()
        val place = Place("ChIJo4za6vi5j4ARMua093RgGGA", "ATtYBwLAMkBQQXb3uxnF4CyoST--LJa586mVvC-KTBJE5Gk2ZFBDMrguZY4o-_d2LVMloImFNF76zWmEiD2dohRMhuyj_FSSKXAtGaXscbfM64hmWXw-djxF4BIVVlMhzEXBjeLzCaEFca8GbGg_Sip0dYYfUCZRUySiM1Pnvlvxtk9atvfQ", "Charleston Park", "1500 Charleston Rd, Mountain View, CA 94043, USA", "park, point of interest, establishment", "https://maps.google.com/?cid=6924390482570438194", location, date, true, true )
        repository.updateOrInsert(place)
    }

    @After
    fun cleanupDb() {
        ServiceLocator.resetRepository()
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun visitedPlace_is_Unblocked_From_RV_And_Saved_To_Database() {

        onView(withId(R.id.mainDrawerLayout)).perform(DrawerActions.open())

        onView(withText("Visited activities"))
            .check(matches(isDisplayed()))
        onView(withText("Visited activities"))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.switch_block_visited_place))
            .check(matches(isChecked()))

        onView(withId(R.id.switch_block_visited_place))
            .perform(click())

        Thread.sleep(500)

        onView(withContentDescription("Navigate up"))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.mainDrawerLayout)).perform(DrawerActions.open())

        onView(withText("Visited activities"))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.switch_block_visited_place))
            .check(matches(isNotChecked()))

        Thread.sleep(500)
    }

    @Test
    fun visitedPlace_is_Unblocked_From_Place_Fragment_And_Saved_To_Database() {

        onView(withId(R.id.mainDrawerLayout)).perform(DrawerActions.open())

        onView(withText("Visited activities"))
            .perform(click())

        onView(withId(R.id.switch_block_visited_place))
            .check(matches(isChecked()))

        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<VisitedPlacesAdapter.PlaceViewHolder>(0, click()))

        Thread.sleep(1000)

        onView(withId(R.id.placeBlockActivityCheckbox))
            .check(matches(isChecked()))

        Thread.sleep(1000)

        onView(withId(R.id.placeBlockActivityCheckbox))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.placeReturnButton))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.switch_block_visited_place))
            .check(matches(isNotChecked()))

        Thread.sleep(500)
    }

}