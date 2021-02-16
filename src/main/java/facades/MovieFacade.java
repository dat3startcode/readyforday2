package facades;

import dtos.MovieDTO;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;


public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private MovieFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public MovieDTO create(MovieDTO m){
        Movie movie = new Movie(m.getTitle(),m.getYear(),m.getActors());
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MovieDTO(movie);
    }
    public MovieDTO getById(long id){
        EntityManager em = emf.createEntityManager();
        return new MovieDTO(em.find(Movie.class, id));
    }
  
    public long getMovieCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long count = (long)em.createQuery("SELECT COUNT(m) FROM Movie m").getSingleResult();
            return count;
        }finally{  
            em.close();
        }
        
    }
    
    public List<MovieDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        List<Movie> movies = query.getResultList();
        return MovieDTO.getMovieDTOs(movies);
    }
    
    public String getAllAsJson(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        List<Movie> movies = query.getResultList();
        return MovieDTO.getMovieDTOsAsJSON(movies);
    }
    
    public MovieDTO getMovieById(long id){
        EntityManager em = emf.createEntityManager();
        Movie movie = em.find(Movie.class, id);
        return new MovieDTO(movie);
    }
   
    public List<MovieDTO> getMoviesByTitle(String title){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Movie> query = em.createQuery("SELECT m from Movie m WHERE m.title LIKE :title",Movie.class);
        query.setParameter("title", "%"+title+"%");
        List<Movie> movies = query.getResultList();
        List<MovieDTO> movieDTOs = MovieDTO.getMovieDTOs(movies);
        return movieDTOs;
    }
    
    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        MovieFacade fe = getMovieFacade(emf);
        fe.getAll().forEach(dto->System.out.println(dto));
    }

}
