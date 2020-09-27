#ifndef HR_TEST_MOVIEHOLDER_HPP
#define HR_TEST_MOVIEHOLDER_HPP

#include <jni.h>
#include <MovieController.hpp>

static const char *ON_MOVIES_FETCHED_METHOD = "onMoviesFetched";
static const char *ON_MOVIES_FETCHED_METHOD_SIG = "([Ljava/lang/String;)V";

static const char *ON_MOVIE_DETAILS_FETCHED_METHOD = "onMovieDetailsFetched";
static const char *ON_MOVIE_DETAILS_FETCHED_METHOD_SIG = "(Ljava/lang/String;FLjava/lang/String;[Ljava/lang/String;)V";

namespace movies {
    class MovieHolder {

    private:
        const jclass clz;

    public:
        const jobject instance;
        MovieController movieController;
        const jmethodID onMoviesFetchedMethod;
        const jmethodID onMovieDetailsFetchedMethod;

        MovieHolder(JNIEnv *env, const jobject jvm_instance) :
                instance(env->NewGlobalRef(jvm_instance)),
                clz((jclass) env->NewGlobalRef(env->GetObjectClass(jvm_instance))),
                movieController(MovieController()),
                onMoviesFetchedMethod(env->GetMethodID(clz, ON_MOVIES_FETCHED_METHOD, ON_MOVIES_FETCHED_METHOD_SIG)),
                onMovieDetailsFetchedMethod(env->GetMethodID(clz, ON_MOVIE_DETAILS_FETCHED_METHOD, ON_MOVIE_DETAILS_FETCHED_METHOD_SIG)) {
        };
    };
}

#endif //HR_TEST_MOVIEHOLDER_HPP
