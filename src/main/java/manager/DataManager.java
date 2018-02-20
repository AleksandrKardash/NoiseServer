package manager;

import models.BuilderCar.Car;
import models.UserBuilder.User;

//singlton сделать в этом классе!!!!
//использовать builder для заполнения моделей
// пользовательский ввод через паттерн Слушатель

//хранит экзэмпляры классов
public class DataManager {

    private static DataManager instance;

    private DataManager(){
    };

    public static DataManager getInstance() {

        if(instance==null)
            instance = new DataManager();

        return instance;
    }

//заменить на коллекции
public User user;
public Car car;

    public void addUser(User u){

        user = u;

    }

    public void addCar(Car c){

        car = c;

    }

}