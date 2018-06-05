package noise;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.Communication;

public class NoiseServer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //для получения далее ссылки на контроллер создаем обьект FMXLoader, а не используем статический метод
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Noise Server");
        stage.setScene(new Scene(root, 300,350));
        //получение ссылки на контроллер для доступа к его методам!
        Communication.controller = fxmlLoader.getController();
        stage.show();

    }

    public static void main(String[] args) throws Exception {

        launch(args);

    }

}





//package noise;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//
//    public static void main(String[] args) throws Exception {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        String fxmlFile = "/fxml/hello.fxml";
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
//        stage.setTitle("JavaFX and Maven");
//        stage.setScene(new Scene(root));
//        stage.show();
//    }
//}