package com.hr.test.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hr.core.viewmodel.MovieDetailViewState
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.test.R
import com.hr.test.main.detail.actors.ActorsAdapter
import com.hr.test.utils.inflateNoAttach
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_movie_fragment.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var likedMovie: Boolean = false

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
        ViewModelProvider(this).get(MoviesViewModel::class.java).apply {
            observeMoviesDetails(fragmentArgs.movieName)
                .observe(viewLifecycleOwner, Observer {
                    val detail = it.movieDetail
                    movieDescriptionTextView.text = detail.description
                    scoreRatingBar.rating = detail.score
                    likedMovie = it.liked
                    likeFloatingActionButton.setImageResource(getIconForLikeState())
                    likeFloatingActionButton.setOnClickListener {
                        likedMovie = !likedMovie
                        likeFloatingActionButton.setImageResource(getIconForLikeState())
                        updateLike(detail.movieName, likedMovie)
                    }
                    actorsAdapter.update(detail.actors)
                })
        }
    }

    @DrawableRes
    private fun getIconForLikeState(): Int = if (likedMovie) R.drawable.ic_like_icon else R.drawable.ic_not_liked_icon
}