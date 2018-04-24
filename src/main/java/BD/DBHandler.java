package BD;

import models.Table.SellerMaterialTable;
import models.UserBuilder.User;
import net.MyRequest;

import java.sql.*;
import java.util.ArrayList;

// соединение с базой (Singltone)
public class DBHandler extends Configs {

    private static DBHandler instance;

    private DBHandler() {
    }

    public static DBHandler getInstance() {

        if (instance == null)
            instance = new DBHandler();

        return instance;
    }

    private Connection dbconnection;
    private PreparedStatement pst;
    private MyRequest request;
    private String q1;
    private String login;
    private String password;

    //подключение
    public Connection getConnection() {
        //путь к БД
        String connectionString = "jdbc:mysql://" + Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname + "?autoReconnect=true&useSSL=false";
        //загрузка драйвера
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //получаем Connection
        try {
            dbconnection = DriverManager.getConnection(connectionString, Configs.dbuser, Configs.dbpass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbconnection;
    }


    public MyRequest Create(MyRequest r) {
        //создаем соединение
        DBHandler handler = DBHandler.getInstance();
        Connection connection = handler.getConnection();
        int count;
        String q1;
        String insert;

        //проверяем тип обьекта
        switch (r.getRequestTypeB()){

            case USER:

                //читаем из реквеста обьект и приводим к нужному классу
                User user = (User) r.getData();

                //формируем запросы к БД
                count = 0;
                q1 = "SELECT * from users where login=?";
                insert = "INSERT INTO /**noise.**/users(names,city,adress,mail,phone,login,password)"
                        + "VALUES (?,?,?,?,?,?,?)";

                //если User был успешно создан в программе(с учетом проверки данных), проверям на повтор логина
                if (user != null) {
                    try {
                        pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);
                        pst.setString(1, user.getLogin());
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            count++;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                //загружаем user в БД
                if (count == 0 && user != null){

                    try {
                        pst = connection.prepareStatement(insert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        pst.setString(1, user.getName());
                        pst.setString(2, user.getCity());
                        pst.setString(3, user.getAdress());
                        pst.setString(4, user.getMail());
                        pst.setString(5, user.getPhone());
                        pst.setString(6, user.getLogin());
                        pst.setString(7, user.getPassword());

                        pst.executeUpdate();


                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    count=2;

                }
                //запаковываем ответ в MyRequest
                request = new MyRequest(MyRequest.RequestType.ANSWER,MyRequest.RequestTypeB.INT, count);

                break;


            case LIST_NEW_PRODUCT:

                //читаем из реквеста обьект и приводим к нужному классу
                ArrayList list = (ArrayList) r.getData();

                //формируем запросы к БД
                count = 1;
                insert = "INSERT INTO /**noise.**/product(manufactured, type, name, secondName, area, depth, classMat, cost, owner)"
                        + "VALUES (?,?,?,?,?,?,?,?,?)";

                //загружаем product в БД
                try {
                    pst = connection.prepareStatement(insert);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    pst.setString(1, (String) list.get(0));
                    pst.setString(2, (String) list.get(1));
                    pst.setString(3, (String) list.get(2));
                    pst.setString(4, (String) list.get(3));
                    pst.setString(5, (String) list.get(4));
                    pst.setString(6, (String) list.get(5));
                    pst.setString(7, (String) list.get(6));
                    pst.setString(8, (String) list.get(7));
                    pst.setString(9, (String) list.get(8));

                    pst.executeUpdate();

                    count = 0;

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //запаковываем ответ в MyRequest
                request = new MyRequest(MyRequest.RequestType.ANSWER,MyRequest.RequestTypeB.INT, count);

                break;

        }

        //возвращаем значение count для определения результата операции
        return request;
    }
    public MyRequest Read(MyRequest r) {

        //создаем соединение
        DBHandler handler = DBHandler.getInstance();
        Connection connection = handler.getConnection();

        ArrayList list;
        int count;

        //проверяем тип обьекта
        switch (r.getRequestTypeB()){

            case LIST_SIGN_IN:

                //читаем из реквеста обьект и приводим к нужному классу
                list = (ArrayList) r.getData();
                login = (String) list.get(0);
                password = (String) list.get(1);

                //формируем запросы к БД и проверяем соответствие логина и пароля
                count = 0;
                User user;
                q1 = "SELECT * from users where login=? and password=?";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    pst.setString(1, login);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();


                    while (rs.next()) {
                        count++;
                    }

                    if (count == 1) {
                        //вернуться к найденой записи с логином и паролем  и загрузить данные User в ответ
                        rs.previous();
                        user =  new User.Builder()
                                .setName(rs.getString(2))
                                .setCity(rs.getString(3))
                                .setAdress(rs.getString(4))
                                .setMail(rs.getString(5))
                                .setPhone(rs.getString(6))
                                .setLogin(rs.getString(7))
                                .setPassword(rs.getString(8))
                                .setSeller(rs.getString(9))
                                .build();

                        request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.USER, user);

                    //в случае отсутствия совпадений логин/пароль
                    } else {
                        request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.USER, null);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                    //закрываем соединение с БД
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case LIST_MY_PRODUCT:

                //читаем из реквеста обьект и приводим к нужному классу
                list = (ArrayList) r.getData();
                login = (String) list.get(0);

                ArrayList list2 = new ArrayList<>();

                //формируем запросы к БД
                q1 = "SELECT * from product where owner=?";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    pst.setString(1, login);
                    ResultSet rs = pst.executeQuery();

                    //в цикле достаем из ответа строки и записываем их в коллекцию в виде обьектов SellerMaterialTable
                    while (rs.next()) {
                        list2.add( new SellerMaterialTable(rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5), Double.parseDouble(rs.getString(6)), Double.parseDouble(rs.getString(7)),
                                rs.getString(8), Double.parseDouble(rs.getString(9))));
                    }

                    //записываем ответ
                    request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.LIST_MY_PRODUCT, list2);

                } catch (SQLException e) {
                    e.printStackTrace();
                  //закрываем соединение с БД
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                break;

        }
        return request;
    }
    public MyRequest Update(MyRequest r){
        return request;
    }
    public MyRequest Delete(MyRequest r) {
        return request;
    }

}