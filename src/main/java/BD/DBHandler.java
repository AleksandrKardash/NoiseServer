package BD;

import models.Table.MaterialTable;
import models.Table.SellerMaterialTable;
import models.Table.SellerOrders;
import models.Table.SellerTable;
import models.UserBuilder.User;
import net.MyRequest;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;

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
    private DBHandler handler;
    private Connection connection;
    private String q1;
    private String q2;
    private int count;

    //подключение
    private Connection getConnection() {
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
        handler = DBHandler.getInstance();
        connection = handler.getConnection();

        //проверяем тип обьекта
        switch (r.getRequestTypeB()){

            case USER:

                //читаем из реквеста обьект и приводим к нужному классу
                User user = (User) r.getData();

                //формируем запросы к БД
                count = 0;
                q1 = "SELECT * from users where login=?";
                q2 = "INSERT INTO /**noise.**/users(names,city,adress,mail,phone,login,password)"
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
                        pst = connection.prepareStatement(q2);
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
                int a = 0;
                q1 = "SELECT idsellers FROM noise.sellers WHERE idusers IN (SELECT idusers FROM noise.users WHERE login=?)";
                q2 = "INSERT INTO /**noise.**/product(manufactured, type, name, secondName, area, depth, classMat, cost, owner, idsellers)"
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);
                    pst.setString(1, (String) list.get(8));
                    ResultSet rs = pst.executeQuery();
                    rs.next();
                    a = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
               
                //загружаем product в БД
                try {
                    pst = connection.prepareStatement(q2);
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
                    pst.setInt(10, a);

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

            case ORDER:

                //читаем из реквеста обьект и приводим к нужному классу
                SellerTable seller = (SellerTable) r.getData();
                User customer = (User) r.getDataB();
                ArrayList<MaterialTable> listProduct = seller.getListProduct();

                //формируем запросы к БД
                count = 1;
                int id = 0;
                q2 = "INSERT INTO /**noise.**/orders(customer_login, order_date, amount, seller_name)"
                        + "VALUES (?,?,?,?)";

                //загружаем order в БД
                try {
                    //записываем данные и получаем ID созданной строки
                    pst = connection.prepareStatement(q2, Statement.RETURN_GENERATED_KEYS);

                    pst.setString(1, customer.getLogin());
                    pst.setDate(2,  new Date(new java.util.Date().getTime()));
                    pst.setDouble(3, seller.getCost());
                    pst.setString(4, seller.getSeller());

                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next()){
                        id=rs.getInt(1);
                    }


                    //записываем материалы относящиеся к только созданному заказу
                    for (MaterialTable i: listProduct) {
                        q2 = "INSERT INTO /**noise.**/items_order(orders_id, name, area, depth, count)"
                                + "VALUES (?,?,?,?,?)";

                        pst = connection.prepareStatement(q2);

                        pst.setInt(1, id);
                        pst.setString(2, i.getNameMat());
                        pst.setDouble(3, i.getAreaMat());
                        pst.setDouble(4, i.getDepthMat());
                        pst.setDouble(5, i.getCountMat());

                        pst.executeUpdate();

                    }

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
        handler = DBHandler.getInstance();
        connection = handler.getConnection();

        ArrayList list;
        ArrayList listAnswer;
        String login;
        String password;

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

                listAnswer = new ArrayList<>();

                //формируем запросы к БД
                q1 = "SELECT * from product where owner=?";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    pst.setString(1, login);
                    ResultSet rs = pst.executeQuery();

                    //в цикле достаем из ответа строки и записываем их в коллекцию в виде обьектов SellerMaterialTable
                    while (rs.next()) {

                        listAnswer.add( new SellerMaterialTable(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5), Double.parseDouble(rs.getString(6)), Double.parseDouble(rs.getString(7)),
                                rs.getString(8), Double.parseDouble(rs.getString(9))));

                    }

                    //записываем ответ
                    request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.LIST_MY_PRODUCT, listAnswer);

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

            case LIST_FIND_SELLER:

                //читаем из реквеста обьект и приводим к нужному классу
                list = (ArrayList) r.getData();
                ArrayList<MaterialTable> listB = (ArrayList<MaterialTable>) r.getDataB();
                String city = (String) list.get(0);
                String grade = (String) list.get(1);

                //коллекци для записи ответа
                listAnswer = new ArrayList<SellerTable>();
                ArrayList<MaterialTable> listChoiseProduct;

                //формируем запросы к БД
                q1 = "SELECT * from sellers where city=?";
                q2 = "SELECT * from product where idsellers=?";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    pst.setString(1, city);
                    ResultSet rs = pst.executeQuery();

                    //в цикле подбираем из ответа подходящих продавцов и записываем их в коллекцию в виде обьектов SellerTable
                    while (rs.next()) {

                        //создаем новую коллекцию материалов для каждого продавца
                        listChoiseProduct = new ArrayList<>();

                        //получаем все материалы, принадлежащие текущему продавцу
                        String idSeller = rs.getString(1);
                        pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q2);
                        pst.setString(1, idSeller);
                        ResultSet rsB = pst.executeQuery();

                        //переменные для рассчета стоимости
                        Double value = 0.0;
                        Double cost;

                        //проходим по каждому материалу текущего продавца
                        while (rsB.next()) {

                            cost = 0.0;
                            ArrayList<MaterialTable> cloneListB = new ArrayList<MaterialTable>();
                            for (MaterialTable i: listB) {cloneListB.add(new MaterialTable(i.getNameMat(), i.getAreaMat(), i.getDepthMat(), i.getCountMat()));}

                            //сравниваем каждый материал из присланного списка с текущим материалом из базы
                            for (int i = 0; i < cloneListB.size(); i++){

                                if (cloneListB.get(i).getNameMat().equals(rsB.getString("type")) &&
                                  cloneListB.get(i).getDepthMat()==Math.floor(Double.parseDouble(rsB.getString("depth"))) &&
                                  grade.equals(rsB.getString("classMat"))){

                                    //считаем стоимость
                                    cost = cloneListB.get(i).getCountMat() * Double.parseDouble(rsB.getString("cost"));

                                    //записываем подобранный материал в коллекцию
                                    listChoiseProduct.add(new MaterialTable(rsB.getString("type") + " " + rsB.getString("manufactured") + " " +
                                            rsB.getString("name") + " " + rsB.getString("secondName"), Double.parseDouble(rsB.getString("area")),
                                            Double.parseDouble(rsB.getString("depth")), listB.get(i).getCountMat()));

                                    //помечаем уже найденный материал, исключая его из подбора
                                    cloneListB.get(i).setNameMat("delete");

                                }
                            }

                            //увеличиваем стоимость на сумму каждой подобранной позиции
                            value += cost;
                        }

                        //заполняем данными SellerTable
                        //клонируем коллекцию с подобранными материалами и обнуляем ее для дальнейшей работы
                        listAnswer.add( new SellerTable(rs.getString(2), rs.getString(6), value,
                                rs.getString(4), listChoiseProduct));
                    }

                    //записываем ответ
                    request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.LIST_FIND_SELLER, listAnswer);

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

            case LIST_MY_ORDERS:

                //читаем из реквеста обьект и приводим к нужному классу
                list = (ArrayList) r.getData();
                login = (String) list.get(0);

                listAnswer = new ArrayList<>();
                ArrayList<MaterialTable> listProductOrder;

                //формируем запросы к БД
                q1 = "SELECT idorders, customer_login, order_date, amount, processed, adress, phone FROM noise.orders\n" +
                        "JOIN noise.users  ON noise.orders.customer_login=noise.users.login\n" +
                        "WHERE seller_name IN (SELECT name FROM noise.sellers \n" +
                        "WHERE idusers IN (SELECT idusers FROM noise.users \n" +
                        "WHERE login=?));";
                q2 = "SELECT * FROM noise.items_order\n" +
                        "WHERE orders_id=?;";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    pst.setString(1, login);
                    ResultSet rs = pst.executeQuery();


                    //в цикле достаем из ответа строки и записываем их в коллекцию в виде обьектов SellerOrder
                    while (rs.next()) {

                        //создаем новую коллекцию материалов для каждого продавца
                        listProductOrder = new ArrayList<MaterialTable>();

                        listAnswer.add( new SellerOrders(rs.getInt("idorders"), rs.getString("adress"), rs.getString("phone"),
                                rs.getDouble("amount"), rs.getDate("order_date"), rs.getString("processed"), listProductOrder));


                        //получаем все материалы, принадлежащие текущему продавцу
                        String idOrder = rs.getString(1);
                        pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q2);
                        pst.setString(1, idOrder);
                        ResultSet rsB = pst.executeQuery();


                        //для каждого заказа записываем список материалов
                        while (rsB.next()) {

                            listProductOrder.add(new MaterialTable(rsB.getString("name"), rsB.getDouble("area"),
                                    rsB.getDouble("depth"), rsB.getDouble("count")));

                        }

                    }

                    //записываем ответ
                    request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.LIST_MY_ORDERS, listAnswer);

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

            case LIST_CITY:

                listAnswer = new ArrayList<String>();

                //формируем запросы к БД
                q1 = "SELECT DISTINCT city FROM sellers";

                try {
                    pst = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(q1);

                    ResultSet rs = pst.executeQuery();

                    //в цикле достаем из ответа строки и записываем их в коллекцию в виде обьектов SellerMaterialTable
                    while (rs.next()) {

                        listAnswer.add(rs.getString("city"));

                    }

                    //записываем ответ
                    request = new MyRequest(MyRequest.RequestType.ANSWER, MyRequest.RequestTypeB.LIST_CITY, listAnswer);

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

        //создаем соединение
        handler = DBHandler.getInstance();
        connection = handler.getConnection();

        //проверяем тип обьекта
        switch (r.getRequestTypeB()) {

            case CONFIRM_ORDER:

                //читаем из реквеста обьект и приводим к нужному классу
                SellerOrders confirmOrder = (SellerOrders) r.getData();

                //формируем запросы к БД
                count = 1;
                q1 = "UPDATE orders SET processed = 'yes' WHERE idorders = ?;";

                //загружаем product в БД
                try {
                    pst = connection.prepareStatement(q1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    pst.setInt(1, confirmOrder.getIdOrder());

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
        return request;
    }
    public MyRequest Delete(MyRequest r) {

        //создаем соединение
        DBHandler handler = DBHandler.getInstance();
        Connection connection = handler.getConnection();

        //проверяем тип обьекта
        switch (r.getRequestTypeB()) {

            case DELETE_ORDER:

                //читаем из реквеста обьект и приводим к нужному классу
                SellerOrders deleteOrders = (SellerOrders) r.getData();

                //формируем запросы к БД
                count = 1;
                q1 = "DELETE FROM orders WHERE idorders = ?;";

                //загружаем product в БД
                try {
                    pst = connection.prepareStatement(q1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    pst.setInt(1, deleteOrders.getIdOrder());

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

            case DELETE_PRODUCT:

                //читаем из реквеста обьект и приводим к нужному классу
                SellerMaterialTable deleteProduct = (SellerMaterialTable) r.getData();

                //формируем запросы к БД
                count = 1;
                q1 = "DELETE FROM product WHERE idproduct = ? ;";

                //загружаем product в БД
                try {
                    pst = connection.prepareStatement(q1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    pst.setInt(1, deleteProduct.getIdproduct());

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
        return request;
    }
}