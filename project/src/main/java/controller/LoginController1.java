package controller;



import Sample.EmployeeConnectionFactory;
import Sample.MyComponentFactory;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import model.User;
import model.validation.Notification;
import repository.user.AuthenticationException;
import service.user.AuthenticationService;
import view.LoginView;

import java.io.IOException;

public class LoginController1 {

    private final AuthenticationService authenticationService;

    public LoginController1(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    Button logInButton;

    @FXML
    Button registerButton;

    @FXML
    TextField userNameText;

    @FXML
    TextField passwordText;

    public String getUserName(){
        System.out.println(userNameText.getText());
        return userNameText.getText();
    }

   /* public String getPassword(){
        System.out.println(passwordText.getText());
        return passwordText.getText();
    }*/

    public void onLoginButtonClicked() throws IOException {
        String username = userNameText.getText();
        String password = passwordText.getText();
        Notification<User> loginNotification = null;
        try {
            loginNotification = authenticationService.login(username, password);
        } catch (AuthenticationException e1) {
            e1.printStackTrace();
        }
        if (loginNotification != null) {
            if (loginNotification.hasErrors()) {
                //  JOptionPane.showMessageDialog(loginView.getContentPane(), loginNotification.getFormattedErrors());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login alert!");
                alert.setHeaderText(null);
                alert.setContentText(loginNotification.getFormattedErrors());
                alert.showAndWait();

                System.out.println("Errors");
            } else {
                //JOptionPane.showMessageDialog(loginView.getContentPane(), "Login successful!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login alert!");
                alert.setHeaderText(null);
                alert.setContentText("Login successful!");
                alert.showAndWait();

                EmployeeConnectionFactory employeeConnectionFactory = EmployeeConnectionFactory.instance(true);

                FXMLLoader loader = null;
                Stage primaryStage =  new Stage();
                 EmployeeController controller1;
                  controller1 = new EmployeeController(employeeConnectionFactory.getClientService(),employeeConnectionFactory.getAccountService());
                  //
                if(username.equals("adm@gmail.com")) {
                        loader = new FXMLLoader(getClass().getResource("/adminPage.fxml"));
                }
                else{
                        loader = new FXMLLoader(getClass().getResource("/employeePage.fxml"));
                }
                //
                loader.setController(controller1);
                 Parent root = loader.load();
                Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();

            }
        }
        // System.out.println(userNameText.getText());

    }

    public void onRegisterButtonClicked(){

        String username = userNameText.getText();
        String password = passwordText.getText();

        Notification<Boolean> registerNotification = authenticationService.register(username, password);
        if (registerNotification.hasErrors()) {
            //JOptionPane.showMessageDialog(loginView.getContentPane(), registerNotification.getFormattedErrors());
            System.out.println("Errors");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration alert!");
            alert.setHeaderText(null);
            alert.setContentText(registerNotification.getFormattedErrors());
            alert.showAndWait();
        } else {
            if (!registerNotification.getResult()) {
                //     JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration not successful, please try again later.");
                System.out.println("Registration not succesful");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration alert!");
                alert.setHeaderText(null);
                alert.setContentText("Registration not successful, please try again later");
                alert.showAndWait();
            } else {
                //    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration successful!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration alert!");
                alert.setHeaderText(null);
                alert.setContentText("Registration successful!");
                alert.showAndWait();

            }
        }
        //System.out.println(userNameText.getText());
    }


}

