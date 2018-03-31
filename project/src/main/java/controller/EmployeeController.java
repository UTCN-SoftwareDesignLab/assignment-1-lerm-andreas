package controller;

import Sample.AdminConnectionFactory;
import Sample.EmployeeConnectionFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Account;
import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;
import service.account.AccountService;
import service.client.ClientService;
import service.user.AuthenticationService;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EmployeeController {

    private final ClientService clientService;

    private final AccountService accountService;

    public EmployeeController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
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

    public void addClient(){
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
        try{
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
        {
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
        Notification<Boolean> addAccountNotification = accountService.addAccountToClient(type,amountOfMoney,date,clientId);
        if(addAccountNotification.hasErrors()){
            System.out.println("Errors encountered while adding account");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Errors encountered while adding account!");
            alert.setHeaderText(null);
            alert.setContentText(addAccountNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
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
        try{
            booleanNotification = accountService.transferMoney(sourceId,destionationId,amount);
        }catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("At least one of the account does not exist");
            alert.showAndWait();
        }
        if(booleanNotification.hasErrors())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("Your transfer was successful");
            alert.showAndWait();
        }
    }

    public void processBill(){
        Long cost = Long.parseLong(billText.getText());
        Long clientId = Long.parseLong(idText.getText());
        Long accountId = Long.parseLong(clientIdText.getText());

        Notification<Boolean> booleanNotification = null;
        try{
            booleanNotification = accountService.processBill(cost,clientId,accountId);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account/client does not exist");
            alert.showAndWait();
        }
        if(booleanNotification.hasErrors())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
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
        try {
           booleanNotification = accountService.updateAccount(type, amountOfMoney, date, clientId);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account your are trying to update does not exist");
            alert.showAndWait();
        }
        if(booleanNotification.hasErrors()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
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
        try {
            booleanNotification = clientService.updateClient(name,identityCardNumber,personalNumericalCode,address,id);
        }
        catch (EntityNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The client your are trying to update does not exist");
            alert.showAndWait();
        }
        if(booleanNotification.hasErrors()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!!!!");
            alert.setHeaderText(null);
            alert.setContentText(booleanNotification.getFormattedErrors());
            alert.showAndWait();
        }
        else{
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("The account was succesfully deleted!");
            alert.showAndWait();
        }
}
    public void goToAdmin() throws IOException {

       AdminConnectionFactory adminConnectionFactory = AdminConnectionFactory.instance(true);

        FXMLLoader loader = null;
        Stage primaryStage =  new Stage();
        AdminController controller1;
        controller1 = new AdminController(adminConnectionFactory.getAuthenticationService());
        loader = new FXMLLoader(getClass().getResource("/specificAdmin.fxml"));
        //create constructor in admin controller
        //
        loader.setController(controller1);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
