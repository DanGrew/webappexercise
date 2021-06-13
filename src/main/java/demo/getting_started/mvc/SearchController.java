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

import static demo.getting_started.cookies.Cookies.*;
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
   private Listheader modelHeader;
   @Wire
   private Listheader makeHeader;
   @Wire
   private Listheader colourHeader;
   @Wire
   private Listheader priceHeader;
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
   @Wire
   private Checkbox pagingCheckBox;

   @WireVariable
   private CarService carService;

   private final PageRedirect pageRedirect;
   private final Messages messages;
   private final ListPaging listPaging;
   private final ListSorting listSorting;
   private final ListModelOperations listModelOperations;

   /**
    * Constructs a new {@link SearchController}.
    */
   public SearchController() {
      this( new PageRedirect(), new Messages(), new ListPaging(), new ListSorting() );
   }

   /**
    * Constructs a new {@link SearchController}.
    * @param pageRedirect for managing page redirection.
    * @param messages     for managing user messages.
    * @param listPaging   for managing list paging.
    * @param listSorting  for managing sorting.
    */
   SearchController(
         PageRedirect pageRedirect,
         Messages messages,
         ListPaging listPaging,
         ListSorting listSorting
   ) {
      this.pageRedirect = pageRedirect;
      this.messages = messages;
      this.listPaging = listPaging;
      this.listSorting = listSorting;
      this.listModelOperations = new ListModelOperations();
   }

   @Override
   public void doAfterCompose( Component comp ) throws Exception {
      super.doAfterCompose( comp );
      listPaging.configurePagingComponentsAfterCompose( carListbox, pagingCheckBox );
      listSorting.configureSortingAfterCompose(
            modelHeader, makeHeader, colourHeader, priceHeader
      );
   }

   /**
    * Performs a search through car data based on the input in the keyword box.
    */
   @Listen( "onClick = #searchButton" )
   public void search() {
      String keyword = keywordBox.getValue();
      List< Car > result = carService.search( keyword );

      //identify current selection
      Optional< Car > existingSelection = listModelOperations.extractCurrentModel( carListbox )
            .flatMap( listModelOperations::retrieveCurrentSelection );

      listSorting.sortData( result );
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
      ListModelList< Car > model = listModelOperations.extractCurrentModel( carListbox )
            .orElseThrow( () -> new IllegalStateException(
                  "Application only supports ListModelList."
            ) );

      Optional< Car > currentSelection = listModelOperations.retrieveCurrentSelection( model );
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

   /**
    * Handles the user changing the state of the paging mode.
    */
   @Listen( "onCheck = #pagingCheckBox" )
   public void pagingModeChanged() {
      listPaging.configurePagingInResponseToCheck( carListbox, pagingCheckBox );
   }

   /**
    * Handles sorting applied to the model column.
    */
   @Listen( "onSort = #modelHeader" )
   public void sortModel() {
      listSorting.configureSortForDirectionChange(
            MODEL_SORTING_KEY,
            modelHeader.getSortDirection()
      );
   }

   /**
    * Handles sorting applied to the make column.
    */
   @Listen( "onSort = #makeHeader" )
   public void sortMake() {
      listSorting.configureSortForDirectionChange(
            MAKE_SORTING_KEY,
            makeHeader.getSortDirection()
      );
   }

   /**
    * Handles sorting applied to the colour column.
    */
   @Listen( "onSort = #colourHeader" )
   public void sortColour() {
      listSorting.configureSortForDirectionChange(
            COLOUR_SORTING_KEY,
            colourHeader.getSortDirection()
      );
   }

   /**
    * Handles sorting applied to the price column.
    */
   @Listen( "onSort = #priceHeader" )
   public void sortPrice() {
      listSorting.configureSortForDirectionChange(
            PRICE_SORTING_KEY,
            priceHeader.getSortDirection()
      );
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
      listModelOperations.extractCurrentModel( carListbox ).ifPresent( ListModelList::clearSelection );
      editSelection();
   }

   /**
    * Deletes the selected car if the user confirms.
    */
   @Listen( "onClick = #deleteSelectionButton" )
   public void deleteSelection() {
      Optional< Car > currentSelection = listModelOperations.extractCurrentModel( carListbox )
            .flatMap( listModelOperations::retrieveCurrentSelection );

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
      listModelOperations.expectModel( carListbox ).remove( car );
      showDetailForCurrentSelection();
   }

   @Listen( "onClick = #editSelectionButton" )
   public void editSelection() {
      Optional< Integer > carId = listModelOperations.retrieveCurrentSelection( carListbox )
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

   void setCarListbox( Listbox carListBox ) {
      this.carListbox = carListBox;
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

   void setPagingCheckBox( Checkbox pagingCheckBox ) {
      this.pagingCheckBox = pagingCheckBox;
   }

   void setColourHeader( Listheader colourHeader ) {
      this.colourHeader = colourHeader;
   }

   void setMakeHeader( Listheader makeHeader ) {
      this.makeHeader = makeHeader;
   }

   void setModelHeader( Listheader modelHeader ) {
      this.modelHeader = modelHeader;
   }

   void setPriceHeader( Listheader priceHeader ) {
      this.priceHeader = priceHeader;
   }
}

