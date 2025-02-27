package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import launcher.AdminComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import model.User;

import model.validation.Notification;
import model.validation.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;

import java.util.List;

public class LoginController {

    // NU punem Repository aici
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else{
                User loggedInUser = loginNotification.getResult(); // daca nu exista erori metoda getResult() va returna un obiect de tip User
                if (loggedInUser != null){
                    Long userId = loggedInUser.getId();
                    loginView.setActionTargetText("LogIn Successfull!");

                    if (loggedInUser.hasRole("employee")){

                        // aceasta linie de cod deschide un alt stage care este Library
                        EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), userId);
                    } else if (loggedInUser.hasRole("administrator")){

                        // daca persoana care face log in este Admin atunci se va deschide o interfata noua care va permite utilizatorului sa adauge noi users
                        // getStage furnizeaza scena in care componentele din interfata vor fi adaugate
                        AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                    }
                } else {
                    loginView.setActionTargetText("Error: Logged in user details not found.");
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}