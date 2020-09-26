package com.hr.test.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.hr.test.R
import com.hr.test.utils.inflateNoAttach
import kotlinx.android.synthetic.main.detail_movie_fragment.*

class MovieDetailFragment : Fragment() {

    private val fragmentArgs: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflateNoAttach(R.layout.detail_movie_fragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieName.text = fragmentArgs.movieName
    }
}