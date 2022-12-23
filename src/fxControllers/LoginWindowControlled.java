package fxControllers;

import entities.User;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.HelloApplication;

import java.io.IOException;

public class LoginWindowControlled {
    public Button signupButton;
    public TextField loginField;
    public PasswordField passwordField;

    public void loginButtonClicked(ActionEvent actionEvent) throws IOException {
        User user = null;
        user = HibernateCRUD.getUserByLoginData(loginField.getText(),passwordField.getText());

        if(user!=null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/main-window.fxml"));
            Parent root = loader.load();

            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setUser(user);

            Stage stage = (Stage)signupButton.getScene().getWindow();
            stage.setTitle("This is not default title.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There's no mach for those login & password inputs");
            alert.show();
        }
    }

    public void signupButtonClicked(ActionEvent actionEvent) {
        SignupWindowController.openSignupWindow((Stage)signupButton.getScene().getWindow());
    }
    public static void openLoginWindow (Stage stage){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxmls/login-window.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }
}