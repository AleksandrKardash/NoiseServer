package net;

import UI.controller.MainController;
import models.UserBuilder.User;

import java.io.*;
import java.net.*;

public class NetHelper implements Runnable {

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter    out= null;
        ServerSocket servers = null;
        Socket       fromclient = null;

        //создаем server socket
        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }
        //ждем подключения
        try {
            System.out.print("Waiting for a client...");
            fromclient= servers.accept();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        }

        try {
            in  = new BufferedReader(new
                    InputStreamReader(fromclient.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input,output;

        try {
            out = new PrintWriter(fromclient.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //принимаем строки, печатаем в консоль и отправляем клиенту
        try {
            while ((input = in.readLine()) != null) {

                if (input.equalsIgnoreCase("close")) break;
                if (input.equalsIgnoreCase("exit")) break;

                out.println("S ::: "+input);
                System.out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //все закрываем
        out.close();
        try {
            in.close();
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