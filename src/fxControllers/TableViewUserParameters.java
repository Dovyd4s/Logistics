package fxControllers;

import javafx.beans.property.SimpleStringProperty;

public class TableViewUserParameters {
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty lastName = new SimpleStringProperty();
    private SimpleStringProperty role = new SimpleStringProperty();
    private SimpleStringProperty idColumn = new SimpleStringProperty();



    public String getIdColumn() {
        return idColumn.get();
    }

    public SimpleStringProperty idColumnProperty() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn.set(idColumn);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public TableViewUserParameters(){
    }

    public TableViewUserParameters(SimpleStringProperty name, SimpleStringProperty lastName, SimpleStringProperty role, SimpleStringProperty idColumn) {
        this.name = name;
        this.lastName = lastName;
        this.role = role;
        this.idColumn = idColumn;
    }
}
