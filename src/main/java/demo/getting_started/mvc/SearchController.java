package demo.getting_started.mvc;


import demo.getting_started.beans.BeanResolver;
import demo.getting_started.model.services.CarService;
import demo.getting_started.model.structures.Car;
import demo.getting_started.utility.Messages;
import demo.getting_started.utility.PageRedirect;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;

/**
 * Provides the controls for searching data and providing results.
 */
@VariableResolver( { BeanResolver.class } )
public class SearchController extends SelectorComposer< Component > {

   private static final long serialVersionUID = 1L;

   private static final String CAR_ID_PARAMETER_FORMAT = "id=%d";

   @Wire
   private Textbox keywordBox;
   @Wire
   private Listbox carListbox;
   @Wire
   private Label modelLabel;
   @Wire
   private Label makeLabel;
   @Wire
   private Label priceLabel;
   @Wire
   private Label descriptionLabel;
   @Wire
   private Image previewImage;
   @Wire
   private Component detailBox;

   @WireVariable
   private CarService carService;

   private final PageRedirect pageRedirect;
   private final Messages messages;

   public SearchController() {
      this( new PageRedirect(), new Messages() );
   }

   SearchController( PageRedirect pageRedirect, Messages messages ) {
      this.pageRedirect = pageRedirect;
      this.messages = messages;
   }

   /**
    * Performs a search through car data based on the input in the keyword box.
    */
   @Listen( "onClick = #searchButton" )
   public void search() {
      String keyword = keywordBox.getValue();
      List< Car > result = carService.search( keyword );

      //identify current selection
      Optional< Car > existingSelection = extractCurrentModel()
            .flatMap( this::retrieveCurrentSelection );

      ListModelList< Car > searchResultModel = new ListModelList<>( result );
      carListbox.setModel( searchResultModel );

      //restore selection if possible
      if ( existingSelection.isPresent() && result.contains( existingSelection.get() ) ) {
         searchResultModel.setSelection( singleton( existingSelection.get() ) );
      }

      showDetailForCurrentSelection();
   }

   /**
    * Shows the detail of the current selection, if there is one.
    */
   @Listen( "onSelect = #carListbox" )
   public void showDetailForCurrentSelection() {
      ListModelList< Car > model = extractCurrentModel()
            .orElseThrow( () -> new IllegalStateException(
                  "Application only supports ListModelList."
            ) );

      Optional< Car > currentSelection = retrieveCurrentSelection( model );
      if ( !currentSelection.isPresent() ) {
         //nothing to select, hide detail
         detailBox.setVisible( false );
         return;
      }

      if ( !model.contains( currentSelection.get() ) ) {
         //our selection is no longer in the model - maybe due to filtering, hide detail
         detailBox.setVisible( false );
         return;
      }

      Car selected = currentSelection.get();
      detailBox.setVisible( true );
      previewImage.setSrc( selected.getPreview() );
      modelLabel.setValue( selected.getModel() );
      makeLabel.setValue( selected.getMake() );
      priceLabel.setValue(
            Optional.ofNullable( selected.getPrice() )
                  .map( Objects::toString )
                  .orElse( "Unknown" ) );
      descriptionLabel.setValue( selected.getDescription() );
   }

   private ListModelList< Car > expectModel() {
      return extractCurrentModel()
            .orElseThrow( () -> new IllegalStateException( "Expected model not present." ) );
   }

   /**
    * Convenience method to retrieve the {@link ListModelList} expected from the car listbox. It
    * is expected that this application only uses {@link ListModelList} however on start up, a
    * system default may be provided which will subsequently be replaced.
    * @return the model expected or empty if it has not been set yet.
    */
   private Optional< ListModelList< Car > > extractCurrentModel() {
      ListModel< ? > model = carListbox.getModel();
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
    * @param model to retrieve the selection from.
    * @return the current selection, if there is one. Note that this expects that multi selection
    * is not possible.
    */
   private Optional< Car > retrieveCurrentSelection( ListModelList< Car > model ) {
      if ( model == null || model.isEmpty() ) {
         return Optional.empty();
      }

      Set< Car > selection = model.getSelection();
      if ( selection == null || selection.isEmpty() ) {
         return Optional.empty();
      }
      return Optional.ofNullable( selection.iterator().next() );
   }

   /**
    * Called on window load. Provides any initialisation for this controller or default
    * information to the user.
    */
   @Listen( "onCreate = #carListbox" )
   public void windowLoad() {
      search();
   }

   /**
    * Redirects to the editing page.
    */
   @Listen( "onClick = #addCarButton" )
   public void addCar() {
      extractCurrentModel().ifPresent( ListModelList::clearSelection );
      editSelection();
   }

   /**
    * Deletes the selected car if the user confirms.
    */
   @Listen( "onClick = #deleteSelectionButton" )
   public void deleteSelection() {
      Optional< Car > currentSelection = extractCurrentModel()
            .flatMap( this::retrieveCurrentSelection );

      if ( !currentSelection.isPresent() ) {
         messages.information( "Cannot delete selection as nothing is selected.", "Car Deletion" );
         return;
      }

      String message = "Are you sure you wish to delete the current selection?";
      String title = "Car Deletion";
      messages.question( message, title, event -> completeDeletion(
            event,
            currentSelection.get()
      ) );
   }

   /**
    * Completes the deletion request when the user responds to prompt.
    * @param event indicating the response.
    * @param car   that was selected when the delete was requested.
    */
   private void completeDeletion( Event event, Car car ) {
      if ( !event.getName().equals( "onOK" ) ) {
         return;
      }

      carService.remove( car );
      expectModel().remove( car );
      showDetailForCurrentSelection();
   }

   @Listen( "onClick = #editSelectionButton" )
   public void editSelection() {
      Optional< Integer > carId = retrieveCurrentSelection( expectModel() )
            .map( Car::getId );
      if ( carId.isPresent() ) {
         pageRedirect.redirectTo(
               ApplicationPage.EDIT_CARS_PAGE,
               String.format( CAR_ID_PARAMETER_FORMAT, carId.get() )
         );
      } else {
         pageRedirect.redirectTo( ApplicationPage.EDIT_CARS_PAGE );
      }
   }

   /*
    * Ideally we would not have these setters. It's not clear how best to test these elements.
    * There's possibility for wiring on construction, or using set methods, but no success with
    * this thus far. Mockito is capable of these types of things so this should be able to be
    * improved.
    */

   void setCarService( CarService carService ) {
      this.carService = carService;
   }

   void setCarListbox( Listbox carListbox ) {
      this.carListbox = carListbox;
   }

   void setDescriptionLabel( Label descriptionLabel ) {
      this.descriptionLabel = descriptionLabel;
   }

   void setDetailBox( Component detailBox ) {
      this.detailBox = detailBox;
   }

   void setKeywordBox( Textbox keywordBox ) {
      this.keywordBox = keywordBox;
   }

   void setMakeLabel( Label makeLabel ) {
      this.makeLabel = makeLabel;
   }

   void setModelLabel( Label modelLabel ) {
      this.modelLabel = modelLabel;
   }

   void setPreviewImage( Image previewImage ) {
      this.previewImage = previewImage;
   }

   void setPriceLabel( Label priceLabel ) {
      this.priceLabel = priceLabel;
   }
}
