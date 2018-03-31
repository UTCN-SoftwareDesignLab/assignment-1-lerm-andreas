package Sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.validation.Notification;
import repository.user.AuthenticationException;
import service.user.AuthenticationService;
import view.LoginView;

public class LoginController {



   private AuthenticationService authenticationService;
    public LoginController(){}
    public LoginController(AuthenticationService authenticationService) {
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

   /* public void onLoginButtonClicked(){
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
                System.out.println("Errors");
            } else {
                //JOptionPane.showMessageDialog(loginView.getContentPane(), "Login successful!");
                System.out.println("Login successful");
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
        } else {
            if (!registerNotification.getResult()) {
           //     JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration not successful, please try again later.");
                System.out.println("Registration not succesful");
            } else {
            //    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration successful!");
                System.out.println("Registration not succesful");
            }
        }
        //System.out.println(userNameText.getText());
    }
*/
}
