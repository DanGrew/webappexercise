package demo.getting_started.mvc;

import demo.getting_started.model.structures.Car;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import java.util.Optional;
import java.util.Set;

/**
 * Encapsulates the interactions with {@link Listbox}es in relation to the model used to manage
 * cars.
 */
public class ListModelOperations {

   /**
    * Extracts the model, expected in the return type.
    * @param carListBox to extract from.
    * @return the model.
    */
   ListModelList< Car > expectModel( Listbox carListBox ) {
      return extractCurrentModel( carListBox )
            .orElseThrow( () -> new IllegalStateException( "Expected model not present." ) );
   }

   /**
    * Convenience method to retrieve the {@link ListModelList} expected from the car list box. It
    * is expected that this application only uses {@link ListModelList} however on start up, a
    * system default may be provided which will subsequently be replaced.
    * @return the model expected or empty if it has not been set yet.
    */
   Optional< ListModelList< Car > > extractCurrentModel( Listbox carListBox ) {
      ListModel< ? > model = carListBox.getModel();
      if ( model == null ) {
         return Optional.empty();
      }

      if ( model instanceof ListModelList ) {
         return Optional.ofNullable( ( ListModelList< Car > ) model );
      }

      return Optional.empty();
   }

   /**
    * Retrieves the current selection from the given model.
    * @param carListBox to get the current selection from.
    * @return the current selection, if there is one. Note that this expects that multi selection
    * is not possible.
    */
   Optional< Car > retrieveCurrentSelection( Listbox carListBox ) {
      return retrieveCurrentSelection( expectModel( carListBox ) );
   }

   /**
    * Retrieves the current selection from the given model.
    * @param model to retrieve the selection from.
    * @return the current selection, if there is one. Note that this expects that multi selection
    * is not possible.
    */
   Optional< Car > retrieveCurrentSelection( ListModelList< Car > model ) {
      if ( model == null || model.isEmpty() ) {
         return Optional.empty();
      }

      Set< Car > selection = model.getSelection();
      if ( selection == null || selection.isEmpty() ) {
         return Optional.empty();
      }
      return Optional.ofNullable( selection.iterator().next() );
   }
}
