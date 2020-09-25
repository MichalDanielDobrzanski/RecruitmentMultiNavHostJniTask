package com.hr.test.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hr.models.Movie
import com.hr.test.R
import kotlinx.android.synthetic.main.movie_item_view.view.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var movies = emptyList<Movie>()

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view, parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    data class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie) {
            itemView.titleTextView.text = movie.name.value
        }
    }
}