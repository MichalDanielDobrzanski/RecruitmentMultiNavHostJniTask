# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.4.1)
add_library(movies-lib
        SHARED
        MovieGlue.cpp
        movie/MovieController.hpp
        movie/MovieHolder.hpp)

find_library(log-lib
        log)

target_link_libraries(movies-lib
        ${log-lib})

# add this so #include can use "<>"
target_include_directories(
        movies-lib
        PRIVATE
        movie)