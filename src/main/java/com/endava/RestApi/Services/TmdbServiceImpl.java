package com.endava.RestApi.Services;

import com.endava.RestApi.Domain.Actor;
import com.endava.RestApi.Domain.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TmdbServiceImpl implements TmdbService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${imdb.api.url}")
    private String TmdbUrl;
    @Value("${imdb.api.key}")
    private String TmdbKey;

    @Override
    public List<Actor> getCastFromMovieId(int movieId){
        try {
            JsonNode castPage = restTemplate.getForObject(TmdbUrl + "/movie/" + movieId + "/credits" + TmdbKey, JsonNode.class);
            try {
                return objectMapper.readValue(castPage.get("cast").toString(), new TypeReference<List<Actor>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public List<Movie> getSpecificNumberOfMovies(int numberOfMovies) throws IOException {
        int page = 0;
        List<Movie> movies = new ArrayList<>();
        if(numberOfMovies<1)
            return movies;
        do{
            page++;
            JsonNode castPage = restTemplate.
                    getForObject(TmdbUrl + "/movie/popular" + TmdbKey+"&page="+page, JsonNode.class);
            List<Object> toAdd =objectMapper.
                    readValue(castPage.get("results").toString(), new TypeReference<ArrayList<Movie>>() {});
            movies.addAll((List)toAdd.stream().limit(numberOfMovies-movies.size()).collect(Collectors.toList()));
        }while(movies.size()<numberOfMovies);
        return movies;
    }

    @Override
    public List<Actor> getAllActorsFromMoviesByIdList(List<Integer> ids) throws IOException {
        return ids.stream().map(this::getCastFromMovieId).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

}
