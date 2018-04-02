package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Account;
import model.Action;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;
import service.user.AuthenticationService;

import java.util.List;

import static java.lang.Boolean.FALSE;

public class AdminController {
    private final AuthenticationService authenticationService;
    private FXMLLoader adminLoader;


    public AdminController(AuthenticationService authenticationService,FXMLLoader adminLoader) {
        this.authenticationService = authenticationService;
        this.adminLoader = adminLoader;
    }

    @FXML
    TextField idText;

    @FXML
    TextField userText;

    @FXML
    TextField passwordText;

    @FXML
    TextField fromText;

    @FXML
    TextField toText;

    @FXML
    Button addButton;

    @FXML
    Button viewButton;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    @FXML
    Button reportButton;


    public void addUser(){
        System.out.println("add");

    }

    public void viewUser(){

        Long id = Long.parseLong(idText.getText());
        Notification<User> userNotification= null;

        try{
            userNotification = authenticationService.viewUser(id);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The user you are trying to view does not exist");
            alert.showAndWait();
        }
        if(userNotification!=null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("This is the user you are looking for: " + "\n" + "Username: " +  userNotification.getResult().getUsername()
                                    );
            alert.showAndWait();
        }

    }

    public void updateUser(){
        System.out.println("update");
        String userName = userText.getText();
        String password = passwordText.getText();

        Long userId = Long.parseLong(idText.getText());

        Notification<Boolean> boolNotification = null;

        boolNotification = authenticationService.updateUser(userName, password,userId);

        if(boolNotification.hasErrors()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!!!!");
            alert.setHeaderText(null);
            alert.setContentText(boolNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The user was successfully updated!");
            alert.showAndWait();
        }
    }

    public void deleteUser(){
        System.out.println("delete");

        Long id = Long.parseLong(idText.getText());
        boolean test = false;
        try{
            authenticationService.deleteUser(id);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The user your are trying to delete does not exist");
            alert.showAndWait();
            test = true;
        }
        if(test == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The user was successfully deleted!");
            alert.showAndWait();
        }
    }

    public void generateReport(){
       Long id  = Long.parseLong(idText.getText());
        boolean test = false;
        List<Action> actionList = null;
       try{
          actionList =  authenticationService.getActionsForUser(id);
       }
       catch (EntityNotFoundException e){
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Alert!");
           alert.setHeaderText(null);
           alert.setContentText("There are no actions to show for the chosen user");
           alert.showAndWait();
           test = true;
       }
        if(test == false) {
           String str = " ";
            for(Action action1:actionList){
                str+= "Action description: " +  action1.getDescription() + " " + "Action date: " + action1.getDate() + "\n";
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("These are the actions done by the user!" + "\n" + str);
            alert.showAndWait();
        }
    }
}
