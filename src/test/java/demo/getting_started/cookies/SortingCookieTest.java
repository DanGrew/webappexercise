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

package demo.getting_started.cookies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SortingCookieTest {

   private SortingCookie systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new SortingCookie( "key", "value" );
   }

   @Test
   public void shouldParseBackFromValue() {
      SortingCookie sortingCookie = SortingCookie.cookieForValue( systemUnderTest.toValue() ).get();
      assertThat( sortingCookie.getSorting(), equalTo( systemUnderTest.getSorting() ) );
      assertThat( sortingCookie.getKey(), equalTo( systemUnderTest.getKey() ) );
   }

   @Test
   public void shouldHandleInvalidCookies() {
      assertAll(
            () -> assertThat( SortingCookie.cookieForValue( "anything" ).isPresent(),
                  equalTo( false ) ),
            () -> assertThat( SortingCookie.cookieForValue( "too-many-params" ).isPresent(),
                  equalTo( false ) ),
            () -> assertThat( SortingCookie.cookieForValue( "split wrong" ).isPresent(),
                  equalTo( false ) )
      );
   }
}