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

import demo.getting_started.utility.ExecutionsHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class CookiesTest {

   @Mock
   private HttpServletResponse response;
   @Mock
   private HttpServletRequest request;
   @Captor
   private ArgumentCaptor< Cookie > cookieCaptor;

   @Mock
   private ExecutionsHandle executionsHandle;
   private Cookies systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new Cookies( executionsHandle );

      lenient().when( executionsHandle.getNativeResponse() ).thenReturn( response );
      lenient().when( executionsHandle.getNativeRequest() ).thenReturn( request );
   }

   @Test
   public void shouldSetPagingCookie() {
      systemUnderTest.setPagingCookie();
      verify( response ).addCookie( cookieCaptor.capture() );

      assertThat( cookieCaptor.getValue().getName(), equalTo( Cookies.PAGING_COOKIE_NAME ) );
      assertThat( cookieCaptor.getValue().getValue(), equalTo( Boolean.TRUE.toString() ) );
   }

   @Test
   public void shouldSetNotPagingCookie() {
      systemUnderTest.setNotPagingCookie();
      verify( response ).addCookie( cookieCaptor.capture() );

      assertThat( cookieCaptor.getValue().getName(), equalTo( Cookies.PAGING_COOKIE_NAME ) );
      assertThat( cookieCaptor.getValue().getValue(), equalTo( Boolean.FALSE.toString() ) );
   }

   @Test
   public void shouldFindPagingCookie() {
      assertThat( systemUnderTest.isPaging(), equalTo( false ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.PAGING_COOKIE_NAME, Boolean.TRUE.toString() ) }
      );

      assertThat( systemUnderTest.isPaging(), equalTo( true ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.PAGING_COOKIE_NAME, Boolean.FALSE.toString() ) }
      );

      assertThat( systemUnderTest.isPaging(), equalTo( false ) );
   }

   @Test
   public void shouldHandleUnexpectedResponse() {
      when( executionsHandle.getNativeResponse() ).thenReturn( new Object() );
      systemUnderTest.setPagingCookie();
   }

   @Test
   public void shouldHandleUnexpectedRequest() {
      when( executionsHandle.getNativeRequest() ).thenReturn( new Object() );
      assertThat( systemUnderTest.isPaging(), equalTo( false ) );
   }

   @Test
   public void shouldGetCookieForModelSorting() {
      assertThat( systemUnderTest.getModelColumnSorting(), equalTo( Cookies.SORTING_NATURAL ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "MODEL-descending" ) }
      );

      assertThat( systemUnderTest.getModelColumnSorting(), equalTo( Cookies.SORTING_DESCENDING ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "MODEL-ascending" ) }
      );

      assertThat( systemUnderTest.getModelColumnSorting(), equalTo( Cookies.SORTING_ASCENDING ) );
   }

   @Test
   public void shouldGetCookieForMakeSorting() {
      assertThat( systemUnderTest.getMakeColumnSorting(), equalTo( Cookies.SORTING_NATURAL ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "MAKE-descending" ) }
      );

      assertThat( systemUnderTest.getMakeColumnSorting(), equalTo( Cookies.SORTING_DESCENDING ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "MAKE-ascending" ) }
      );

      assertThat( systemUnderTest.getMakeColumnSorting(), equalTo( Cookies.SORTING_ASCENDING ) );
   }

   @Test
   public void shouldGetCookieForColourSorting() {
      assertThat( systemUnderTest.getColourColumnSorting(), equalTo( Cookies.SORTING_NATURAL ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "COLOUR-descending" ) }
      );

      assertThat( systemUnderTest.getColourColumnSorting(), equalTo( Cookies.SORTING_DESCENDING ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "COLOUR-ascending" ) }
      );

      assertThat( systemUnderTest.getColourColumnSorting(), equalTo( Cookies.SORTING_ASCENDING ) );
   }

   @Test
   public void shouldGetCookieForPriceSorting() {
      assertThat( systemUnderTest.getPriceColumnSorting(), equalTo( Cookies.SORTING_NATURAL ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "PRICE-descending" ) }
      );

      assertThat( systemUnderTest.getPriceColumnSorting(), equalTo( Cookies.SORTING_DESCENDING ) );

      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "PRICE-ascending" ) }
      );

      assertThat( systemUnderTest.getPriceColumnSorting(), equalTo( Cookies.SORTING_ASCENDING ) );
   }

   @Test
   public void shouldGetSortingKeyAndType() {
      when( request.getCookies() ).thenReturn( new Cookie[]{
            new Cookie( Cookies.SORTING_COOKIE, "PRICE-descending" ) }
      );

      assertThat( systemUnderTest.getSortingKey().get(), equalTo( Cookies.PRICE_SORTING_KEY ) );
      assertThat(
            systemUnderTest.getSortingDirection().get(),
            equalTo( Cookies.SORTING_DESCENDING )
      );
   }

   @Test
   public void shouldSupportNoCookie() {
      when( request.getCookies() ).thenReturn( new Cookie[]{} );

      assertThat( systemUnderTest.getSortingKey().isPresent(), equalTo( false ) );
      assertThat( systemUnderTest.getSortingDirection().isPresent(), equalTo( false ) );
   }

   @Test
   public void shouldConfigureSortingCookie() {
      systemUnderTest.configureSorting( Cookies.PRICE_SORTING_KEY, Cookies.SORTING_DESCENDING );
      verify( response ).addCookie( cookieCaptor.capture() );

      assertThat( cookieCaptor.getValue().getName(), equalTo( Cookies.SORTING_COOKIE ) );
      assertThat( cookieCaptor.getValue().getValue(), equalTo( "PRICE-descending" ) );
   }

}