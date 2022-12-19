package fxControllers;

import entities.StopPoint;
import entities.Trip;
import hibernate.HibernateCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class StopPointWindowController implements Initializable {
    public TextField textFieldStopName;
    public TextArea textAreaStopDescription;
    public TextField textFieldAddress;
    public TextField textFieldCoordinates;
    public DatePicker datePickerArrival;
    public TextField textFieldTimeOfArrival;
    public DatePicker datePickerDeparture;
    public TextField textFieldTimeofDeparture;
    public Button buttonCreate;
    public Button buttonUpdate;
    public TextField textFieldOdometer;
    public TextField textLabelDieselRefueled;
    private StopPoint stopPoint;
    private TripsWindowController tripsWindowController;
    private Trip trip;

    public void createStopPoint(ActionEvent actionEvent) {
        collectData();
        List<StopPoint> stopPoints = trip.getAllRestPoints();
        stopPoints.add(stopPoint);
        trip.setAllRestPoints(stopPoints);
        HibernateCRUD.createObject(stopPoint);
        HibernateCRUD.updateObject(trip);
        tripsWindowController.refreshStopPints();
        Stage stage = (Stage) buttonUpdate.getScene().getWindow();
        stage.close();
    }

    public void updateStopPoint(ActionEvent actionEvent) {
        collectData();
        HibernateCRUD.updateObject(stopPoint);
        tripsWindowController.refreshStopPints();
        Stage stage = (Stage) buttonUpdate.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    private void collectData(){
        stopPoint.setStopName(textFieldStopName.getText());
        stopPoint.setStopDescription(textAreaStopDescription.getText());
        stopPoint.setAddress(textFieldAddress.getText());
        stopPoint.setCoordinates(textFieldCoordinates.getText());
        stopPoint.setTimeOfArrival(LocalDateTime.of(datePickerArrival.getValue(), LocalTime.parse(textFieldTimeOfArrival.getText())));
        stopPoint.setTimeOfDeparture(LocalDateTime.of(datePickerDeparture.getValue(), LocalTime.parse(textFieldTimeofDeparture.getText())));
        stopPoint.setOdometer(Integer.parseInt(textFieldOdometer.getText()));
        if(!textLabelDieselRefueled.getText().isEmpty()){
            stopPoint.setVolumeOfDiesel(Double.parseDouble(textLabelDieselRefueled.getText()));
        }
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        setButtons();
    }
    public void setTripsWindowController(TripsWindowController tripsWindowController){
        this.tripsWindowController = tripsWindowController;
    }

    public void setStopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
        setButtons();
    }
    private void setButtons(){
        if(stopPoint==null){
            stopPoint = new StopPoint();
            buttonUpdate.setVisible(false);
        }else{
            buttonCreate.setVisible(false);
            buttonUpdate.setVisible(true);

            textFieldStopName.setText(stopPoint.getStopName());
            textAreaStopDescription.setText(stopPoint.getStopDescription());
            textFieldAddress.setText(stopPoint.getAddress());
            textFieldCoordinates.setText(stopPoint.getCoordinates());
            datePickerArrival.setValue(LocalDate.from(stopPoint.getTimeOfArrival()));
            textFieldTimeOfArrival.setText(String.valueOf(LocalTime.from(stopPoint.getTimeOfArrival())));
            datePickerDeparture.setValue(LocalDate.from(stopPoint.getTimeOfDeparture()));
            textFieldTimeofDeparture.setText(String.valueOf(LocalTime.from(stopPoint.getTimeOfDeparture())));
            textFieldOdometer.setText(String.valueOf(stopPoint.getOdometer()));
            textLabelDieselRefueled.setText(String.valueOf(stopPoint.getVolumeOfDiesel()));
        }
    }
}
