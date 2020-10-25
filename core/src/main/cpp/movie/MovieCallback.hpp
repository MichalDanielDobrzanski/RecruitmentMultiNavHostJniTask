//
// Created by Michał Dobrzański on 28/09/2020.
//

#ifndef HR_TEST_MOVIECALLBACK_HPP
#define HR_TEST_MOVIECALLBACK_HPP

#include <vector>
#include <Movie.hpp>

namespace movies {

    class MovieCallback {

    public:
        virtual void onMoviesFetched(std::vector<Movie *>) = 0;

    };

}

#endif //HR_TEST_MOVIECALLBACK_HPP
