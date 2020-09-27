package com.hr.test.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.core.viewmodel.MoviesViewState
import com.hr.test.R
import com.hr.test.main.navigation.MainTabManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_content_view.*
import kotlinx.android.synthetic.main.main_error_view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val tabManager: MainTabManager by lazy { MainTabManager(this) }

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            tabManager.useStartingController()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.fetch()
        viewModel.observeMovies().observe(this, Observer {
            renderState(it)
        })
    }

    private fun renderState(viewState: MoviesViewState) = when (viewState) {
        MoviesViewState.Loading -> {
            renderLoading()
        }
        is MoviesViewState.Content -> {
            renderContent()
        }
        MoviesViewState.Error -> {
            renderError()
        }
    }

    private fun renderLoading() {
        contentViewAnimator.displayedChild = LOADING_INDEX
    }

    private fun renderContent() {
        contentViewAnimator.displayedChild = CONTENT_INDEX
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    private fun renderError() {
        contentViewAnimator.displayedChild = ERROR_INDEX
        retryButton.setOnClickListener {
            renderLoading()
            viewModel.fetch()
        }
    }

    override fun onBackPressed() {
        handleBack()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        tabManager.switchTab(menuItem.itemId)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                handleBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleBack() {
        if (!tabManager.onBackPressed()) {
            finish()
        }
    }
}

private const val LOADING_INDEX = 0
private const val CONTENT_INDEX = 1
private const val ERROR_INDEX = 2