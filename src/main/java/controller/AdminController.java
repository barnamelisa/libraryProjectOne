package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import service.admin.AdminService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

public class AdminController {

    // NU punem Repository aici
    private final AdminView adminView;
    private final AdminService adminService; // in service se intampla partea de Business Logic

    public AdminController(AdminView adminView, AdminService adminService) {
        this.adminView = adminView;
        this.adminService = adminService;

        this.adminView.addUserButtonListener(new AddUserButtonListener());
        this.adminView.generateRaportButtonListener(new GenerateRaportButtonListener());
    }

    private class AddUserButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername(); // preia username-ul din view
            String password = adminView.getPassword(); // preia parola din view

            if (username.isEmpty() || password.isEmpty()){
                adminView.addDisplayAlertMessage("Save Error","Problem at Username or Password fields","Can not have an empty Username or Password field.");
            } else {
                UserDTO userDTO = new UserDTOBuilder().setUsername(username).setPassword(password).setRole("employee").build();
                boolean addedUser = adminService.addUser(UserMapper.convertUserDTOToUser(userDTO));

                if (addedUser){
                    adminView.addDisplayAlertMessage("Save Successful","User Added","User was successfully added to the database.");
                    adminView.addUserToObservableList(userDTO);
                } else {
                    adminView.addDisplayAlertMessage("Save Error","Problem at adding User","There was a problem at adding the user to the database. Please try again.");
                }
            }

        }
    }

    private class GenerateRaportButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            boolean reportGenerated = adminService.generateReport();

            if (reportGenerated) {
                adminView.addDisplayAlertMessage("Raport Generat", "Raportul a fost generat cu succes!", "Raportul PDF a fost creat.");
            } else {
                adminView.addDisplayAlertMessage("Eroare Generare Raport", "A aparut o problema la generarea raportului", "Nu s-a putut crea raportul PDF. Incearca din nou.");
            }


        }
    }
}
