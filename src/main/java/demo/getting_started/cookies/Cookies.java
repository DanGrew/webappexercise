package demo.getting_started.cookies;

import demo.getting_started.utility.ExecutionsHandle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Arrays.stream;

/**
 * Encapsulates interactions with cookies associated with the session.
 */
public class Cookies {

   static final String PAGING_COOKIE_NAME = "PAGING";

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
