// **************************************************
//                        GSDK                       
//           Graffica System Development Kit         
//                                                   
//  Release Version: {RELEASE_VERSION}               
//  Copyright: (c) Graffica Ltd {RELEASE_DATE}       
//                                                   
// **************************************************
//  This software is provided under the terms of the 
//        Graffica Software Licence Agreement        
//                                                   
//     THIS HEADER MUST NOT BE ALTERED OR REMOVED    
// **************************************************

package demo.getting_started.mvc;

import demo.getting_started.model.services.CarService;
import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;
import demo.getting_started.utility.Messages;
import demo.getting_started.utility.PageRedirect;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import java.util.List;

import static demo.getting_started.cookies.Cookies.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class SearchControllerTest {

   private static final String SEARCH_CRITERIA = "anything";

   @Captor
   private ArgumentCaptor< ListModelList< Car > > modelCaptor;

   @Mock
   private Textbox keywordBox;
   @Mock
   private Listbox carListbox;
   @Mock
   private Label modelLabel;
   @Mock
   private Label makeLabel;
   @Mock
   private Label priceLabel;
   @Mock
   private Label descriptionLabel;
   @Mock
   private Image previewImage;
   @Mock
   private Component detailBox;
   @Mock
   private Checkbox pagingCheckBox;
   @Mock
   private Listheader modelHeader;
   @Mock
   private Listheader makeHeader;
   @Mock
   private Listheader colourHeader;
   @Mock
   private Listheader priceHeader;
   @Mock
   private Button deleteSelectionButton;
   @Mock
   private Button editSelectionButton;

   @Mock
   private CarService carService;
   @Mock
   private PageRedirect pageRedirect;
   @Mock
   private Messages messages;
   @Mock
   private ListPaging listPaging;
   @Mock
   private ListSorting listSorting;
   private SearchController systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new SearchController( pageRedirect, messages, listPaging, listSorting );

      systemUnderTest.setCarService( carService );
      systemUnderTest.setKeywordBox( keywordBox );
      systemUnderTest.setCarListbox( carListbox );
      systemUnderTest.setModelLabel( modelLabel );
      systemUnderTest.setMakeLabel( makeLabel );
      systemUnderTest.setPriceLabel( priceLabel );
      systemUnderTest.setDescriptionLabel( descriptionLabel );
      systemUnderTest.setPreviewImage( previewImage );
      systemUnderTest.setDetailBox( detailBox );
      systemUnderTest.setPagingCheckBox( pagingCheckBox );
      systemUnderTest.setColourHeader( colourHeader );
      systemUnderTest.setMakeHeader( makeHeader );
      systemUnderTest.setModelHeader( modelHeader );
      systemUnderTest.setPriceHeader( priceHeader );
      systemUnderTest.setDeleteSelectionButton( deleteSelectionButton );
      systemUnderTest.setEditSelectionButton( editSelectionButton );
   }

   @Test
   public void shouldUpdateModelWithSearchResults() {
      List< Car > searchResult = asList( mock( Car.class ) );
      when( carService.search( SEARCH_CRITERIA ) ).thenReturn( searchResult );
      when( keywordBox.getValue() ).thenReturn( SEARCH_CRITERIA );
      when( carListbox.getModel() ).thenReturn( new ListModelList<>( searchResult ) );

      systemUnderTest.search();

      InOrder order = inOrder( listSorting, carListbox );
      order.verify( listSorting ).sortData( searchResult );
      order.verify( carListbox ).setModel( modelCaptor.capture() );
      assertThat( modelCaptor.getValue().getInnerList(), equalTo( searchResult ) );
   }

   @Test
   public void shouldRestoreSelectionWhenSearching() {
      Car selected = new Car();
      List< Car > searchResult = asList( selected );
      when( carService.search( SEARCH_CRITERIA ) ).thenReturn( searchResult );
      when( keywordBox.getValue() ).thenReturn( SEARCH_CRITERIA );

      ListModelList< Object > modelList = new ListModelList<>( searchResult );
      modelList.setSelection( singleton( selected ) );
      when( carListbox.getModel() ).thenReturn( modelList );

      systemUnderTest.search();

      verify( carListbox ).setModel( modelCaptor.capture() );
      assertThat( modelCaptor.getValue().getSelection(), containsInAnyOrder( selected ) );
   }

   @Test
   public void shouldEnableEditAndDeleteWhenRestoringSelection() {
      shouldRestoreSelectionWhenSearching();
      verify( deleteSelectionButton ).setDisabled( false );
      verify( editSelectionButton ).setDisabled( false );
   }

   @Test
   public void shouldNotRestoreSelectionWhenSearchResultsDoNotContainSelection() {
      Car selected = new Car();
      List< Car > searchResult = asList( new Car() );
      when( carService.search( SEARCH_CRITERIA ) ).thenReturn( searchResult );
      when( keywordBox.getValue() ).thenReturn( SEARCH_CRITERIA );

      ListModelList< Object > modelList = new ListModelList<>( searchResult );
      modelList.setSelection( singleton( selected ) );
      when( carListbox.getModel() ).thenReturn( modelList );

      systemUnderTest.search();

      verify( carListbox ).setModel( modelCaptor.capture() );
      assertThat( modelCaptor.getValue().getSelection(), empty() );
   }

   @Test
   public void shouldRetainDetailShownFollowingSearch() {
      shouldRestoreSelectionWhenSearching();
      verify( detailBox ).setVisible( true );
      verify( deleteSelectionButton ).setDisabled( false );
      verify( editSelectionButton ).setDisabled( false );
   }

   @Test
   public void shouldHideDetailShowFollowingSearch() {
      shouldNotRestoreSelectionWhenSearchResultsDoNotContainSelection();
      verify( detailBox ).setVisible( false );
   }

   @Test
   public void shouldHideDetailWhenNoSelectionPresent() {
      Car notSelected = new Car( 21, "Model", "Make", "Description", "Preview", 20000,
            new SortableColour( "White" )
      );

      ListModelList< Object > modelList = new ListModelList<>( singleton( notSelected ) );

      modelList.setSelection( emptyList() );
      when( carListbox.getModel() ).thenReturn( modelList );

      systemUnderTest.showDetailForCurrentSelection();
      verify( detailBox ).setVisible( false );
   }

   @Test
   public void shouldProvideAllDataWhenLoaded() {
      List< Car > searchResult = asList( mock( Car.class ) );
      when( carService.search( "" ) ).thenReturn( searchResult );
      when( keywordBox.getValue() ).thenReturn( "" );
      when( carListbox.getModel() ).thenReturn( new ListModelList<>( searchResult ) );

      systemUnderTest.windowLoad();

      verify( carListbox ).setModel( modelCaptor.capture() );
      assertThat( modelCaptor.getValue().getInnerList(), equalTo( searchResult ) );
   }

   @Test
   public void shouldRedirectToEditPageForAddingCar() {
      ListModelList< Object > modelList = mock( ListModelList.class );
      when( carListbox.getModel() ).thenReturn( modelList );

      systemUnderTest.addCar();
      InOrder order = inOrder( modelList, pageRedirect );
      order.verify( modelList ).clearSelection();
      order.verify( pageRedirect ).redirectTo( ApplicationPage.EDIT_CARS_PAGE );
   }

   @Test
   public void shouldNotDeleteCarIfNoneSelected() {
      systemUnderTest.deleteSelection();

      verify( messages ).information(
            "Cannot delete selection as nothing is selected.",
            "Car Deletion"
      );

      verify( carService, never() ).remove( any() );
   }

   @Test
   public void shouldConfigurePagingAfterCompose() throws Exception {
      systemUnderTest.doAfterCompose( mock( Component.class ) );
      verify( listPaging ).configurePagingComponentsAfterCompose( carListbox, pagingCheckBox );
   }

   @Test
   public void shouldConfigurePagingInResponseToCheck() {
      systemUnderTest.pagingModeChanged();
      verify( listPaging ).configurePagingInResponseToCheck( carListbox, pagingCheckBox );
   }

   @Test
   public void shouldConfigureSortingAfterCompose() throws Exception {
      systemUnderTest.doAfterCompose( mock( Component.class ) );
      verify( listSorting ).configureSortingAfterCompose(
            modelHeader, makeHeader, colourHeader, priceHeader
      );
   }

   @Test
   public void shouldConfigureSortingInResponseToSort() {
      when( modelHeader.getSortDirection() ).thenReturn( SORTING_DESCENDING );
      when( makeHeader.getSortDirection() ).thenReturn( SORTING_ASCENDING );
      when( colourHeader.getSortDirection() ).thenReturn( SORTING_NATURAL );
      when( priceHeader.getSortDirection() ).thenReturn( SORTING_DESCENDING );

      systemUnderTest.sortModel();
      verify( listSorting ).configureSortForDirectionChange(
            MODEL_SORTING_KEY, SORTING_DESCENDING );

      systemUnderTest.sortMake();
      verify( listSorting ).configureSortForDirectionChange(
            MAKE_SORTING_KEY, SORTING_ASCENDING );

      systemUnderTest.sortColour();
      verify( listSorting ).configureSortForDirectionChange(
            COLOUR_SORTING_KEY, SORTING_NATURAL );

      systemUnderTest.sortPrice();
      verify( listSorting ).configureSortForDirectionChange(
            PRICE_SORTING_KEY, SORTING_DESCENDING );
   }

   @Nested
   public class SelectionBasedTests {

      private Car selected;

      @BeforeEach
      public void initialiseTestData() {
         selected = new Car( 21, "Model", "Make", "Description", "Preview", 20000,
               new SortableColour( "White" )
         );

         ListModelList< Object > modelList = new ListModelList<>( singleton( selected ) );

         modelList.setSelection( singleton( selected ) );
         when( carListbox.getModel() ).thenReturn( modelList );
      }

      @Test
      public void shouldShowDetailWhenSelectionOccurs() {
         systemUnderTest.showDetailForCurrentSelection();
         verify( detailBox ).setVisible( true );
         verify( previewImage ).setSrc( selected.getPreview() );
         verify( modelLabel ).setValue( selected.getModel() );
         verify( makeLabel ).setValue( selected.getMake() );
         verify( priceLabel ).setValue( "20000" );
         verify( descriptionLabel ).setValue( selected.getDescription() );
      }

      @Test
      public void shouldDeleteCarWhenInstructed() throws Exception {
         systemUnderTest.deleteSelection();

         ArgumentCaptor< EventListener< Event > > eventListenerCaptor =
               ArgumentCaptor.forClass( EventListener.class );
         verify( messages ).question(
               eq( "Are you sure you wish to delete the current selection?" ),
               eq( "Car Deletion" ),
               eventListenerCaptor.capture()
         );

         eventListenerCaptor.getValue().onEvent( new Event( "onOK" ) );
         verify( carService ).remove( selected );
         verify( detailBox ).setVisible( false );
      }

      @Test
      public void shouldNotDeleteCarIfCancelled() throws Exception {
         systemUnderTest.deleteSelection();

         ArgumentCaptor< EventListener< Event > > eventListenerCaptor =
               ArgumentCaptor.forClass( EventListener.class );
         verify( messages ).question(
               eq( "Are you sure you wish to delete the current selection?" ),
               eq( "Car Deletion" ),
               eventListenerCaptor.capture()
         );

         eventListenerCaptor.getValue().onEvent( new Event( "anything" ) );
         verify( carService, never() ).remove( selected );
      }

      @Test
      public void shouldProvideParameterWhenEditingSelection() {
         systemUnderTest.editSelection();
         verify( pageRedirect ).redirectTo( ApplicationPage.EDIT_CARS_PAGE, "id=21" );
      }
   }
}