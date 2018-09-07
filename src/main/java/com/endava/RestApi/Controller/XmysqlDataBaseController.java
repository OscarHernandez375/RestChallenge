package com.endava.RestApi.Controller;

import com.endava.RestApi.Services.TmdbServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/popular")
public class XmysqlDataBaseController {
    @Autowired
    TmdbServiceImpl tmdbService;
    @GetMapping("/cast")
    public List getCast() throws IOException {
            return tmdbService.getCastFromMovieId(299536);
    }
    @GetMapping("/movies")
    public List getMovies() throws IOException {
        return tmdbService.getSpecificNumberOfMovies(21);
    }

}
