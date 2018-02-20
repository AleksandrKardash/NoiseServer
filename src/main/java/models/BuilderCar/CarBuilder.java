package models.BuilderCar;


public class CarBuilder implements Builder {

    private String brand;
    private String model;
    private TypeAuto type;
    private ClassAuto c;
    private Elements elements;

    public void setBrandAuto(String brand){
        this.brand = brand;
    }
    public void setModelAuto(String model){
        this.model = model;
    }
    public void setTypeAuto(TypeAuto type){
        this.type = type;
    }
    public void setClassAuto(ClassAuto c){
        this.c = c;
    }
    public void setElements(Elements elements){
        this.elements = elements;
    }



    public Car getResult() {
        return new Car(brand, model, type, c, elements);
    }
}
