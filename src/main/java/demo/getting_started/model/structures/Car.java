package demo.getting_started.model.structures;

public class Car {

   private Integer id;
   private String model;
   private String make;
   private String preview;
   private String description;
   private Integer price;
   private SortableColour colour;

   public Car() {
      this(null, null, null, null, null, null, null);
   }

   public Car(Integer id, String model, String make, String description, String preview, Integer price, SortableColour colour) {
      this.id = id;
      this.model = model;
      this.make = make;
      this.preview = preview;
      this.description = description;
      this.price = price;
      this.colour = colour;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getMake() {
      return make;
   }

   public void setMake(String make) {
      this.make = make;
   }

   public String getPreview() {
      return preview;
   }

   public void setPreview(String preview) {
      this.preview = preview;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Integer getPrice() {
      return price;
   }

   public void setPrice(Integer price) {
      this.price = price;
   }

   public String getModel() {
      return model;
   }

   public void setModel(String model) {
      this.model = model;
   }

   public SortableColour getColour() {
      return colour;
   }

   public void setColour(SortableColour colour) {
      this.colour = colour;
   }
}
