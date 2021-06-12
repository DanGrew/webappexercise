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

package demo.getting_started.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static demo.getting_started.beans.ApplicationBean.CAR_SERVICE;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class BeanResolverTest {

   private BeanResolver systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new BeanResolver();
   }

   @Test
   public void shouldProvideSingletonCarService() {
      assertThat( systemUnderTest.resolveVariable( CAR_SERVICE.beanName() ), notNullValue() );
      assertThat(
            systemUnderTest.resolveVariable( CAR_SERVICE.beanName() ),
            equalTo( systemUnderTest.resolveVariable( CAR_SERVICE.beanName() ) )
      );
   }
   
   @Test
   public void shouldHandleInvalidBean() {
      assertThat( systemUnderTest.resolveVariable( "anything" ), nullValue() );
   }

}