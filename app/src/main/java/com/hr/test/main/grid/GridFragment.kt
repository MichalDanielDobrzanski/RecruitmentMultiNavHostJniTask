package com.hr.test.main.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hr.test.R
import com.hr.test.main.MainMovieFragment
import com.hr.test.utils.inflateNoAttach
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_grid_fragment.*

@AndroidEntryPoint
class GridFragment : MainMovieFragment() {

    override fun itemLayoutId(): Int = R.layout.movie_grid_item_view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflateNoAttach(R.layout.main_grid_fragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentMovieListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = moviesAdapter

        }
    }
}