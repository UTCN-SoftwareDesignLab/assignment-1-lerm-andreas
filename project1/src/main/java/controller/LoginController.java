package controller;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.user.AuthenticationException;
import service.user.AuthenticationService;

import java.io.IOException;

public class LoginController {

    private final AuthenticationService authenticationService;
    private String userName;
    private String password;
    private FXMLLoader administratorLoader;
    private FXMLLoader employeeLoader;


    public LoginController(AuthenticationService authenticationService, FXMLLoader administratorLoader, FXMLLoader employeeLoader) {
        this.authenticationService = authenticationService;
        this.administratorLoader = administratorLoader;
        this.employeeLoader = employeeLoader;
    }

    @FXML
    Button logInButton;

    @FXML
    Button registerButton;

    @FXML
    TextField userNameText;

    @FXML
    TextField passwordText;



    public String getUserNameText(){
        System.out.println(userNameText.getText());
        return userNameText.getText();
    }

    public String getPassword(){
        System.out.println(passwordText.getText());
        return passwordText.getText();
    }

    public void onLoginButtonClicked() throws IOException, AuthenticationException, EntityNotFoundException {
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
               //setCurrentUser(authenticationService.viewUserByUserNameAndPassword(username,password).getResult());
                authenticationService.saveAction("Login",1l);
//                System.out.println(currentUser.getUsername());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login alert!");
                alert.setHeaderText(null);
                alert.setContentText("Login successful!");
                alert.showAndWait();
                setUserName(username);
                setPassword(password);
              /*  EmployeeComponentFactory employeeConnectionFactory = EmployeeComponentFactory.instance(true);

                FXMLLoader loader = null;
                Stage primaryStage =  new Stage();
                 EmployeeController controller1;
                  controller1 = new EmployeeController(employeeConnectionFactory.getClientService(),employeeConnectionFactory.getAccountService(),employeeConnectionFactory.getAuthenticationService());
                  //
                setUserName(username);
                if(userName.equals("adm@gmail.com")) {
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
               */
                FXMLLoader loader;
                if(userName.equals("adm@gmail.com")){
                    loader = administratorLoader;
                   EmployeeController adminController = loader.getController();
                }
                else{
                    loader = employeeLoader;
                    EmployeeController employeeController = loader.getController();
                }
                Scene scene = logInButton.getScene();
               scene.setRoot(loader.getRoot());
            }
        }
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

