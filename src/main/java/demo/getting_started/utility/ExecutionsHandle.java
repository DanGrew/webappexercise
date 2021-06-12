package demo.getting_started.utility;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

/**
 * Wraps the {@link Executions} object to provide a friendlier, testable, interaction.
 */
public class ExecutionsHandle {

   /**
    * Retrieves the parameter associated with the given parameter name.
    * @param parameter to retieve.
    * @return the parameter if one is found, otherwise null.
    */
   public String retrieveParameter( String parameter ) {
      return Executions.getCurrent().getParameter( parameter );
   }

   /**
    * See {@link Executions#getCurrent()} and {@link Execution#getNativeResponse()}.
    * @return the native response.
    */
   public Object getNativeResponse() {
      return Executions.getCurrent().getNativeResponse();
   }

   /**
    * See {@link Executions#getCurrent()} and {@link Execution#getNativeRequest()}.
    * @return the native request.
    */
   public Object getNativeRequest() {
      return Executions.getCurrent().getNativeRequest();
   }
}
