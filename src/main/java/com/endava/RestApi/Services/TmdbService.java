package com.endava.RestApi.Services;

import com.endava.RestApi.Domain.Actor;
import com.endava.RestApi.Domain.Movie;

import java.io.IOException;
import java.util.List;

public interface TmdbService {
    List getCastFromMovieId(int movieId) throws IOException;
    List<Movie> getSpecificNumberOfMovies(int numberOfMovies) throws IOException;
    List<Actor> getAllActorsFromMoviesByIdList(List<Integer> ids) throws IOException;
}
