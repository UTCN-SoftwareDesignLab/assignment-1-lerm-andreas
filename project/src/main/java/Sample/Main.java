package Sample;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.awt.ComponentFactory;
import view.LoginView;

public class Main extends Application {
    ComponentFactory componentFactory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{



        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    //   componentFactory = new ComponentFactory.instance(false);
    //    new LoginController(componentFactory.getAuthenticationService());

    }


}