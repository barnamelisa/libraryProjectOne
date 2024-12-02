package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import service.admin.AdminService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

public class AdminController {
    private final AdminView adminView;
    private final AdminService adminService;

    public AdminController(AdminView adminView, AdminService adminService) {
        this.adminView = adminView;
        this.adminService = adminService;

        this.adminView.addUserButtonListener(new AddUserButtonListener());
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
}
