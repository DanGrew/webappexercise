package demo.getting_started.cookies;

import java.util.Optional;

/**
 * Encapsulates the information associated with a sorting cookie. We want to persist a single
 * type of sorting for one column, and so we compose a value of the column reference and the
 * sorting applied.
 */
class SortingCookie {

   private final String key;
   private final String sorting;

   /**
    * Constructs a new {@link SortingCookie}.
    * @param key     referring to the column.
    * @param sorting the actual sorting applied.
    */
   SortingCookie( String key, String sorting ) {
      this.key = key;
      this.sorting = sorting;
   }

   /**
    * Provides the reference to the column.
    * @return the key.
    */
   String getKey() {
      return key;
   }

   /**
    * Provides the exact direction of sorting applied to the column, directly compatible with ui
    * configuration.
    * @return the sorting direction.
    */
   String getSorting() {
      return sorting;
   }

   /**
    * Determines the sorting for the given key. If it matches this cookie's key, then
    * {@link #getSorting()} is returned, otherwise the default {@link Cookies#SORTING_NATURAL}.
    * @param key to get the sorting for.
    * @return the sorting direction.
    */
   String getSortingForKey( String key ) {
      if ( this.key.equals( key ) ) {
         return sorting;
      }

      return Cookies.SORTING_NATURAL;
   }

   /**
    * Builds the cookie information into a single value that can be held in a cookie.
    * @return the value.
    */
   String toValue() {
      return getKey() + "-" + getSorting();
   }

   /**
    * Builds a new {@link SortingCookie} from the value held in a cookie by splitting it into the
    * relevant information.
    * @param value to split.
    * @return the resulting {@link SortingCookie}, or empty if cookie value invalid.
    */
   static Optional< SortingCookie > cookieForValue( String value ) {
      String[] parts = value.split( "-" );
      if ( parts.length != 2 ) {
         return Optional.empty();
      }
      return Optional.of( new SortingCookie( parts[0], parts[1] ) );
   }
}
