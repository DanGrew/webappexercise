package demo.getting_started.utility;

import org.zkoss.zul.Messagebox;

/**
 * Encapsulates the method of raising messages to the user.
 */
public class Messages {

   /**
    * Presents information to the user to direct their interaction and focus but not concern.
    * @param message to present.
    * @param title   of the information message.
    */
   public void information( String message, String title ) {
      Messagebox.show(
            message,
            title,
            Messagebox.OK,
            Messagebox.INFORMATION
      );
   }
}
