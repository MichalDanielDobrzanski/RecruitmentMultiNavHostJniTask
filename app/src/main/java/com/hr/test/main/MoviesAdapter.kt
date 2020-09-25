package com.hr.test.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.test.R
import kotlinx.android.synthetic.main.movie_item_view.view.*

class MoviesAdapter(
    private val onLikeItem: (String, Boolean) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var movies = mutableListOf<ContentMovieViewStateData>()

    fun setMovies(movies: List<ContentMovieViewStateData>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view, parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var movieName: String
        private var likedMovieState: Boolean = false

        fun bind(movie: ContentMovieViewStateData) {
            likedMovieState = movie.liked
            movieName = movie.movie.name.value
            itemView.apply {
                titleTextView.text = movieName
                likeButton.setBackgroundResource(if (likedMovieState) R.drawable.ic_like_icon else R.drawable.ic_not_liked_icon)
                likeButton.setOnClickListener {
                    likedMovieState = !likedMovieState
                    val newItemState = movie.copy(liked = likedMovieState)
                    movies.indexOf(movie).takeIf { it != -1 }?.let { idx ->
                        movies.removeAt(idx)
                        movies.add(idx, newItemState)
                        notifyItemChanged(idx)
                    }
                    onLikeItem(movieName, likedMovieState)
                }
            }
        }
    }
}