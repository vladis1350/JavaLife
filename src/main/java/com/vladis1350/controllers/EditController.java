package com.vladis1350.controllers;

import com.vladis1350.bean.Human;
import com.vladis1350.db_configuration.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lb;

    @FXML
    private TextField nameEditField;

    @FXML
    private TextField ageEditField;

    @FXML
    private DatePicker dateEditField;

    @FXML
    private Button saveEditButton;

    @FXML
    private Button cancelEditButton;

    public void setNameEditField(TextField nameEditField) {
        this.nameEditField = nameEditField;
    }

    public void setAgeEditField(TextField ageEditField) {
        this.ageEditField = ageEditField;
    }

    public void setDateEditField(DatePicker dateEditField) {
        this.dateEditField = dateEditField;
    }

    public TextField getNameEditField() {
        return nameEditField;
    }

    public TextField getAgeEditField() {
        return ageEditField;
    }

    public DatePicker getDateEditField() {
        return dateEditField;
    }

    @FXML
    void initialize() throws IOException {

        saveEditButton.setOnAction(event -> {
            try {
                updateSelectedHuman();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelEditButton.setOnAction(event -> {
            cancelEditButton.getScene().getWindow().hide();
        });
    }

    private void showAlertWithoutHeaderText(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Зполните все поля");

        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private boolean updateSelectedHuman() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        boolean isName = nameEditField.getText().equals("");
        boolean isAge = ageEditField.getText().equals("");
        boolean isBirthday = dateEditField.getValue().equals("");
        if (isName || isAge || isBirthday) {
            showAlertWithoutHeaderText("Fill in all the fields");
        } else {
            Human human = new Human(Long.valueOf(lb.getText()), nameEditField.getText(), Integer.parseInt(ageEditField.getText()), dateEditField.getValue());
            dbHandler.update(human);
            showAlertWithoutHeaderText("Changes saved successfully!");
            return true;
        }
        return false;
    }

}
