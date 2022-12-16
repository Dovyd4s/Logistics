package fxControllers;

import entities.Truck;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TruckWindowController implements Initializable {
    @FXML
    private Truck truck;
    @FXML
    public TextField fieldVIN;
    @FXML
    public TextField fieldPlateNumber;
    @FXML
    public TextField fieldMake;
    @FXML
    public TextField fieldModel;
    @FXML
    public TextField fieldWeight;
    @FXML
    public TextField fieldCapacity;
    @FXML
    public TextField fieldAvgFuelConsumtion;
    @FXML
    public TextField fieldFuelTankSize;
    @FXML
    public DatePicker fieldDateOfLatestTechStateCheck;
    @FXML
    public DatePicker fieldDateOfManufacture;
    @FXML
    public Button addTruckButton;
    @FXML
    public Button updateButton;

    private void collectTaxtDataToTruckObject (){
        truck = new Truck();
        truck.setVIN(fieldVIN.getText());
        truck.setPlateNumber(fieldPlateNumber.getText());
        truck.setMake(fieldMake.getText());
        truck.setModel(fieldModel.getText());
        truck.setWeightTonnes(Float.parseFloat(fieldWeight.getText()));
        truck.setMaxCapacityTonnes(Float.parseFloat(fieldCapacity.getText()));
        truck.setAverageFuelConsumption(Float.parseFloat(fieldAvgFuelConsumtion.getText()));
        truck.setTankSizeLiters(Float.parseFloat(fieldFuelTankSize.getText()));
        truck.setManufacturedDate(fieldDateOfManufacture.getValue());
        truck.setLastTechnicalStateCheckDate(fieldDateOfLatestTechStateCheck.getValue());
    }
    public void addTruck(ActionEvent actionEvent) {
        collectTaxtDataToTruckObject();
        HibernateCRUD.createObject(truck);
    }

    public void fillTruckData (){
        fieldVIN.setText(truck.getVIN());
        fieldVIN.setDisable(true);
        fieldPlateNumber.setText(truck.getPlateNumber());
        fieldMake.setText(truck.getMake());
        fieldModel.setText(truck.getModel());
        fieldWeight.setText(String.valueOf(truck.getWeightTonnes()));
        fieldCapacity.setText(String.valueOf(truck.getMaxCapacityTonnes()));
        fieldAvgFuelConsumtion.setText(String.valueOf(truck.getAverageFuelConsumption()));
        fieldFuelTankSize.setText(String.valueOf(truck.getTankSizeLiters()));
        fieldDateOfLatestTechStateCheck.setValue(truck.getLastTechnicalStateCheckDate());
        fieldDateOfManufacture.setValue(truck.getManufacturedDate());
        updateButton.setDisable(false);
    }
    public void UpdateTruckInfo(ActionEvent actionEvent) {
        collectTaxtDataToTruckObject();
        HibernateCRUD.updateObject(truck);
        System.out.println("NEW DATA: " + truck);
    }

    public void openNewWindow () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TruckWindowController.class.getResource("/fxmls/Truck-window.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        addTruckButton.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void setTruck(Truck truck) {
        this.truck = truck;
        fillTruckData();
    }
}
