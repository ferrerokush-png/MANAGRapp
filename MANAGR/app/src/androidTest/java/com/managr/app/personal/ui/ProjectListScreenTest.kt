package com.managr.app.personal.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.managr.app.core.design.component.EmptyProjectsState
import com.managr.app.core.design.component.LoadingIndicator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProjectListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyProjectsState_displaysManagrBranding() {
        composeTestRule.setContent {
            EmptyProjectsState(onCreateClick = {})
        }
        composeTestRule.onNodeWithText("No Projects Yet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create your first music release project to get started with MANAGR").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Project").assertIsDisplayed()
    }

    @Test
    fun loadingIndicator_displaysLoadingMessage() {
        composeTestRule.setContent {
            LoadingIndicator(message = "Loading projects...")
        }
        composeTestRule.onNodeWithText("Loading projects...").assertIsDisplayed()
    }
}
