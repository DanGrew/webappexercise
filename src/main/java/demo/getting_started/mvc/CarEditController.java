package demo.getting_started.mvc;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

public class CarEditController extends SelectorComposer< Component > {

   @Listen( "onClick = #testMeButton" )
   public void testMe() {
      System.out.println( "You tested me good!" );
   }
   
   @Listen( "onClick = #returnToDemoButton" )
   public void returnToDemo(){
      Executions.sendRedirect( ApplicationPages.DEMO_PAGE.pageName() );
   }
}
