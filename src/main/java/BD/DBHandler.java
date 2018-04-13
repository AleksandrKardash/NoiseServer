package BD;

import manager.DataManager;
import models.UserBuilder.User;
import net.MyRequest;

import java.sql.*;

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

    Connection dbconnection;
    PreparedStatement pst;

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


    //суть фасада, использовать обобщения с switch внутри
    public int Create(User user) {
        //создаем соединение и формируем запросы к БД
        DataManager manager = DataManager.getInstance();
        DBHandler handler = DBHandler.getInstance();
        Connection connection = handler.getConnection();
        int count = 0;

        String q1 = "SELECT * from users where login=?";
        String insert = "INSERT INTO /**noise.**/users(names,city,adress,mail,phone,login,password)"
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
        //возвращаем значение count для определения результата операции
        return count;
    }
    public MyRequest Read(String login, String password) {

        MyRequest request = null;
        int count = 0;
        DBHandler handler = DBHandler.getInstance();
        User user;

        //Проверка соответствия логина и пароля
        Connection connection = handler.getConnection();
        String q1 = "SELECT * from users where login=? and password=?";


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
                        .build();
                request = new MyRequest(MyRequest.RequestType.USER, user);

            //в случае отсутствия совпадений логин/пароль
            } else {
                request = new MyRequest(MyRequest.RequestType.USER, null);
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

            return request;
}
    public boolean Update(User user){
        return true;
    }
    public boolean Delete(User user) {
        return true;
    }

}