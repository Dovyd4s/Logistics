package fxControllers;

import entities.Driver;
import entities.Manager;
import entities.User;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import view.HelloApplication;

import java.io.IOException;

public class SignupWindowController {
    public RadioButton driverRadioButton;
    public RadioButton managetRadioButton;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField repeatPasswordField;
    public TextField nameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField phoneNrField;
    public TextField addressField;
    public DatePicker birthDateField;
    public DatePicker dateOfEmploymentField;
    public Button createAccButton;
    public Label healthCheckDateLabel;
    public Label driversLicenceValidDateLabel;
    public DatePicker healthCheckDateField;
    public DatePicker driversLicenceValidDateField;
    public Label labelPasswordDontMach;
    public Label labelLoginInUse;
    public Button buttonUpdate;
    public Button buttonGoToLogin;
    public TextField textFieldBonusPerKm;
    public TextField textFieldMinimumDistanceForBonus;
    public RadioButton radioIsAdmin;
    public ToggleGroup AdminOrNotGroup;
    public RadioButton radioNotAdmin;
    public Label labelBonusperKm;
    public Label labelMinDistanceForBonus;
    private User editor;

    public void setUser(User user) {
        this.user = user;
        fillData();
        buttonGoToLogin.setDisable(true);
    }
    public void setEditor (User editor){
        this.editor = editor;
        if(editor.getClass().equals(Manager.class)){
            if(((Manager) editor).isAdmin()){
                textFieldBonusPerKm.setDisable(false);
                textFieldMinimumDistanceForBonus.setDisable(false);
                radioIsAdmin.setDisable(false);
                radioNotAdmin.setDisable(false);
            }
        }
        createAccButton.setDisable(true);
    }

    private User user;

    public static void openSignupWindow (Stage stage){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxmls/signup-window.fxml"));
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
    public void createButtonClicked(ActionEvent actionEvent) {
        User user = null;
        if(driverRadioButton.isSelected()){
            Driver driver = new Driver();
            driver.setHealthCheckValidUntilDate(healthCheckDateField.getValue());
            driver.setDriverLicenseValidUntilDate(driversLicenceValidDateField.getValue());
            user = driver;
        } else if (managetRadioButton.isSelected()) {
            Manager manager = new Manager();
            user = manager;
        }

        user.setLogin(loginField.getText());
        user.setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setLastName(lastNameField.getText());
        user.setEmail(emailField.getText());
        user.setAddress(addressField.getText());
        user.setBirthDate(birthDateField.getValue());
        user.setEmployedSinceDate(dateOfEmploymentField.getValue());

        HibernateCRUD.createObject(user);
    }

    public void driverSelected(ActionEvent actionEvent) {
        healthCheckDateField.setVisible(true);
        healthCheckDateLabel.setVisible(true);
        driversLicenceValidDateField.setVisible(true);
        driversLicenceValidDateLabel.setVisible(true);
        createAccButton.setDisable(false);
        radioIsAdmin.setVisible(false);
        radioNotAdmin.setVisible(false);
        textFieldBonusPerKm.setVisible(false);
        textFieldMinimumDistanceForBonus.setVisible(false);
        labelBonusperKm.setVisible(false);
        labelMinDistanceForBonus.setVisible(false);
    }

    public void managerSelected(ActionEvent actionEvent) {
        healthCheckDateField.setVisible(false);
        healthCheckDateLabel.setVisible(false);
        driversLicenceValidDateField.setVisible(false);
        driversLicenceValidDateLabel.setVisible(false);
        createAccButton.setDisable(false);
        radioIsAdmin.setVisible(true);
        radioNotAdmin.setVisible(true);
        textFieldBonusPerKm.setVisible(true);
        textFieldMinimumDistanceForBonus.setVisible(true);
        labelBonusperKm.setVisible(true);
        labelMinDistanceForBonus.setVisible(true);
    }

    public void goToLoginWindow(ActionEvent actionEvent) {
        LoginWindowControlled.openLoginWindow((Stage) createAccButton.getScene().getWindow());
    }

    public void keyPressedPasswordField(KeyEvent keyEvent) {
        if(passwordField.getText().length()>0 && repeatPasswordField.getText().length()>0){
            if(passwordField.getText().equals(repeatPasswordField.getText())){
                labelPasswordDontMach.setVisible(false);
            }else{
                labelPasswordDontMach.setVisible(true);
            }
        }else{
            labelPasswordDontMach.setVisible(false);
        }
    }

    public void loginKeyPressed(KeyEvent keyEvent) {
        if(HibernateCRUD.getUserByLogin(loginField.getText())!=null){
            labelLoginInUse.setVisible(true);
        }else{
            labelLoginInUse.setVisible(false);
        }
    }

    public void fillData (){
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword());
        repeatPasswordField.setText(user.getPassword());
        nameField.setText(user.getName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        phoneNrField.setText(user.getPhoneNumber());
        addressField.setText(user.getAddress());
        birthDateField.setValue(user.getBirthDate());
        dateOfEmploymentField.setValue(user.getEmployedSinceDate());
        if(user.getClass().equals(Driver.class)){
            managetRadioButton.setDisable(true);
            driversLicenceValidDateField.setValue(((Driver) user).getDriverLicenseValidUntilDate());
            healthCheckDateField.setValue(((Driver) user).getHealthCheckValidUntilDate());
            driverRadioButton.setSelected(true);
            labelMinDistanceForBonus.setVisible(false);
            labelBonusperKm.setVisible(false);
            textFieldMinimumDistanceForBonus.setVisible(false);
            textFieldBonusPerKm.setVisible(false);
            radioIsAdmin.setVisible(false);
            radioNotAdmin.setVisible(false);
        }else{
            driverRadioButton.setDisable(true);
            managetRadioButton.setSelected(true);
            textFieldBonusPerKm.setText(String.valueOf(((Manager)user).getBonusAmountPerKmEUR()));
            textFieldMinimumDistanceForBonus.setText(String.valueOf(((Manager)user).getMinDistanceToGetBonus()));
            if(((Manager)user).isAdmin()){
                radioIsAdmin.setSelected(true);
            }else{
                radioNotAdmin.setSelected(true);
            }
        }

    }
    public void updateUser(ActionEvent actionEvent) {
        if(driverRadioButton.isSelected()){
            ((Driver)user).setHealthCheckValidUntilDate(healthCheckDateField.getValue());
            ((Driver)user).setDriverLicenseValidUntilDate(driversLicenceValidDateField.getValue());
        }else{
            ((Manager)user).setAdmin(radioIsAdmin.isSelected());
            ((Manager)user).setBonusAmountPerKmEUR(Float.parseFloat(textFieldBonusPerKm.getText()));
            ((Manager)user).setMinDistanceToGetBonus(Integer.parseInt(textFieldMinimumDistanceForBonus.getText()));
        }
        user.setLogin(loginField.getText());
        user.setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setLastName(lastNameField.getText());
        user.setEmail(emailField.getText());
        user.setAddress(addressField.getText());
        user.setBirthDate(birthDateField.getValue());
        user.setEmployedSinceDate(dateOfEmploymentField.getValue());

        HibernateCRUD.updateObject(user);
    }
}
