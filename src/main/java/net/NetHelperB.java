package net;

import BD.DBHandler;
import UI.controller.MainController;
import manager.DataManager;
import models.UserBuilder.User;

import java.io.*;
import java.net.*;

public class NetHelperB implements Runnable {

    //ссылка на controller для передачи данных в окно через метод setText
    public static MainController controller = null;

    @Override
    public void run() {

        ServerSocket servers = null;
        Socket       fromclient = null;
        PrintWriter    out= null;

        //создаем server socket
        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            controller.setText("Couldn't listen to port 4444");
            System.exit(-1);
        }
        //ждем подключения
        try {
            controller.setText("Waiting for a client...");
            fromclient= servers.accept();
            controller.setText("Client connected");
        } catch (IOException e) {
            controller.setText("Can't accept");
            System.exit(-1);
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(fromclient.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out = new PrintWriter(fromclient.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            User obj2 = (User) ois.readObject();
            DataManager.getInstance().addUser(obj2);
            DBHandler handler = DBHandler.getInstance();
            int reg = handler.Create(obj2);
            out.println(reg);

        }catch(Exception e){}

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
        try {
            fromclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            servers.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}