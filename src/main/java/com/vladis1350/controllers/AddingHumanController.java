package com.vladis1350.controllers;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.vladis1350.bean.Human;
import com.vladis1350.db_configuration.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddingHumanController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    MainController controller = new MainController();

    @FXML
    void initialize() {
        saveButton.setOnAction(event -> {
            addHuman();
            try {
                controller.updateTreeTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            saveButton.getScene().getWindow().hide();
        });

        cancelButton.setOnAction(event -> {
            cancelButton.getScene().getWindow().hide();
        });
    }

    private boolean checkHumanData(String name, int age, LocalDate date) {
        return !name.equals("") && age > 0 && !date.equals("");
    }

    private void addHuman() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String nameText = nameField.getText().trim();
        int age = Integer.parseInt(ageField.getText().trim());
        LocalDate date = dateField.getValue();
        if (checkHumanData(nameText, age, date)) {
            Human human = new Human(nameText, age, date);
            try {
                dbHandler.save(human);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("gooood");
    }
}

