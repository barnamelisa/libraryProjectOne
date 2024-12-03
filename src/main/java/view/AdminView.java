package view;

import view.model.BookDTO;
import view.model.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

// interfata/stage-ul pt admin, unde se vor putea adauga mai multi users
public class AdminView {
    private TextField usernameTextField;
    private PasswordField passwordField;
    private final ObservableList<UserDTO> usersObservableList;
    private Button addUserButton;
    private Button generateRaportButton;
    private Text actiontarget;

    public AdminView(Stage primaryStage, List<UserDTO> users){
        primaryStage.setTitle("Admin Page");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(users);
        initializeSceneTitle(gridPane);

        initializeFields(gridPane);
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("Welcome to Admin Page");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }
    private void initializeFields(GridPane gridPane){
        Label userName = new Label("User Name:");
        gridPane.add(userName, 0, 1);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 1, 1);

        Label password = new Label("Password");
        gridPane.add(password, 0, 2);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        addUserButton = new Button("Add User");
        HBox signInButtonHBox = new HBox(10);
        signInButtonHBox.setAlignment(Pos.BOTTOM_CENTER); // for now on center
        signInButtonHBox.getChildren().add(addUserButton);
        gridPane.add(signInButtonHBox, 1, 4);

        generateRaportButton = new Button("Generate Report");
        HBox generateRaportButtonHBox = new HBox(10);
        generateRaportButtonHBox.setAlignment(Pos.BOTTOM_CENTER);
        generateRaportButtonHBox.getChildren().add(generateRaportButton);
        gridPane.add(generateRaportButtonHBox, 2, 4);

        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 6);
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }
    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
    public void addUserButtonListener(EventHandler<ActionEvent> addUserButtonListener) {
        addUserButton.setOnAction(addUserButtonListener);
    }

    public void generateRaportButtonListener(EventHandler<ActionEvent> generateRaportButtonListener) {
        generateRaportButton.setOnAction(generateRaportButtonListener);
    }
    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait(); // va afisa si va astepta pana il inchid manual
    }

    public void addUserToObservableList(UserDTO userDTO){
        this.usersObservableList.add(userDTO);
    }

}
