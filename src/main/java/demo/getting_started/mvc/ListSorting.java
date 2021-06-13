package demo.getting_started.mvc;

import demo.getting_started.cookies.Cookies;
import demo.getting_started.model.comparator.ColourComparator;
import demo.getting_started.model.comparator.MakeComparator;
import demo.getting_started.model.comparator.ModelComparator;
import demo.getting_started.model.comparator.PriceComparator;
import demo.getting_started.model.structures.Car;
import org.zkoss.zul.Listheader;

import java.util.*;

import static demo.getting_started.cookies.Cookies.*;

/**
 * Encapsulates the behaviour associated with monitoring sorting and usage of cookies.
 */
class ListSorting {

   private final Cookies cookies;
   private final Map< String, Comparator< Car > > comparators;

   private Comparator< Car > currentComparator;

   /**
    * Constructs a new {@link ListSorting}.
    */
   ListSorting() {
      this( new Cookies() );
   }

   /**
    * Constructs a new {@link ListSorting}.
    * @param cookies for managing the persistence of the sorting.
    */
   ListSorting( Cookies cookies ) {
      this.cookies = cookies;
      this.comparators = new HashMap<>();

      comparators.put( MODEL_SORTING_KEY, new ModelComparator() );
      comparators.put( MAKE_SORTING_KEY, new MakeComparator() );
      comparators.put( COLOUR_SORTING_KEY, new ColourComparator() );
      comparators.put( PRICE_SORTING_KEY, new PriceComparator() );
   }

   /**
    * Sorts the given data if a sorting direction for a column is currently applied.
    * @param data to sort.
    */
   void sortData( List< Car > data ) {
      Optional.ofNullable( currentComparator ).ifPresent( data::sort );
   }

   /**
    * Configures the currently applied sorting following compose. This will use cookies to apply
    * the sorting last applied by the user.
    * @param modelHeader  column header.
    * @param makeHeader   column header.
    * @param colourHeader column header.
    * @param priceHeader  column header.
    */
   void configureSortingAfterCompose(
         Listheader modelHeader,
         Listheader makeHeader,
         Listheader colourHeader,
         Listheader priceHeader
   ) {
      modelHeader.setSortDirection( cookies.getModelColumnSorting() );
      makeHeader.setSortDirection( cookies.getMakeColumnSorting() );
      colourHeader.setSortDirection( cookies.getColourColumnSorting() );
      priceHeader.setSortDirection( cookies.getPriceColumnSorting() );

      Optional< String > sortingKey = cookies.getSortingKey();
      Optional< String > sortingDirection = cookies.getSortingDirection();
      if ( !sortingKey.isPresent() || !sortingDirection.isPresent() ) {
         return;
      }
      switch ( sortingDirection.get() ) {
         case SORTING_ASCENDING:
            currentComparator = comparators.get( sortingKey.get() );
            break;
         case SORTING_DESCENDING:
            currentComparator = comparators.get( sortingKey.get() ).reversed();
            break;
         case SORTING_NATURAL:
            currentComparator = null;
            break;
         default:
            break;
      }
   }

   /**
    * Configures the sorting applied when the sort direction is changed for a particular column.
    * @param key                     referring to the column being sorted.
    * @param currentSortingDirection the current sorting. This will transition to the next type
    *                                of sort: natural -> ascending -> descending -> ascending etc.
    */
   void configureSortForDirectionChange( String key, String currentSortingDirection ) {
      String sortedDirection = null;
      if ( currentSortingDirection.equals( SORTING_ASCENDING ) ) {
         sortedDirection = SORTING_DESCENDING;
      } else if ( currentSortingDirection.equals( SORTING_NATURAL ) ) {
         sortedDirection = SORTING_ASCENDING;
      } else {
         sortedDirection = SORTING_ASCENDING;
      }
      cookies.configureSorting( key, sortedDirection );

      switch ( sortedDirection ) {
         case SORTING_ASCENDING:
            currentComparator = comparators.get( key );
            break;
         case SORTING_DESCENDING:
            currentComparator = comparators.get( key ).reversed();
            break;
         default:
            break;
      }
   }
}
