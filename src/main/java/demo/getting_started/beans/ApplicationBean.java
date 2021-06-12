package demo.getting_started.beans;

/**
 * Defines the beans in the system that can be injected/wired. Note that this is most likely
 * redundant when using the right or known framework such as spring.
 */
public enum ApplicationBean {

   CAR_SERVICE( "carService" );

   private final String beanName;

   /**
    * Constructs a new {@link ApplicationBean}.
    * @param beanName name of the bean that can be used in annotations.
    */
   private ApplicationBean( String beanName ) {
      this.beanName = beanName;
   }

   public String beanName() {
      return beanName;
   }
}
