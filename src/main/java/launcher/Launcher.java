package launcher;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Cream o clasa Main care sa extinda Application
 * Aceasta clasa va lansa aplicatia si va deschide interfata grafica
 */
public class Launcher extends Application {
    public static void main(String[] args){
        launch(args); // apeleaza in spate metoda de start
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        ComponentFactory.getInstance(false, primaryStage);
    }
}
