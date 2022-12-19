package fxControllers;

import entities.*;
import hibernate.HibernateCRUD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Button buttonAddNewTrip;
    public Button buttonDeleteSelectedTrip;
    public Group groupButtonsCargo;
    public TreeView forumTreeView;
    public TextField textFieldNewForumName;
    public TextField textFieldCommentTitle;
    public TextArea textAreaComment;
    public Button buttonCreateNewForum;
    public Button buttonCreateNewComment;
    public Label labelReplyAddNewThread;
    public Label labelAddNewSubForum;
    public RadioButton radioButtonStatusComplete;
    public ToggleGroup StatusRadioGroup;
    public RadioButton radioButtonStatusInProgress;
    public RadioButton radioButtonStatusNotStarted;
    public RadioButton radioButtonStatusShowAll;
    public ChoiceBox filterChoiceBoxDriver;
    public DatePicker pickupDateFilter;
    public DatePicker deliveryDateFilter;
    public Button buttonEditSelectedTrip;
    public Button buttonShowStatistics;
    private TreeItem root;
    private User user;
    public Tab usersTab;
    public TableView<TableViewUserParameters> usersTableView;
    public TableColumn<TableViewUserParameters, String> idColumn;
    public TableColumn<TableViewUserParameters, String> nameColumns;
    public TableColumn<TableViewUserParameters, String> lastNameColumn;
    public TableColumn<TableViewUserParameters, String> roleColumn;
    private ObservableList<TableViewUserParameters> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumns.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idColumn"));

        fillDriversBox ();

        Forum rootForum = new Forum();
        rootForum.setTitle("root cat.");
        root = new TreeItem(rootForum);

        List<Forum> forums = HibernateCRUD.getAllEntity(rootForum);
        List<Comment> comments = HibernateCRUD.getAllEntity(new Comment());
        for(Forum forum : forums){
            if(forum.getParentForum()==null) {
                TreeItem<Forum> treeItem = new TreeItem<>(forum);

                treeGenerator(treeItem,forums,comments);

                root.getChildren().add(treeItem);
            }
        }

        forumTreeView.setRoot(root);
        root.setExpanded(true);
    }
    private void refreshForum(){
        root.getChildren().clear();
        List<Forum> forums = HibernateCRUD.getAllEntity(new Forum());
        List<Comment> comments = HibernateCRUD.getAllEntity(new Comment());
        for(Forum forum : forums){
            if(forum.getParentForum()==null) {
                TreeItem<Forum> treeItem = new TreeItem<>(forum);
                treeGenerator(treeItem,forums,comments);

                root.getChildren().add(treeItem);
            }
        }
    }
    private void treeGenerator (TreeItem t, List<Forum> forums, List<Comment> comments){
        if(t.getValue().getClass().equals(Forum.class)){
            Forum forum = (Forum) t.getValue();
            for(Forum f : forums){
                if(f.getParentForum()!= null && f.getParentForum().equals(forum)){
                    TreeItem treeItem = new TreeItem(f);
                    t.getChildren().add(treeItem);
                    treeGenerator(treeItem, forums, comments);
                }
            }
            for(Comment c : comments){
                if(c.getParentForum() != null && c.getParentForum().equals(forum)){
                    TreeItem<Comment> treeItem = new TreeItem<>(c);
                    t.getChildren().add(treeItem);
                    treeGenerator(treeItem,forums,comments);
                }
            }
        } else if (t.getValue().getClass().equals(Comment.class)) {
            Comment selectedComment = (Comment) t.getValue();
            for(Comment c : comments){
                if(c.getParentComment()!= null && c.getParentComment().equals(selectedComment)){
                    TreeItem<Comment> treeItem = new TreeItem<>(c);
                    t.getChildren().add(treeItem);
                    treeGenerator(treeItem,forums,comments);
                }
            }
        }
    }
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
            buttonAddNewTrip.setDisable(true);
            buttonDeleteSelectedTrip.setDisable(true);
            groupButtonsCargo.setDisable(true);
        }
        fillTrucksList();
        fillTripsList();
        fillCargoList();
    }

    private void fillTripsList() {
        List<Trip> trips = HibernateCRUD.getAllEntity(new Trip());
        if(user.getClass().equals(Manager.class)){
            trips.forEach(c->listViewTrips.getItems().add(c));
        }else{
            trips.forEach(c->{
                if(c.getAssignedDriver()==null || c.getAssignedDriver().equals(user)){
                    listViewTrips.getItems().add(c);
                }
            });
        }
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
    private void fillCargoList(){
        listViewCargo.getItems().clear();
        Cargo cargo = new Cargo();
        List<Cargo> cargos = HibernateCRUD.getAllEntity(cargo);
        cargos.forEach(c->listViewCargo.getItems().add(c));
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
        TripsWindowController tripsWindowController = loader.getController();
        tripsWindowController.setUser(user);
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
        tripsWindowController.setUser(user);
        tripsWindowController.refreshStopPints();
        tripsWindowController.setUpdateMode();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelectedTrip(ActionEvent actionEvent) {
        if(!(listViewTrips.getSelectionModel().getSelectedItem().isInProcess() || listViewTrips.getSelectionModel().getSelectedItem().isComplete())){
            HibernateCRUD.deleteObject(listViewTrips.getSelectionModel().getSelectedItem());
            listViewTrips.getItems().clear();
            fillTripsList();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Selected trip is already in process or complete.");
            alert.showAndWait();
        }
    }

    public void refreshTrips(ActionEvent actionEvent) {
        listViewTrips.getItems().clear();
        fillTripsList();
    }
    public void refreshTrips() {
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
        System.out.println(listViewCargo.getSelectionModel().getSelectedItem());
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

    public void mouseClickedOnForum(MouseEvent mouseEvent) {
        TreeItem<?> selection = (TreeItem<?>) forumTreeView.getSelectionModel().getSelectedItem();
        if(selection != null && selection.getValue().getClass().equals(Forum.class)){
            buttonCreateNewComment.setDisable(false);
            buttonCreateNewForum.setDisable(false);
            labelAddNewSubForum.setText("Create new SubForum:");
            labelReplyAddNewThread.setText("Create new Thread:");
            textFieldCommentTitle.setDisable(false);
            textAreaComment.setDisable(false);
            textFieldNewForumName.setDisable(false);
        } else if (selection != null && selection.getValue().getClass().equals(Comment.class)) {
            buttonCreateNewForum.setDisable(true);
            buttonCreateNewComment.setDisable(false);
            textFieldCommentTitle.setDisable(true);
            textAreaComment.setDisable(false);
            textFieldNewForumName.setDisable(true);
            labelReplyAddNewThread.setText("Reply to selected comment:");
        }
        if(selection != null && selection.equals(root)){
            buttonCreateNewComment.setDisable(true);
            buttonCreateNewForum.setDisable(false);
            labelAddNewSubForum.setText("Add New Forum:");
            textFieldCommentTitle.setDisable(true);
            textAreaComment.setDisable(true);
            textFieldNewForumName.setDisable(false);
        }
    }
    private void fillDriversBox (){
        Driver driver = new Driver();
        List<Driver> drivers = new ArrayList<>();
        drivers = HibernateCRUD.getAllEntity(driver);
        drivers.forEach(c->filterChoiceBoxDriver.getItems().add(c));
        filterChoiceBoxDriver.getItems().add("Show All");
        filterChoiceBoxDriver.getItems().add("W/O assigned driver");
        filterChoiceBoxDriver.setValue("Show All");
        filterChoiceBoxDriver.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filterSelection();
            }
        });
    }
    public void addNewSubForum(ActionEvent actionEvent) {
        TreeItem selected = (TreeItem) forumTreeView.getSelectionModel().getSelectedItem();
        Forum forum = new Forum();
        forum.setTitle(textFieldNewForumName.getText());
        if(selected.equals(root)){
            forum.setParentForum(null);
        }else{
            forum.setParentForum((Forum) selected.getValue());
        }
        HibernateCRUD.createObject(forum);
        refreshForum();
    }

    public void buttonAddNewComment(ActionEvent actionEvent) {
        TreeItem<?> selectedItem = (TreeItem<?>) forumTreeView.getSelectionModel().getSelectedItem();

        Comment comment = new Comment();
        comment.setTitle(textFieldCommentTitle.getText());
        comment.setCommentText(textAreaComment.getText());
        comment.setAuthor(user);
        comment.setCreatedDateTime(LocalDateTime.now());

        if(selectedItem.getValue().getClass().equals(Forum.class)){
            comment.setParentForum((Forum) selectedItem.getValue());
        } else if (selectedItem.getValue().getClass().equals(Comment.class)) {
            comment.setParentComment((Comment) selectedItem.getValue());
        }
        HibernateCRUD.createObject(comment);
        refreshForum();
    }

    public void filterSelection() {
        refreshTrips();
        List<Trip>filterOutList = new ArrayList<>();

        if(!radioButtonStatusShowAll.isSelected()){
            for(Trip trip : listViewTrips.getItems()){
                if(radioButtonStatusComplete.isSelected()){
                    if(!trip.isComplete()){
                        filterOutList.add(trip);
                    }
                } else if (radioButtonStatusInProgress.isSelected()) {
                    if(!trip.isInProcess()){
                        filterOutList.add(trip);
                    }
                }else if (radioButtonStatusNotStarted.isSelected()){
                    if((trip.isComplete() || trip.isInProcess())){
                        filterOutList.add(trip);
                    }
                }
            }
            listViewTrips.getItems().removeAll(filterOutList);
            filterOutList.clear();
        }

        if(!filterChoiceBoxDriver.getValue().equals("Show All")){
            for(Trip trip : listViewTrips.getItems()){
                if(filterChoiceBoxDriver.getValue().equals("W/O assigned driver")){
                    if(trip.getAssignedDriver() != null){
                        filterOutList.add(trip);
                    }
                }else {
                    if(trip.getAssignedDriver() == null || (!trip.getAssignedDriver().equals(filterChoiceBoxDriver.getValue()))){
                        filterOutList.add(trip);
                    }
                }
            }
            listViewTrips.getItems().removeAll(filterOutList);
            filterOutList.clear();
        }

        if(pickupDateFilter.getValue()!=null){
            for(Trip trip : listViewTrips.getItems()){
                if(trip.getCargo() == null || trip.getCargo().getReadyForPickUpDate()==null){
                    filterOutList.add(trip);
                } else if (!trip.getCargo().getReadyForPickUpDate().equals(pickupDateFilter.getValue())){
                    filterOutList.add(trip);
                }
            }
            listViewTrips.getItems().removeAll(filterOutList);
            filterOutList.clear();
        }
        if(deliveryDateFilter.getValue()!=null){
            for(Trip trip : listViewTrips.getItems()){
                if(trip.getCargo() == null || trip.getCargo().getMustBeDeliveredUntilDate()==null){
                    filterOutList.add(trip);
                } else if(!trip.getCargo().getMustBeDeliveredUntilDate().equals(deliveryDateFilter.getValue())){
                    filterOutList.add(trip);
                }
            }
            listViewTrips.getItems().removeAll(filterOutList);
            filterOutList.clear();
        }
    }

    public void clickedOnTripsList(MouseEvent mouseEvent) {
        if(listViewTrips.getSelectionModel().getSelectedItem()!=null){
            buttonShowStatistics.setDisable(false);
            buttonEditSelectedTrip.setDisable(false);
            buttonDeleteSelectedTrip.setDisable(false);
        }
    }

    public void showStatistics(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/trip-statistics.fxml"));
        Parent root = loader.load();
        TripStatisticsController tripStatisticsController = loader.getController();
        tripStatisticsController.setTrip(listViewTrips.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
