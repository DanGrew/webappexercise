package demo.getting_started.cookies;

import demo.getting_started.utility.ExecutionsHandle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * Encapsulates interactions with cookies associated with the session.
 */
public class Cookies {

   static final String PAGING_COOKIE_NAME = "PAGING";
   static final String SORTING_COOKIE = "SORTING";

   public static final String SORTING_NATURAL = "natural";
   public static final String SORTING_ASCENDING = "ascending";
   public static final String SORTING_DESCENDING = "descending";

   public static final String MODEL_SORTING_KEY = "MODEL";
   public static final String MAKE_SORTING_KEY = "MAKE";
   public static final String COLOUR_SORTING_KEY = "COLOUR";
   public static final String PRICE_SORTING_KEY = "PRICE";

   private final ExecutionsHandle executionsHandle;

   /**
    * Constructs a new {@link Cookies}.
    */
   public Cookies() {
      this( new ExecutionsHandle() );
   }

   /**
    * Constructs a new {@link Cookies}.
    * @param executionsHandle for interacting with the {@link org.zkoss.zk.ui.Execution}.
    */
   Cookies( ExecutionsHandle executionsHandle ) {
      this.executionsHandle = executionsHandle;
   }

   /**
    * Applies a cookie to enable paging.
    */
   public void setPagingCookie() {
      setCookie( PAGING_COOKIE_NAME, Boolean.TRUE.toString() );
   }

   /**
    * Applies a cookie to disable paging.
    */
   public void setNotPagingCookie() {
      setCookie( PAGING_COOKIE_NAME, Boolean.FALSE.toString() );
   }

   /**
    * Configures the use of paging in the form of a cookie based on the given boolean.
    * @param isPaging whether enabled.
    */
   public void configurePagingCookie( boolean isPaging ) {
      if ( isPaging ) {
         setPagingCookie();
      } else {
         setNotPagingCookie();
      }
   }

   /**
    * Determines whether paging is active based on cookies found.
    * @return true if enabled.
    */
   public boolean isPaging() {
      String cookie = getCookie( PAGING_COOKIE_NAME );
      if ( cookie == null ) {
         return false;
      }

      return cookie.equals( Boolean.TRUE.toString() );
   }

   /**
    * Provides the current sorting applied to the model column.
    * @return the sorting applied, as the correct attribute value.
    */
   public String getModelColumnSorting() {
      return getSortingForKey( MODEL_SORTING_KEY );
   }

   /**
    * Provides the current sorting applied to the make column.
    * @return the sorting applied, as the correct attribute value.
    */
   public String getMakeColumnSorting() {
      return getSortingForKey( MAKE_SORTING_KEY );
   }

   /**
    * Provides the current sorting applied to the colour column.
    * @return the sorting applied, as the correct attribute value.
    */
   public String getColourColumnSorting() {
      return getSortingForKey( COLOUR_SORTING_KEY );
   }

   /**
    * Provides the current sorting applied to the price column.
    * @return the sorting applied, as the correct attribute value.
    */
   public String getPriceColumnSorting() {
      return getSortingForKey( PRICE_SORTING_KEY );
   }

   /**
    * Calculates the sorting applied for the following key.
    * @param key representing the column in question.
    * @return the sorting applied, never null.
    */
   private String getSortingForKey( String key ) {
      String cookieValue = getCookie( SORTING_COOKIE );
      if ( cookieValue == null ) {
         return SORTING_NATURAL;
      }
      return Optional.ofNullable( cookieValue )
            .map( SortingCookie::cookieForValue )
            .filter( Optional::isPresent )
            .map( Optional::get )
            .map( cookie -> cookie.getSortingForKey( key ) )
            .orElse( SORTING_NATURAL );
   }

   /**
    * Calculates the sorting key, referring to the column being sorted.
    * @return the key, or empty if no sorted saved in cookies.
    */
   public Optional< String > getSortingKey() {
      return Optional.ofNullable( getCookie( SORTING_COOKIE ) )
            .map( SortingCookie::cookieForValue )
            .filter( Optional::isPresent )
            .map( Optional::get )
            .map( SortingCookie::getKey );
   }

   /**
    * Calculates the sorting direction currently applied.
    * @return the sorting, or empty if no sorted saved in cookies.
    */
   public Optional< String > getSortingDirection() {
      return Optional.ofNullable( getCookie( SORTING_COOKIE ) )
            .map( SortingCookie::cookieForValue )
            .filter( Optional::isPresent )
            .map( Optional::get )
            .map( SortingCookie::getSorting );
   }

   /**
    * Configures the sorting in cookies for the given key and sorting direction.
    * @param key     referring to the column being sorted.
    * @param sorting direction.
    */
   public void configureSorting( String key, String sorting ) {
      setCookie( SORTING_COOKIE, new SortingCookie( key, sorting ).toValue() );
   }

   /**
    * Interacts with the response to add a cookie.
    * @param name  of the cookie.
    * @param value of the cookie.
    */
   private void setCookie( String name, String value ) {
      Object nativeResponse = executionsHandle.getNativeResponse();
      if ( !( nativeResponse instanceof HttpServletResponse ) ) {
         return;
      }
      ( ( HttpServletResponse ) nativeResponse ).addCookie(
            new Cookie( name, value ) );
   }

   /**
    * Interacts with the request to retrieve the value associated with the given cookie name.
    * @param name of the cookie.
    * @return the value of the cookie, or null if no cookie present.
    */
   private String getCookie( String name ) {
      Object nativeRequest = executionsHandle.getNativeRequest();
      if ( !( nativeRequest instanceof HttpServletRequest ) ) {
         return null;
      }
      Cookie[] cookies = ( ( HttpServletRequest ) nativeRequest )
            .getCookies();
      if ( cookies == null ) {
         return null;
      }

      return stream( cookies )
            .filter( cookie -> cookie.getName().equals( name ) )
            .findFirst()
            .map( Cookie::getValue )
            .orElse( null );
   }
}
