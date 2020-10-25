#include <jni.h>
#include <string>

#include <MovieController.hpp>
#include <MovieHolder.hpp>
#include <MovieCallback.hpp>
#include <Movie.hpp>

static const char *STRING_JVM_SIG = "java/lang/String";

extern "C"
JNIEXPORT jobject JNICALL
Java_com_hr_core_repository_MoviesNativeRepository_createMovieHolder(
        JNIEnv *env,
        jobject thiz
) {
    auto holder = new movies::MovieHolder(env, thiz);
    return env->NewDirectByteBuffer((void *) holder, sizeof(movies::MovieHolder));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hr_core_repository_MoviesNativeRepository_fetchMoviesJni(
        JNIEnv *env,
        jobject thiz,
        jobject ptr
) {
    using namespace movies;
    using namespace std;
    auto holder = (MovieHolder *) env->GetDirectBufferAddress(ptr);

    std::function<void(std::vector<Movie*>)> callback = [env, holder](const std::vector<movies::Movie*>& moviesVector) {
        vector<string> marshalled;
        auto it = moviesVector.begin();
        while (it != moviesVector.end()) {
            marshalled.push_back((*it)->name);
            marshalled.push_back(to_string((*it)->lastUpdated));
            it++;
        }

        auto jObjectArrayMovies = (jobjectArray) env->NewObjectArray(marshalled.size(), env->FindClass(STRING_JVM_SIG), nullptr);
        for (int i = 0; i < marshalled.size(); i++) {
            jstring jvmString = env->NewStringUTF(marshalled.at(i).data());
            env->SetObjectArrayElement(jObjectArrayMovies, i, jvmString);
        }
        env->CallVoidMethod(holder->instance, holder->onMoviesFetchedMethod, jObjectArrayMovies);
    };

    holder->movieController.getMovies(callback);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hr_core_repository_MoviesNativeRepository_fetchMovieDetailsJni(
        JNIEnv *env,
        jobject thiz,
        jobject ptr,
        jobject jStringMovieName // jstring type crashes debugger
) {
    using namespace movies;
    using namespace std;
    auto *holder = (MovieHolder *) env->GetDirectBufferAddress(ptr);
    auto *movieName = env->GetStringUTFChars((jstring)jStringMovieName, nullptr);
    MovieDetail *details = holder->movieController.getMovieDetail(string(movieName));

    vector<Actor> actorsVector = details->actors;
    vector<string> marshalled;
    auto it = actorsVector.begin();
    while (it != actorsVector.end()) {
        marshalled.push_back((*it).name);
        marshalled.push_back(to_string((*it).age));
        marshalled.push_back((*it).imageUrl);
        it++;
    }

    auto jObjectArrayActors = (jobjectArray) env->NewObjectArray(marshalled.size(), env->FindClass(STRING_JVM_SIG), nullptr);
    for (int i = 0; i < marshalled.size(); i++) {
        jstring jvmString = env->NewStringUTF(marshalled.at(i).data());
        env->SetObjectArrayElement(jObjectArrayActors, i, jvmString);
    }

    env->CallVoidMethod(
            holder->instance,
            holder->onMovieDetailsFetchedMethod,
            env->NewStringUTF(details->name.c_str()),
            (jfloat) details->score,
            env->NewStringUTF(details->description.c_str()),
            jObjectArrayActors
    );
    env->ReleaseStringUTFChars((jstring)jStringMovieName, movieName);
}
