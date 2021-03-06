package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Account;
import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.user.AuthenticationException;
import service.account.AccountService;
import service.client.ClientService;
import service.user.AuthenticationService;

import java.io.IOException;
import java.util.Date;

public class EmployeeController {

    private final ClientService clientService;

    private final AccountService accountService;

    private final AuthenticationService authenticationService;

    private FXMLLoader loginLoader;
    private FXMLLoader specificAdmin;

    public EmployeeController(ClientService clientService, AccountService accountService,AuthenticationService authenticationService, FXMLLoader loginLoader,FXMLLoader specificAdmin) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.authenticationService = authenticationService;
        this.loginLoader = loginLoader;
        this.specificAdmin = specificAdmin;
    }

    @FXML
    Button addClientButton;

    @FXML
    TextField nameText;

    @FXML
    TextField identityCardText;

    @FXML
    TextField personalNumCodeText;

    @FXML
    TextField addressText;

    @FXML
    TextField idText;

    @FXML
    TextField clientIdText;

    @FXML
    TextField typeText;

    @FXML
    TextField moneyText;

    @FXML
    TextField dateText;

    @FXML
    Button createAccountButton;

    @FXML
    Button updateAccountButton;

    @FXML
    Button deleteAccountButton;

    @FXML
    Button viewAccountButton;

    @FXML
    Button transferButton;

    @FXML
    TextField sourceText;

    @FXML
    TextField destinationText;

    @FXML
    TextField amountText;

    @FXML
    TextField billText;

    @FXML
    Button billButton;

    @FXML
    Button extraButton;

    public void addClient() throws AuthenticationException, EntityNotFoundException {

       String name = nameText.getText();
       Long identityCardNumber  = Long.parseLong(identityCardText.getText());
       Long personalNumericalCode = Long.parseLong(personalNumCodeText.getText());
       String address = addressText.getText();
       Notification<Boolean> addNotification = clientService.addClient(name,identityCardNumber,personalNumericalCode,address);

       if(addNotification.hasErrors()){
           System.out.println("Errors encountered while adding client");
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Alert!");
           alert.setHeaderText(null);
           alert.setContentText(addNotification.getFormattedErrors());
           alert.showAndWait();
       }
       else{
           authenticationService.saveAction("addClient",1l);
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Alert!");
           alert.setHeaderText(null);
           alert.setContentText("The new client was successfully added!");
           alert.showAndWait();
       }
       // clientService.addClient(name,identityCardNumber,personalNumericalCode,address);
    }

    public void viewClient(){
        Long id  = Long.parseLong(idText.getText());

        Notification<Client> viewNotification = null;
        try {
            viewNotification = clientService.viewClient(id);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The client you are trying to view does not exist");
            alert.showAndWait();
        }
        if(viewNotification !=null)
        {   authenticationService.saveAction("viewClient",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("This is the client you are looking for:" + "\n" + "Name: " + viewNotification.getResult().getName() + "\n" + "Identity card number: " + viewNotification.getResult().getIdentityCardNumber()
                                +"\n" + "Personal numerical code: " + viewNotification.getResult().getPersonalNumericalCode() + "\n" + "Address: " + viewNotification.getResult().getAddress());
            alert.showAndWait();
        }
    }

    public void createAccount(){
       String type = typeText.getText();
       Long amountOfMoney = Long.parseLong(moneyText.getText());
       Date date = new Date();
       Long clientId = Long.parseLong(idText.getText());
        Notification<Boolean> addAccountNotification;
     addAccountNotification = accountService.addAccountToClient(type, amountOfMoney, date, clientId);
        if(addAccountNotification.hasErrors()){
            System.out.println("Errors encountered while adding account");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Errors encountered while adding account!");
            alert.setHeaderText(null);
            alert.setContentText(addAccountNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{authenticationService.saveAction("createAccount",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The new account was successfully added to the client with the id: " + clientId);
            alert.showAndWait();
        }

    }

    public void viewAccount(){
        Long id = Long.parseLong(clientIdText.getText());
        Notification<Account> accountNotification= null;

        try{
            accountNotification = accountService.viewAccount(id);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account you are trying to view does not exist");
            alert.showAndWait();
        }
        if(accountNotification!=null){
            authenticationService.saveAction("viewAccount",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("This is the account you are looking for:" + "\n" +  "Type: " + accountNotification.getResult().getType() + "\n" + "Amount of money: " + accountNotification.getResult().getAmountOfMoney() + "\n" + "Date of creation: "
            + accountNotification.getResult().getDateOfCreation() );
            alert.showAndWait();
        }
    }

    public void transferMoney(){
        System.out.println("Transger");
        Long sourceId = Long.parseLong(sourceText.getText());
        Long destionationId = Long.parseLong(destinationText.getText());
        Long amount = Long.parseLong(amountText.getText());
        Notification<Boolean> booleanNotification = null;

        booleanNotification = accountService.transferMoney(sourceId,destionationId,amount);

        if(booleanNotification.hasErrors())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
            authenticationService.saveAction("transferMoney",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("Your transfer was successful");
            alert.showAndWait();
        }
    }


        public void processBill() {

        Long  cost = Long.parseLong(billText.getText());

        Long clientId = Long.parseLong(idText.getText());
        Long accountId = Long.parseLong(clientIdText.getText());

        Notification<Boolean> booleanNotification = null;

        booleanNotification = accountService.processBill(cost, clientId, accountId);

        if (booleanNotification.hasErrors()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        } else {
            authenticationService.saveAction("processBill", 1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The bill was successfully payed!");
            alert.showAndWait();
        }
    }


    public void updateAccount(){
        System.out.print("update");
        String type = typeText.getText();
        Long amountOfMoney = Long.parseLong(moneyText.getText());
        Date date = new Date();
        Long clientId = Long.parseLong(clientIdText.getText());
        Notification<Boolean> booleanNotification = null;

        booleanNotification = accountService.updateAccount(type, amountOfMoney, date, clientId);

        if(booleanNotification.hasErrors()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
            authenticationService.saveAction("updateAccount",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account was successfully updated!");
            alert.showAndWait();
        }
    }
    public void updateClient(){
        String name = nameText.getText();
        Long identityCardNumber  = Long.parseLong(identityCardText.getText());
        Long personalNumericalCode = Long.parseLong(personalNumCodeText.getText());
        String address = addressText.getText();
        Long id  = Long.parseLong(idText.getText());
        Notification<Boolean> booleanNotification = null;

        booleanNotification = clientService.updateClient(name,identityCardNumber,personalNumericalCode,address,id);

        if(booleanNotification.hasErrors()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
            authenticationService.saveAction("updateClient",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The client was successfully updated!");
            alert.showAndWait();
        }
    }

    public void deleteAccount(){
        boolean test = false;
        Long id = Long.parseLong(clientIdText.getText());
        try{
        accountService.deleteAccount(id);
    }
    catch (EntityNotFoundException e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText(null);
        alert.setContentText("The account your are trying to delete does not exist");
        alert.showAndWait();
        test = true;
    }
        if(test == false) {
            authenticationService.saveAction("deleteAccount",1l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account was succesfully deleted!");
            alert.showAndWait();
        }
}
    public void goToAdmin() throws IOException {

      FXMLLoader loader = specificAdmin;
      AdminController adminController = specificAdmin.getController();
      Scene scene = extraButton.getScene();
      scene.setRoot(loader.getRoot());
    }
}
