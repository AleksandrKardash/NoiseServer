package models.BuilderCar;


public class Car {
    private final String brand;
    private final String model;
    private final TypeAuto type;
    private final ClassAuto c;
    private final Elements elements;

    public Car(String brand, String model, TypeAuto type, ClassAuto c,
               Elements elements) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.c = c;
        this.elements = elements;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public TypeAuto getType() {
        return type;
    }

    public ClassAuto getC() {
        return c;
    }

    public Elements getElements() {
        return elements;
    }

    public String printInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Manufactured: " + brand + "\n");
        info.append("Model: " + model + "\n");
        info.append("Type: " + type+ "\n");
        info.append("Class: " + getC()+ "\n");
        info.append("Elemets: " + elements.getDoor() +  elements.getFloor() + elements.getRoof() + elements.getArch() + elements.getHood() + elements.getTrunk() + "\n");
        return info.toString();
    }

}