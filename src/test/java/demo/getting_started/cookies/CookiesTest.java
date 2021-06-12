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
}