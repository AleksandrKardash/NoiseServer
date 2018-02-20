package models.BuilderCar;

public abstract class Elements{
    private boolean floor;   //пол
    private boolean floorS;  //пол
    private int door;        //двери
    private boolean roof;    //крыша
    private int arch;        //арки
    private boolean hood;    //капот
    private boolean trunk;  //багажник

    public int getDoor() {

        return door;
    }

    public boolean getFloor() {
        return floor;
    }

    public boolean getFloorS() {
        return floorS;
    }

    public boolean getRoof() {
        return roof;
    }

    public int getArch() {
        return arch;
    }

    public boolean getHood() {
        return hood;
    }

    public boolean getTrunk() {
        return trunk;
    }

}

class ElementsPassenger extends Elements {

    private boolean floor = true; //пол
    private int door = 4;  //двери
    private boolean roof = true;  //крыша
    private int arch = 4;  //арки
    private boolean hood = true;  //капот
    private boolean trunk = true;  //багажник

    public void floorOn(){
        floor = true;
    }
    public void floorOff(){
        floor = false;
    }
    public boolean getFloor(){
        return floor;
    }

    public void doorOn(){
        door = 4;
    }
    public void doorOff(){
        door = 0;
    }
    public int getDoor(){
        return door;
    }

    public void roofOn(){
        roof = true;
    }
    public void roofOff(){
        roof = false;
    }
    public boolean getRoof(){
        return roof;
    }

    public void archOn(){
        arch = 4;
    }
    public void archOff(){
        arch = 0;
    }
    public int getArch(){
        return arch;
    }

    public void hoodOn(){
        hood = true;
    }
    public void hoodOff(){
        hood = false;
    }
    public boolean getHood(){
        return hood;
    }

    public void trunkOn(){
        trunk = true;
    }
    public void trunkOff(){
        trunk = false;
    }
    public boolean getTrunk(){
        return trunk;
    }

}

class ElementsTruck extends Elements {

    private boolean floor = true; //пол кабины
    private int door = 2;  //двери кабины
    private boolean roof = true;  //крыша кабины
    private boolean hood = true;  //капот
    private boolean trunk = true;  //грузовой отсек

    public void floorOn(){
        floor = true;
    }
    public void floorOff(){
        floor = false;
    }
    public boolean getFloor(){
        return floor;
    }

    public void doorOn(){
        door = 2;
    }
    public void doorOff(){
        door = 0;
    }
    public int getDoor(){
        return door;
    }

    public void roofOn(){
        roof = true;
    }
    public void roofOff(){
        roof = false;
    }
    public boolean getRoof(){
        return roof;
    }

    public void hoodOn(){
        hood = true;
    }
    public void hoodOff(){
        hood = false;
    }
    public boolean getHood(){
        return hood;
    }

    public void trunkOn(){
        trunk = true;
    }
    public void trunkOff(){
        trunk = false;
    }
    public boolean getTrunk(){
        return trunk;
    }

}

class ElementsBus extends Elements {

    private boolean floor = true;  //пол кабины
    private boolean floorS = true; //пол салона
    private int door = 3;          //двери
    private boolean roof = true;   //крыша
    private int arch = 4;          //арки
    private boolean hood = true;   //капот
    private boolean walls = true;  //стенки

    public void floorOn(){
        floor = true;
    }
    public void floorOff(){
        floor = false;
    }
    public boolean getFloor(){
        return floor;
    }

    public void floorSOn(){
        floorS = true;
    }
    public void floorSOff(){
        floorS = false;
    }
    public boolean getFloorS(){
        return floorS;
    }

    public void doorOn(){
        door = 3;
    }
    public void doorOff(){
        door = 0;
    }
    public int getDoor(){
        return door;
    }

    public void roofOn(){
        roof = true;
    }
    public void roofOff(){
        roof = false;
    }
    public boolean getRoof(){
        return roof;
    }

    public void archOn(){
        arch = 4;
    }
    public void archOff(){
        arch = 0;
    }
    public int getArch(){
        return arch;
    }

    public void hoodOn(){
        hood = true;
    }
    public void hoodOff(){
        hood = false;
    }
    public boolean getHood(){
        return hood;
    }

    public void wallsOn(){
        walls = true;
    }
    public void wallsOff(){
        walls = false;
    }
    public boolean getWalls(){
        return walls;
    }

}
