package fxControllers;

import entities.*;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TripsWindowController implements Initializable {
    public ListView trucksList;
    public Button buttonAssignTruck;
    public TextArea textAreaSelectedTruck;
    public ListView listViewStopPoints;
    public RadioButton radioButtonInProcessNo;
    public ToggleGroup TripRadio;
    public RadioButton radioButtonInProcessYes;
    public RadioButton radioButtonComplete;
    public ChoiceBox choiceBoxCargo;
    public Button buttonCreate;
    public Button buttonUpdate;

    public void setTrip(Trip trip) {
        this.trip = trip;
        fillDataFields();
    }

    private Trip trip;
    private Truck assignedTruck;
    public TextField fieldStartPoint;
    public TextField fieldDestination;
    public ChoiceBox<Driver> choiceBoxDriver;
    public ComboBox<Manager> comboBoxManager;
    public TextField fieldDistance;
    public TextField fieldFuelConsumed;
    public TextField fieldAverageSpeed;

    public void createNewTrip(ActionEvent actionEvent) {
        trip = new Trip();
        collectDataFromForms();
        HibernateCRUD.createObject(trip);
    }

    private void fillDataFields (){
        choiceBoxCargo.setValue(trip.getCargo());
        choiceBoxDriver.setValue(trip.getAssignedDriver());
        comboBoxManager.setValue(trip.getAssignedManager());
        fieldDistance.setText(String.valueOf(trip.getDistance()));
        fieldFuelConsumed.setText(String.valueOf(trip.getFuelConsumed()));
        fieldAverageSpeed.setText(String.valueOf(trip.getAverageSpeed()));
        if(trip.isInProcess()){
            radioButtonInProcessYes.setSelected(true);
        }else if(trip.isComplete()){
            radioButtonComplete.setSelected(true);
        }else{
            radioButtonInProcessNo.setSelected(true);
        }
    }
    public void updateTrip(ActionEvent actionEvent) {
        collectDataFromForms();
        HibernateCRUD.updateObject(trip);
    }
    private void collectDataFromForms (){
        trip.setCargo((Cargo) choiceBoxCargo.getValue());
        trip.setAssignedDriver(choiceBoxDriver.getValue());
        trip.setAssignedManager(comboBoxManager.getValue());
        if(!fieldDistance.getText().isEmpty()){
            trip.setDistance(Float.parseFloat(fieldDistance.getText()));
        }
        if(!fieldFuelConsumed.getText().isEmpty()) {
            trip.setFuelConsumed(Float.parseFloat(fieldFuelConsumed.getText()));
        }
        if (!fieldAverageSpeed.getText().isEmpty()) {
            trip.setAverageSpeed(Float.parseFloat(fieldAverageSpeed.getText()));
        }
        trip.setAssignedTruck(assignedTruck);
        if(radioButtonInProcessYes.isSelected()){
            trip.setInProcess(true);
            trip.setComplete(false);
        } else if (radioButtonComplete.isSelected()) {
            trip.setComplete(true);
            trip.setInProcess(false);
        }else{
            trip.setInProcess(false);
            trip.setComplete(false);
        }
    }
    private void fillDriversBox (){
        Driver driver = new Driver();
        List<Driver> drivers = new ArrayList<>();
        drivers = HibernateCRUD.getAllEntity(driver);
        drivers.add(null);
        drivers.forEach(c->choiceBoxDriver.getItems().add(c));
    }
    private void fillManagersBox (){
        Manager manager = new Manager();
        List<Manager> managers = new ArrayList<>();
        managers = HibernateCRUD.getAllEntity(manager);
        managers.forEach(c->comboBoxManager.getItems().add(c));
    }
    private void fillCargoBox (){
        Cargo cargo = new Cargo();
        List<Cargo> cargos = new ArrayList<>();
        cargos = HibernateCRUD.getAllEntity(cargo);
        cargos.forEach(c->choiceBoxCargo.getItems().add(c));
    }

    private void fillTrucksList(){
        Truck truck = new Truck();
        List<Truck> trucks = HibernateCRUD.getAllEntity(truck);
        trucks.forEach(c->trucksList.getItems().add(c));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillDriversBox();
        fillManagersBox();
        fillTrucksList();
        fillCargoBox();
        buttonUpdate.setDisable(true);
    }

    public void assignSelectedTruck(ActionEvent actionEvent) {
        assignedTruck = (Truck) trucksList.getSelectionModel().getSelectedItem();
        textAreaSelectedTruck.setText("Make: " + assignedTruck.getMake() +
            "\nModel: " + assignedTruck.getModel() +
               "\nPlates: " + assignedTruck.getPlateNumber() +
               "\nVIN: " + assignedTruck.getVIN());
    }

    public void editSelectedStopPoint(ActionEvent actionEvent) throws IOException {
        openStopPointsWindow((StopPoint) listViewStopPoints.getSelectionModel().getSelectedItem());
    }

    public void addNewStopPoint(ActionEvent actionEvent) throws IOException {
        openStopPointsWindow(null);
    }
    private void openStopPointsWindow(StopPoint stopPoint) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/stop-point-window.fxml"));
        Parent root = loader.load();
        StopPointWindowController stopPointWindowController = loader.getController();
        stopPointWindowController.setTrip(trip);
        stopPointWindowController.setStopPoint(stopPoint);
        stopPointWindowController.setTripsWindowController(this);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void refreshStopPints (){
        listViewStopPoints.getItems().clear();
        trip.getAllRestPoints().forEach(c->listViewStopPoints.getItems().add(c));
    }
    public void setUpdateMode(){
        buttonCreate.setDisable(true);
        buttonUpdate.setDisable(false);
    }
}
