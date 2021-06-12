package demo.getting_started.mvc;

/**
 * Defines all pages that can be navigated to.
 */
public enum ApplicationPage {

   DEMO_PAGE( "demo.zul" ),
   EDIT_CARS_PAGE( "car-edit.zul" );

   private final String pageName;

   /**
    * Constructs a new {@link ApplicationPage}.
    * @param pageName name of the page to be redirected to.
    */
   private ApplicationPage( String pageName ) {
      this.pageName = pageName;
   }

   public String pageName() {
      return pageName;
   }
}
