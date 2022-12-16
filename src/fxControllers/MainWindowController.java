package fxControllers;

import entities.*;
import hibernate.HibernateCRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public Tab tabTrucks;
    public Tab tabTrips;
    public Tab tabShipments;
    public Tab tabForum;
    public ListView<Truck> listViewTrucks;
    public ListView<Trip> listViewTrips;
    public Button buttonUpdateSelectedCargo;
    public Button buttonDeleteSelectedCargo;
    public ListView<Cargo> listViewCargo;
    public Button buttonEditUser;
    public Button buttonDeleteUser;
    public Group buttonsGroupTrucks;
    private User user;
    public Tab usersTab;
    public TableView<TableViewUserParameters> usersTableView;
    public TableColumn<TableViewUserParameters, String> idColumn;
    public TableColumn<TableViewUserParameters, String> nameColumns;
    public TableColumn<TableViewUserParameters, String> lastNameColumn;
    public TableColumn<TableViewUserParameters, String> roleColumn;
    private ObservableList<TableViewUserParameters> data = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
        if(user.getClass().equals(Manager.class)){
            fillUsersLists();
            if(!((Manager) user).isAdmin()){
                buttonEditUser.setDisable(true);
                buttonDeleteUser.setVisible(false);
            }
        }else{
            usersTab.getTabPane().getTabs().remove(usersTab);
            buttonsGroupTrucks.setVisible(false);

        }
        fillTrucksList();
        fillTripsList();


    }

    private void fillTripsList() {
        Trip trip = new Trip();
        List<Trip> trips = HibernateCRUD.getAllEntity(trip);
        trips.forEach(c->listViewTrips.getItems().add(c));
    }

    private void fillUsersLists(){
        List<User> allUsers = HibernateCRUD.getAllUsers();
        for(User u : allUsers){
            TableViewUserParameters tableViewUserParameters = new TableViewUserParameters();
            tableViewUserParameters.setIdColumn(String.valueOf(u.getId()));
            tableViewUserParameters.setName(u.getName());
            tableViewUserParameters.setLastName(u.getLastName());
            tableViewUserParameters.setRole(String.format(String.valueOf(u.getClass().getSimpleName())));
            data.add(tableViewUserParameters);
        }
        usersTableView.setItems(data);
    }
    private void fillTrucksList(){
        Truck truck = new Truck();
        List<Truck> trucks = HibernateCRUD.getAllEntity(truck);
        trucks.forEach(c->listViewTrucks.getItems().add(c));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumns.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idColumn"));
    }

    public void addNewTruck(ActionEvent actionEvent) throws IOException {
        TruckWindowController truckWindowController = new TruckWindowController();
        truckWindowController.openNewWindow();
    }

    public void editSelectedTruck(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Truck-window.fxml"));
        Parent root = loader.load();
        TruckWindowController truckWindowController = loader.getController();
        truckWindowController.setTruck(listViewTrucks.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelectedTruck(ActionEvent actionEvent) {
        HibernateCRUD.deleteObject(listViewTrucks.getSelectionModel().getSelectedItem());
        listViewTrucks.getItems().clear();
        fillTrucksList();
    }

    public void refreshTrucksList(ActionEvent actionEvent) {
        listViewTrucks.getItems().clear();
        fillTrucksList();
    }

    public void addNewTrip(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/trips-window.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void editSelectedTrip(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/trips-window.fxml"));
        Parent root = loader.load();
        TripsWindowController tripsWindowController = loader.getController();
        tripsWindowController.setTrip(listViewTrips.getSelectionModel().getSelectedItem());
        tripsWindowController.refreshStopPints();
        tripsWindowController.setUpdateMode();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelectedTrip(ActionEvent actionEvent) {
        HibernateCRUD.deleteObject(listViewTrips.getSelectionModel().getSelectedItem());
        listViewTrips.getItems().clear();
        fillTripsList();
    }

    public void refreshTrips(ActionEvent actionEvent) {
        listViewTrips.getItems().clear();
        fillTripsList();
    }
    public void editSelectedUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/signup-window.fxml"));
        Parent root = loader.load();
        SignupWindowController signupWindowController = loader.getController();
        signupWindowController.setUser(HibernateCRUD.getUserById(
                Integer.parseInt(usersTableView.getSelectionModel().getSelectedItem().getIdColumn())));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelectedUser(ActionEvent actionEvent) {
        HibernateCRUD.deleteObject(HibernateCRUD.getUserById(
                Integer.parseInt(usersTableView.getSelectionModel().getSelectedItem().getIdColumn())));
        usersTableView.getSelectionModel().clearSelection();
    }

    public void createNewCargo(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/cargo-window.fxml"));
        Parent root = loader.load();
        CargoWindowController cargoWindowController = loader.getController();
        cargoWindowController.setCargo(listViewCargo.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateSelectedCargo(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/cargo-window.fxml"));
        Parent root = loader.load();
        CargoWindowController cargoWindowController = loader.getController();
        cargoWindowController.setCargo(listViewCargo.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelectedCargo(ActionEvent actionEvent) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if(user.getClass().equals(Manager.class)){
            if(!((Manager) user).isAdmin()){
                if(usersTableView.getSelectionModel().getSelectedItem().getIdColumn().equals(String.valueOf(user.getId()))){
                    buttonEditUser.setDisable(false);
                }else{
                    buttonEditUser.setDisable(true);
                }
            }
        }
    }
}
