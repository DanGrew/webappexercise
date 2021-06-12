package demo.getting_started.beans;

import demo.getting_started.model.services.CarService;
import demo.getting_started.model.services.CarServiceImpl;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the resolution of beans within the system. Note similar to
 * {@link ApplicationBean}, this is possibly redundant with an appropriate framework to manage in
 * a better way.
 */
public class BeanResolver implements VariableResolver {

   //this is wrong but i dont know how to get it to create a singleton - not using spring scopes

   private static final Map< String, Object > beans = new HashMap<>();

   static {
      beans.put( ApplicationBean.CAR_SERVICE.beanName(), new CarServiceImpl() );
   }

   @Override
   public Object resolveVariable( String beanName ) throws XelException {
      return beans.get( beanName );
   }
}
