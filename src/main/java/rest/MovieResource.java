package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieDTO;
import utils.EMF_Creator;
import facades.MovieFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final MovieFacade FACADE =  MovieFacade.getMovieFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        
    @Path("isalive")    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Movie API is up\"}";
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getRenameMeCount() {
        long count = FACADE.getMovieCount();        
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAll() {
        List<MovieDTO> movies = FACADE.getAll();        
        return GSON.toJson(movies);
    }
    
    @Path("allv2")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllV2() {
        String json =  FACADE.getAllAsJson();
        return json;
    }
    
    @Path("byid/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") long id) {
        MovieDTO movie = FACADE.getById(id);        
        return GSON.toJson(movie);
    }
    
    @Path("bytitle/{title}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByTitle(@PathParam("title") String  title) {
        List<MovieDTO> movies = FACADE.getMoviesByTitle(title);        
        return GSON.toJson(movies);
    }
}
