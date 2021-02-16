package dtos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Movie;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static String getMovieDTOsAsJSON(List<Movie> movies){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<MovieDTO> movieDTOs = new ArrayList();
        movies.forEach(movie -> movieDTOs.add(new MovieDTO(movie)));
        return "{\"all\":"+gson.toJson(movieDTOs)+"}";
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 83 * hash + Arrays.deepHashCode(this.actors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovieDTO other = (MovieDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Arrays.deepEquals(this.actors, other.actors)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
