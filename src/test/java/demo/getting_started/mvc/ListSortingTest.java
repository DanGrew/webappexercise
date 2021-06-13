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

import demo.getting_started.cookies.Cookies;
import demo.getting_started.model.comparator.ColourComparator;
import demo.getting_started.model.structures.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zkoss.zul.Listheader;

import java.util.List;
import java.util.Optional;

import static demo.getting_started.cookies.Cookies.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class ListSortingTest {

   @Mock
   private Listheader modelHeader;
   @Mock
   private Listheader makeHeader;
   @Mock
   private Listheader colourHeader;
   @Mock
   private Listheader priceHeader;

   @Mock
   private Cookies cookies;
   private ListSorting systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new ListSorting( cookies );
   }

   @Test
   public void shouldNotSortData() {
      List< Car > data = mock( List.class );
      systemUnderTest.sortData( data );
      verify( data, never() ).sort( any() );
   }

   @Test
   public void shouldSortDataWhenTakenFromCookies() {
      when( cookies.getSortingKey() ).thenReturn( Optional.of( COLOUR_SORTING_KEY ) );
      when( cookies.getSortingDirection() ).thenReturn( Optional.of( SORTING_ASCENDING ) );
      systemUnderTest.configureSortingAfterCompose(
            modelHeader, makeHeader, colourHeader, priceHeader
      );

      List< Car > data = mock( List.class );
      systemUnderTest.sortData( data );
      verify( data ).sort( any( ColourComparator.class ) );
   }

   @Test
   public void shouldUpdateSortDirectionsFromCookie() {
      when( cookies.getModelColumnSorting() ).thenReturn( "a" );
      when( cookies.getMakeColumnSorting() ).thenReturn( "b" );
      when( cookies.getColourColumnSorting() ).thenReturn( "c" );
      when( cookies.getPriceColumnSorting() ).thenReturn( "d" );

      systemUnderTest.configureSortingAfterCompose(
            modelHeader, makeHeader, colourHeader, priceHeader
      );

      verify( modelHeader ).setSortDirection( "a" );
      verify( makeHeader ).setSortDirection( "b" );
      verify( colourHeader ).setSortDirection( "c" );
      verify( priceHeader ).setSortDirection( "d" );
   }

   @Test
   public void shouldConfigureCookieFromChange() {
      systemUnderTest.configureSortForDirectionChange( COLOUR_SORTING_KEY, SORTING_DESCENDING );
      verify( cookies ).configureSorting( COLOUR_SORTING_KEY, SORTING_ASCENDING );

      List< Car > data = mock( List.class );
      systemUnderTest.sortData( data );
      verify( data ).sort( any( ColourComparator.class ) );
   }
}