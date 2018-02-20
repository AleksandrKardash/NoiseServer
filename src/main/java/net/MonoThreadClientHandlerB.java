package net;

import UI.controller.MainController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandlerB implements Runnable {

    //ссылка на controller для передачи данных в окно через метод setText
    public static MainController controller = null;

    private static Socket clientDialog;


    //конструктор
    public MonoThreadClientHandlerB(Socket client) {
        MonoThreadClientHandlerB.clientDialog = client;
    }

    @Override
    public void run() {

        try {
            // инициируем каналы общения в сокете, для сервера

            // канал записи в сокет
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            // канал чтения из сокета
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
//            controller.setText("DataInputStream created");
           // controller.setText("DataOutputStream  created");

            // основная рабочая часть //

            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт клиентом
            while (!clientDialog.isClosed()) {
                //controller.setText("Server reading from channel");

                // серверная нить ждёт в канале чтения (inputstream) получения
                // данных клиента после получения данных считывает их
                String entry = in.readUTF();

                // и выводит в окно
                //controller.setText("READ from clientDialog message - " + entry);

                //инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову - quit в любом регистре
                if (entry.equalsIgnoreCase("quit")) {
                 //   controller.setText("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + entry + " - OK");
                    Thread.sleep(3000);
                    break;
                }

                // отправляем эхо обратно клиенту
               // controller.setText("Server try writing to channel");
                out.writeUTF("Server reply - " + entry + " - OK");
              //  controller.setText("Server Wrote message to clientDialog.");

                // освобождаем буфер сетевых сообщений
                out.flush();
                // возвращаемся в началло для считывания нового сообщения
            }

            // если условие выхода - верно выключаем соединения
           // controller.setText("Client disconnected. Closing connections & channels.");

            // закрываем сначала каналы сокета, потом закрываем сокет общения с клиентом в нити моносервера!
            in.close();
            out.close();
            clientDialog.close();
           // controller.setText("Closing connections & channels - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}