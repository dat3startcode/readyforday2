package dtos;

import entities.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieDTO {
    private long id;
    private String title;
    private int year;
    private String[] actors;

    public MovieDTO(String title, int year, String[] actors) {
        this.title = title;
        this.year = year;
        this.actors = actors;
    }
    public MovieDTO(Movie m) {
        this.id = m.getId();
        this.title = m.getTitle();
        this.year = m.getYear();
        this.actors = m.getActors();
    }
        
    public static List<MovieDTO> getMovieDTOs(List<Movie> movies){
        List<MovieDTO> movieDTOs = new ArrayList();
        movies.forEach(movie -> movieDTOs.add(new MovieDTO(movie)));
        return movieDTOs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }


    
    
    
    
    
    
}
