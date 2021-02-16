package facades;

import dtos.MovieDTO;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        MovieFacade facade = MovieFacade.getMovieFacade(emf);
        facade.create(new MovieDTO("Film A",2000,new String[] {"aaa", "bbb", "ccc"}));
        facade.create(new MovieDTO("Film A and more",2000,new String[] {"a11", "b11", "c11"}));
        facade.create(new MovieDTO("Film B",2000,new String[] {"a11", "b11", "c11"}));
        facade.create(new MovieDTO("Film C",2000,new String[] {"a22", "b22", "c22"}));
    }
    
    public static void main(String[] args) {
        populate();
    }
}
