package fxControllers;

import entities.Cargo;
import entities.CargoType;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CargoWindowController {
    public CheckBox checkboxContainsBatteries;
    public CheckBox checkboxRefrigeratedBelovZero;
    public CheckBox checkboxRefrigeratedAboveZero;
    public CheckBox checkboxLoose;
    public CheckBox checkboxLiquid;
    public CheckBox checkboxHazard;
    public CheckBox checkboxOnEuroPallets;
    public DatePicker datepickerMustBeDeliveredOn;
    public DatePicker datepickerReadyForPickup;
    public TextField fieldValueOfCargo;
    public TextField textfieldTotalWeightOfCargo;
    public TextField textfieldPickupAddress;
    public PasswordField textfieldDestinationAddress;
    public Button buttonCreate;
    public Button buttonUpdate;
    public RadioButton radioButtonLoose;
    public ToggleGroup cargoTypeRadioGroup;
    public RadioButton radioButtonLiquid;
    public RadioButton radioButtonEuroPallet;

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    private Cargo cargo;

    public void createCargo(ActionEvent actionEvent) {
        cargo = new Cargo();
        collectData();
        HibernateCRUD.createObject(cargo);
        Stage stage = (Stage) radioButtonLoose.getScene().getWindow();
        stage.close();
    }

    public void updateAction(ActionEvent actionEvent) {
        collectData();
        HibernateCRUD.updateObject(cargo);
        Stage stage = (Stage) radioButtonLoose.getScene().getWindow();
        stage.close();
    }
    private void collectData(){
        if(radioButtonLiquid.isSelected()){
            cargo.setCargoType(CargoType.Liquid);
        } else if (radioButtonLoose.isSelected()) {
            cargo.setCargoType(CargoType.Loose);
        } else if (radioButtonEuroPallet.isSelected()) {
            cargo.setCargoType(CargoType.OnEuroPallets);
        }
        cargo.setMustBeDeliveredUntilDate(datepickerMustBeDeliveredOn.getValue());
        cargo.setReadyForPickUpDate(datepickerReadyForPickup.getValue());
        if(!fieldValueOfCargo.getText().isEmpty()) {
            cargo.setValueOfCargoEUR(Float.parseFloat(fieldValueOfCargo.getText()));
        }
        if(!textfieldTotalWeightOfCargo.getText().isEmpty()){
            cargo.setTotalCargoWeightTonnes(Float.parseFloat(textfieldTotalWeightOfCargo.getText()));
        }
        cargo.setPickupAddress(textfieldPickupAddress.getText());
        cargo.setDeliveryAddress(textfieldDestinationAddress.getText());
    }
}
