package net;

import UI.controller.MainController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultiThreadServerB implements Runnable {

    //ссылка на controller для передачи данных в окно через метод setText
    public static MainController controller = null;

    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    @Override
    public void run() {


        // стартуем сервер на порту 4444 и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket server = new ServerSocket(4444);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            controller.setText("Server socket created, command console reader for listen to server commands");

            // стартуем цикл при условии что серверный сокет не закрыт
            while (!server.isClosed()) {

                // проверяем поступившие комманды из консоли сервера если такие были
                if (br.ready()) {
                    controller.setText("Main Server found any messages in channel, let's look at them.");

                    // если команда - quit то инициализируем закрытие сервера и выход из цикла раздачии нитей монопоточных серверов
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        controller.setText("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                // если комманд от сервера нет то становимся в ожидание подключения к сокету общения под именем - "clientDialog" на серверной стороне
                Socket client = server.accept();

                // после получения запроса на подключение сервер создаёт сокет  для общения с клиентом и отправляет его в отдельную нить
                //в Runnable(при необходимости можно создать Callable) монопоточную нить = сервер - MonoThreadClientHandler и тот продолжает общение от лица сервера
                executeIt.execute(new MonoThreadClientHandlerB(client));
                controller.setText("Connection accepted.");
            }

            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
