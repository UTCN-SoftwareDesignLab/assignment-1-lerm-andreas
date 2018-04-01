package controller;

import componentFactory.AdminComponentFactory;
import componentFactory.EmployeeComponentFactory;
import componentFactory.LoginComponentFactory;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SceneFactory {

    private FXMLLoader administratorLoader;
    private FXMLLoader employeeLoader;
    private FXMLLoader loginLoader;
    private FXMLLoader specificAdmin;

    private static SceneFactory instance;

    public static SceneFactory instance() throws IOException {
        if(instance == null){
            instance = new SceneFactory();
        }
        return instance;
    }

    private SceneFactory() throws IOException{
        LoginComponentFactory myComponentFactory = LoginComponentFactory.instance(false);
        EmployeeComponentFactory employeeConnectionFactory = EmployeeComponentFactory.instance(true);
        AdminComponentFactory adminConnectionFactory = AdminComponentFactory.instance(true);

        loginLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        employeeLoader = new FXMLLoader(getClass().getResource("/employeePage.fxml"));
        administratorLoader = new FXMLLoader(getClass().getResource("/adminPage.fxml"));
        specificAdmin = new FXMLLoader(getClass().getResource("/specificAdmin.fxml"));

        LoginController loginController = new LoginController(myComponentFactory.getAuthenticationService(),administratorLoader,employeeLoader);
        loginLoader.setController(loginController);
        loginLoader.load();

        EmployeeController employeeController = new EmployeeController(employeeConnectionFactory.getClientService(),employeeConnectionFactory.getAccountService(),employeeConnectionFactory.getAuthenticationService(),loginLoader,specificAdmin);
        employeeLoader.setController(employeeController);
        administratorLoader.setController(employeeController);
        employeeLoader.load();
        administratorLoader.load();

        AdminController adminController = new AdminController(adminConnectionFactory.getAuthenticationService(),employeeLoader);
        specificAdmin.setController(adminController);
        specificAdmin.load();
    }


    public FXMLLoader getLoginLoader() {
        return loginLoader;
    }

}
