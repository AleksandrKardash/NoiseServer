package net;

import BD.DBHandler;
import controller.MainController;
import models.BuilderCar.Car;
import models.UserBuilder.User;

import java.io.*;
import java.net.*;
import java.util.*;

//сервер работает в многопоточном режиме. Для каждого подключившегося клиента создается новый поток
public class Communication implements Runnable  {

    static int numberOfOnline;
    //ссылка на controller для передачи данных в окно через метод setText
    public static MainController controller = null;

    @Override
    public void run() {
        try {
            ServerSocket server = null;
            Socket client = null;
            try {
                server = new ServerSocket(4444);
                controller.setText("Waiting...");
                numberOfOnline = 0;
                // Сервер ждет подключения клиентов в бесконечном цикле и каждому подключившемуся клиенту создает
                // свой поток, что позволяет подключаться к серверу более чем 1му потоку одновременно
                while(true) {
                    client = server.accept(); //Ожидает подключение клиента
                    numberOfOnline++; // Увеличивается счетчик активных клиентов
                    controller.setText("One more client has been connected");
                    controller.setText("There are " + Communication.numberOfOnline + " clients online");
                    Runnable r = new ThreadEchoHandler(client);
                    Thread t = new Thread(r);
                    t.start();
                }
            } //Закрываем сокеты
            finally {
                if (client != null) {
                    client.close();
                }
                if (server != null) {
                    server.close();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//Класс реализует интерфейс Runnable и в свое методе run() поддерживает взаимодействие с программой клиентом
class ThreadEchoHandler implements Runnable {
    ThreadEchoHandler(Socket st) {
        client = st;
    }

    private Socket client;
    private MyRequest request;
    private Object obj = null;
    private DBHandler handler = null;

    public void run() {
        try {

            //Создаем входной поток сервера
            BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
            ObjectInputStream ois = new ObjectInputStream(bis);
            //Создаем выходной поток сервера
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            try{
                //читаем обьект MyRequest в цикле
                while ((obj = ois.readObject())!=null) {

                    MyRequest requestB = (MyRequest) obj;

                    // получаем тип запроса, а потом в зависимости от типа запроса отправляем в нужный метод DBHandler
                    switch (requestB.getRequestType()) {

                        case CREATE:

                            handler = DBHandler.getInstance();
                            //отправляем обьект MyRequest в нужный метод DBHandler и получаем ответ
                            request = handler.Create(requestB);
                            //записываем и передаем ответ в виде обьекта MyRequest
                            oos.writeObject(request);
                            oos.flush();

                            break;

                        case READ:

                            handler = DBHandler.getInstance();
                            //отправляем обьект MyRequest в нужный метод DBHandler и получаем ответ
                            request = handler.Read(requestB);
                            //записываем и передаем ответ в виде обьекта MyRequest
                            oos.writeObject(request);
                            oos.flush();

                            break;

                        case UPDATE:

                            handler = DBHandler.getInstance();
                            //отправляем обьект MyRequest в нужный метод DBHandler и получаем ответ
                            request = handler.Update(requestB);
                            //записываем и передаем ответ в виде обьекта MyRequest
                            oos.writeObject(request);
                            oos.flush();

                           break;

                        case DELETE:

                            handler = DBHandler.getInstance();
                            //отправляем обьект MyRequest в нужный метод DBHandler и получаем ответ
                            request = handler.Delete(requestB);
                            //записываем и передаем ответ в виде обьекта MyRequest
                            oos.writeObject(request);
                            oos.flush();

                            break;
                    }
                }

            }catch(Exception ignored){}
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            client.close();
            Communication.numberOfOnline--; // уменьшает число клиентов онлайн при отсоединении клиента
            Communication.controller.setText("There are " + Communication.numberOfOnline + " clients online");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}