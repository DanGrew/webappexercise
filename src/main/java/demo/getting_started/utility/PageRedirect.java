package demo.getting_started.utility;

import demo.getting_started.mvc.ApplicationPage;
import org.zkoss.zk.ui.Executions;

/**
 * Encapsulates the logic for redirecting to another page so that individuals do not need to be
 * concerned with it and also to support testing.
 */
public class PageRedirect {

   /**
    * Redirects to the given page.
    * @param page to redirect to.
    */
   public void redirectTo( ApplicationPage page ) {
      Executions.sendRedirect( page.pageName() );
   }

   /**
    * Redirects to the given page.
    * @param page      to redirect to.
    * @param parameter the precise format of the parameter, typically in the form of "id=45".
    */
   public void redirectTo( ApplicationPage page, String parameter ) {
      Executions.sendRedirect( String.format( "%s?%s", page.pageName(), parameter ) );
   }
}
