package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import net.Communication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private TextField console;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //запускаем паралельный поток для соединения с клиентом
    @FXML
    void OnServer(ActionEvent event) throws IOException {

        Runnable connect = new Communication();
        Thread conn = new Thread(connect);
        conn.start();
    }

    @FXML
    void OffServer(ActionEvent event) {
        System.exit(0);
    }

    public void setText(String a){
        console.setText(a);
    }

}
