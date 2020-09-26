package com.hr.test.main.navigation

import android.view.View
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.hr.test.R
import com.hr.test.main.MainActivity
import kotlinx.android.synthetic.main.main_content_view.*

class MainTabManager(private val mainActivity: MainActivity) {

    private val menuItemToNavigationFragmentMap = mapOf(
        STARTING_DESTINATION to R.id.listFragment,
        R.id.navigation_grid_menu_item to R.id.gridFragment,
    )

    private val navListController: NavController by lazy {
        mainActivity.findNavController(R.id.listTab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_main).apply {
                startDestination = menuItemToNavigationFragmentMap.getValue(STARTING_DESTINATION)
            }
        }
    }
    private val navGridController: NavController by lazy {
        mainActivity.findNavController(R.id.gridTab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_main).apply {
                startDestination = menuItemToNavigationFragmentMap.getValue(R.id.navigation_grid_menu_item)
            }
        }
    }

    private val listTabContainer: View by lazy { mainActivity.listTabContainer }

    private val gridTabContainer: View by lazy { mainActivity.gridTabContainer }

    private var currentTabId: Int = STARTING_DESTINATION

    private var currentController: NavController? = null

    fun onBackPressed(): Boolean = currentController?.popBackStack() ?: false

    fun switchTab(tabId: Int) {
        currentTabId = tabId
        when (tabId) {
            STARTING_DESTINATION -> {
                currentController = navListController
                invisibleTabContainerExcept(listTabContainer)
            }
            R.id.navigation_grid_menu_item -> {
                currentController = navGridController
                invisibleTabContainerExcept(gridTabContainer)
            }
            else -> throw IllegalArgumentException("Not handled id: $tabId")
        }
    }

    private fun invisibleTabContainerExcept(container: View) {
        listTabContainer.isInvisible = true
        gridTabContainer.isInvisible = true
        container.isInvisible = false
    }

    fun useStartingController() {
        currentController = navListController
    }

    companion object {
        private const val STARTING_DESTINATION: Int = R.id.navigation_list_menu_item
    }
}