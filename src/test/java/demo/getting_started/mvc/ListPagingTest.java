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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;

import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class ListPagingTest {
   
   @Mock private Listbox carListBox;
   @Mock private Checkbox checkbox;
   
   @Mock private Cookies cookies;
   private ListPaging systemUnderTest;
   
   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new ListPaging(cookies);
   }
   
   @Test
   public void shouldChangeMoldToPagingAfterCompose() {
      when(cookies.isPaging()).thenReturn( true );
      
      systemUnderTest.configurePagingComponentsAfterCompose( carListBox, checkbox );
      verify( carListBox ).setMold( ListPaging.PAGING_ATTRIBUTE );
      verify( checkbox ).setChecked( true );
   }
   
   @Test
   public void shouldChangeMoldToDefaultAfterCompose() {
      when(cookies.isPaging()).thenReturn( false );

      systemUnderTest.configurePagingComponentsAfterCompose( carListBox, checkbox );
      verify( carListBox ).setMold( ListPaging.DEFAULT_ATTRIBUTE );
      verify( checkbox ).setChecked( false );
   }
   
   @Test
   public void shouldChangeMoldOnCheck() {
      when(checkbox.isChecked()).thenReturn( true );

      systemUnderTest.configurePagingInResponseToCheck( carListBox, checkbox );
      verify( carListBox ).setMold( ListPaging.PAGING_ATTRIBUTE );
      verify( carListBox ).setPageSize( 5 );
      verify( cookies ).configurePagingCookie( true );
   }
   
   @Test
   public void shouldChangeMoldAndPageSizeOnCheck() {
      when(checkbox.isChecked()).thenReturn( false );

      systemUnderTest.configurePagingInResponseToCheck( carListBox, checkbox );
      verify( carListBox ).setMold( ListPaging.DEFAULT_ATTRIBUTE );
      verify( carListBox, never() ).setPageSize( anyInt() );
      verify( cookies ).configurePagingCookie( false );
   }

}