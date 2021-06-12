package demo.getting_started.model.services;

import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;

import java.util.List;
import java.util.Optional;

public interface CarService {

   /**
    * Retrieve all cars in the catalog.
    * @return all cars
    */
   public List< Car > findAll();

   /**
    * search cars according to keyword in name and company.
    * @param keyword for search
    * @return list of car that match the keyword
    */
   public List< Car > search( String keyword );

   /**
    * Creates a new {@link Car} with no properties other than assigned id.
    * @return the created car.
    */
   public Car create();

   /**
    * Creates a new {@link Car} with the given properties.
    * @param model          of the car.
    * @param make           of the car.
    * @param description    for the car.
    * @param preview        of the car.
    * @param price          of the car.
    * @param sortableColour of the car.
    * @return the created car.
    */
   public Car create(
         String model,
         String make,
         String description,
         String preview,
         Integer price,
         SortableColour sortableColour
   );

   public void remove( Car car );

   /**
    * Looks up the given id and returns the matching car if present.
    * @param id of the car to find.
    * @return the found car or empty if none matching.
    */
   public Optional< Car > find( Integer id );
}
