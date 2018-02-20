package BD;

import manager.DataManager;
import models.UserBuilder.User;

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
    public boolean Read(User user){
        return true;
    }
    public boolean Update(User user){
        return true;
    }
    public boolean Delete(User user) {
        return true;
    }

}