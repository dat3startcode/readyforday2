package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieDTO;
import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieResourceTest {
    
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie m1, m2, m3;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }
    
    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        m1 = new Movie("aa", 2010, new String[]{"A", "B"});
        m2 = new Movie("bb and aa", 2011, new String[]{"C", "D"});
        m3 = new Movie("bb", 2011, new String[]{"Q", "D"});
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/movie/isalive").then()
                .assertThat().statusCode(200)
                .body("msg", equalTo("Movie API is up"));
    }
    
    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/movie/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(3));
    }

    //@Test
    public void doThisWhenYouHaveProblems() {
        given().log().all().when().get("/movie/all").then().log().body();
    }
    
    @Test
    void testGetAllV0() {
        given()
                .when()
                .get("/movie/all")
                .then()
                .assertThat()
                .body("", hasSize(3));
    }
    
    @Test
    public void testGetAll() {
        given()
                .contentType("application/json")
                .get("/movie/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems("aa", "bb", "bb and aa"));
    }
    
    @Test
    public void testById() {
        given().log().all().when().get("/movie/byid/" + m1.getId()).then().log().body();
        
        given()
                .contentType("application/json")
                .get("/movie/byid/" + m1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", equalTo(m1.getTitle()));
    }
    
    //Not working
    //@Test
    public void testGetAllWithActors() {
        String[][] all = new String[][] {{"A","B"},{"C","D"},{"C","D"}};   
        given()
                .when()
                .get("/movie/all")
                .then()
                .assertThat()
                .body("actors[0]", isIn(all));
                //.body("actors[0]", hasItems("C","D"));
                
    }

    
    //@Test
    public void testByTitle(){
      //For you to do  
    }

    
     @Test
    /* Observe: You must override the equals method for MovieDTO for this to work */        
    void testGetAllV2() {
        List<MovieDTO> movies;
        movies = given()
                .contentType("application/json")
                .when()
                .get("/movie/all")
                .then()
                .extract().body().jsonPath().getList(".", MovieDTO.class);

        MovieDTO m1DTO = new MovieDTO(m1);
        MovieDTO m2DTO = new MovieDTO(m2);
        MovieDTO m3DTO = new MovieDTO(m3);
        
        assertThat(movies, containsInAnyOrder(m1DTO, m2DTO, m3DTO));
    }

    
    
    

}
