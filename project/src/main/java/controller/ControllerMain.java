package controller;

import Sample.MyComponentFactory;
import controller.LoginController;
import controller.LoginController1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.awt.ComponentFactory;
import view.LoginView;

public class ControllerMain extends Application {

   /* @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        //ComponentFactory c = ComponentFactory.instance();
        //LoginController loginController=new LoginController(c.getAuthenticationService());
        Controller1 controller1 = new Controller1();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        loader.setController(controller1);

    }*/

    public void start(Stage primaryStage) throws Exception {

        // just load fxml file and display it in the stage:
        MyComponentFactory myComponentFactory = MyComponentFactory.instance(false);

        LoginController1 controller1;
        controller1 = new LoginController1(myComponentFactory.getAuthenticationService());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loader.setController(controller1);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // main method to support non-JavaFX-aware environments:

    public static void main(String[] args) {
        launch(args);
    }

   /* public void start(Stage primaryStage) throws Exception {

        // just load fxml file and display it in the stage:
        MyComponentFactory myComponentFactory = MyComponentFactory.instance(false);

        EmployeeController controller1;
        controller1 = new EmployeeController(myComponentFactory.getAuthenticationService());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/employeePage.fxml"));
        loader.setController(controller1);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // main method to support non-JavaFX-aware environments:

    public static void main(String[] args) {

        launch(args);
    }*/
}