package demo.getting_started.utility;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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

   /**
    * Presents a question to the user to direct their interaction and focus. This supplies the 
    * user with an ok and cancel option.
    * @param message to present.
    * @param title   of the question.
    */
   public void question( String message, String title, EventListener< Event > eventListener ) {
      Messagebox.show(
            message,
            title,
            Messagebox.OK | Messagebox.CANCEL,
            Messagebox.INFORMATION,
            eventListener
      );
   }
}
