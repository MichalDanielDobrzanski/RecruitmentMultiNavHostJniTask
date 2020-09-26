package com.hr.test.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.test.R
import com.hr.test.main.detail.actors.ActorsAdapter
import com.hr.test.utils.inflateNoAttach
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_movie_fragment.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var actorsAdapter: ActorsAdapter

    private val fragmentArgs: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflateNoAttach(R.layout.detail_movie_fragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieName.text = fragmentArgs.movieName
        actorsAdapter = ActorsAdapter(requireActivity())
        actorsRecyclerView.adapter = actorsAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.observeMoviesDetails(fragmentArgs.movieName).observe(viewLifecycleOwner, Observer {
            movieDescriptionTextView.text = it.description
            scoreRatingBar.rating = it.score
            actorsAdapter.update(it.actors)
        })
    }
}