package demo.getting_started.mvc;

import demo.getting_started.cookies.Cookies;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;

/**
 * Encapsulates the logic and configuration for switching the paging mode on the car list, and
 * persisting the users preferences.
 */
class ListPaging {

   static final String PAGING_ATTRIBUTE = "paging";
   static final String DEFAULT_ATTRIBUTE = "";

   private final Cookies cookies;

   /**
    * Constructs a new {@link ListPaging}.
    */
   ListPaging() {
      this( new Cookies() );
   }

   /**
    * Constructs a new {@link ListPaging}.
    * @param cookies for managing the cookies directly.
    */
   ListPaging( Cookies cookies ) {
      this.cookies = cookies;
   }

   /**
    * Configures the paging associated components following a refresh of the page.
    * @param carListBox     providing the car list.
    * @param pagingCheckBox controlling the paging preference.
    */
   void configurePagingComponentsAfterCompose( Listbox carListBox, Checkbox pagingCheckBox ) {
      boolean isPaging = cookies.isPaging();
      if ( isPaging ) {
         carListBox.setMold( PAGING_ATTRIBUTE );
         pagingCheckBox.setChecked( true );
      } else {
         carListBox.setMold( DEFAULT_ATTRIBUTE );
         pagingCheckBox.setChecked( false );
      }
   }

   /**
    * Configures the paging associated components following a change in preference in the ui.
    * @param carListBox     providing the car list.
    * @param pagingCheckBox controlling the paging preference.
    */
   void configurePagingInResponseToCheck( Listbox carListBox, Checkbox pagingCheckBox ) {
      cookies.configurePagingCookie( pagingCheckBox.isChecked() );
      if ( pagingCheckBox.isChecked() ) {
         carListBox.setMold( PAGING_ATTRIBUTE );
         carListBox.setPageSize( 5 );
      } else {
         carListBox.setMold( DEFAULT_ATTRIBUTE );
      }
   }
}
