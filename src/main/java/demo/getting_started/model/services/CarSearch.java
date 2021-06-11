package demo.getting_started.model.services;

import demo.getting_started.model.structures.Car;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the logic for search through {@link Car}s for properties that match the input.
 */
class CarSearch {

   /**
    * Searches the given {@link Car}s to search matching properties of the cars against the input
    * search criteria.
    * @param carsToSearch   {@link Car}s to search through.
    * @param searchCriteria to match {@link Car}s against.
    * @return the matching {@link Car}s, can be the given list if all match.
    */
   List< Car > search( List< Car > carsToSearch, String searchCriteria ) {
      if ( searchCriteria == null || "".equals( searchCriteria ) ) {
         return carsToSearch;
      } else {
         return carsToSearch.stream()
               .filter( car -> carMatchesSearchCriteria( car, searchCriteria ) )
               .collect( Collectors.toList() );
      }
   }

   /**
    * Filter for searching that determines whether the given {@link Car} matches the given search
    * criteria in any way.
    * @param car            to check for a match.
    * @param searchCriteria the criteria to match against.
    * @return true if a match, false otherwise.
    */
   private boolean carMatchesSearchCriteria( Car car, String searchCriteria ) {
      if ( car.getModel() != null && car.getModel().toLowerCase().contains( searchCriteria.toLowerCase() ) ) {
         return true;
      }

      if ( car.getMake() != null && car.getMake().toLowerCase().contains( searchCriteria.toLowerCase() ) ) {
         return true;
      }

      if ( car.getColour() != null && car.getColour().getColourName().toLowerCase().contains( searchCriteria.toLowerCase() ) ) {
         return true;
      }

      return false;
   }
}
