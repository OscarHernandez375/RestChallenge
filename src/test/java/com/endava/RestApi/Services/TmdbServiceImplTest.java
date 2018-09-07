package com.endava.RestApi.Services;

import com.endava.RestApi.Domain.Actor;
import com.endava.RestApi.Domain.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class TmdbServiceImplTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    private TmdbServiceImpl tmdbServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(tmdbServiceImpl,"objectMapper",new ObjectMapper());
    }

    @Test
    public void testGetCastFromMovieIdShouldReturnListOfActor() throws Exception {
        Mockito.when(restTemplate.getForObject(anyString(),eq(JsonNode.class))).thenReturn(getCastPageForTest());
        List result = tmdbServiceImpl.getCastFromMovieId(299536);
        Assert.assertTrue(result.size()>0);
        result.forEach(actor->Assert.assertEquals(Actor.class,actor.getClass()));
    }

    @Test
    public void testGetSpecificNumberOfMoviesShouldReturnSpecificSize() throws Exception {
        int numberOfMovies = 25;
        Mockito.when(restTemplate.getForObject(anyString(),any())).thenReturn(getMoviePageForTest());
        List<Movie> result = tmdbServiceImpl.getSpecificNumberOfMovies(numberOfMovies);
        Assert.assertEquals(numberOfMovies,result.size());
    }

    @Test
    public void testGetAllActorsFromMoviesByIdListShouldReturnDistinctActors() throws Exception {
        List<Integer> movieIds = Arrays.asList(299536,383498);
        Mockito.when(restTemplate.getForObject(anyString(),any())).thenReturn(getCastPageForTest());
        List<Actor> result = tmdbServiceImpl.getAllActorsFromMoviesByIdList(movieIds);
        Assert.assertTrue(result.size()>0);
        Assert.assertEquals(Sets.newHashSet(result).size(),result.size());
    }

    private JsonNode getMoviePageForTest(){
        ObjectMapper objectMapper = new ObjectMapper();
        URL resource = getClass().getClassLoader().getResource("PopularPage");
        try {
            return objectMapper.readTree(resource);
        } catch (IOException e) {
            return null;
        }
    }
    private JsonNode getCastPageForTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL resource = getClass().getClassLoader().getResource("CastPage");
        System.out.println(objectMapper.readTree(resource));
        try {
            return objectMapper.readTree(resource);
        } catch (IOException e) {
            return null;
        }
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme