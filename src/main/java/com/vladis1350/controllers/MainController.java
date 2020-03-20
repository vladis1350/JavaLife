package com.vladis1350.controllers;

import com.vladis1350.bean.Human;
import com.vladis1350.db_configuration.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public TreeTableView<Human> treeTable = new TreeTableView<>();

    @FXML
    private TreeTableColumn<Human, String> columnName = new TreeTableColumn<>();

    @FXML
    private TreeTableColumn<Human, Integer> columnAge = new TreeTableColumn<Human, Integer>();

    @FXML
    private TreeTableColumn<Human, LocalDate> columnBirthday = new TreeTableColumn<Human, LocalDate>();

    @FXML
    private Button addHuman;

    @FXML
    private Button editHuman;

    @FXML
    private Button deleteHuman;

    @FXML
    private Button update;

    private ObservableList<Human> observableList = FXCollections.observableArrayList();
    private DatabaseHandler dbHandler = new DatabaseHandler();

    private EditController editController;

    @FXML
    void initialize() throws SQLException {

        readFromDatabase();
        updateTreeTableView();
        addHuman.setOnAction(event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/adding.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });

        editHuman.setOnAction(event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/edit.fxml"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            asd(root);
        });

        treeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                checkingDateSelectedHuman();
            }
        });

        update.setOnAction(event -> {
            treeTable.getRoot().getChildren().clear();
            try {
                updateTreeTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        deleteHuman.setOnAction(e -> {
            try {
                delSelectedHuman();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

    }

    public void asd(Parent root) {
        TextField fieldName = (TextField) root.lookup("#nameEditField");
        TextField fieldAge = (TextField) root.lookup("#ageEditField");
        DatePicker fieldDate = (DatePicker) root.lookup("#dateEditField");

        Label lbId = (Label) root.lookup("#lb");
        Human human = getDataSelectedField();
        if (human == null) {
            showAlertWithDefaultHeaderText();
        } else {
            fieldName.setText(human.getName());
            fieldAge.setText(String.valueOf(human.getAge()));
            fieldDate.setValue(human.getBirthday());
            lbId.setText(String.valueOf(human.getId()));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        }
    }

    public Human getDataSelectedField() {
        MultipleSelectionModel<TreeItem<Human>> getSelectionModel = treeTable.getSelectionModel();
        getSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        TreeItem<Human> item = getSelectionModel.getSelectedItem();
        if (getSelectionModel.getSelectedItem() == null) {
            return null;
        }
        return item.getValue();
    }

    public void updateTreeTableView() throws SQLException {
        readFromDatabase();
        columnName.setCellValueFactory(new TreeItemPropertyValueFactory<>("Name"));
        columnAge.setCellValueFactory(new TreeItemPropertyValueFactory<>("Age"));
        columnBirthday.setCellValueFactory(new TreeItemPropertyValueFactory<>("Birthday"));

        Human main = new Human("Main Human", 0, null);
        TreeItem<Human> observableTreeItem = new TreeItem<>(main);
        for (Human human : observableList) {
            TreeItem<Human> treeItem = new TreeItem<>(human);
            observableTreeItem.getChildren().add(treeItem);
        }
        observableTreeItem.setExpanded(true);
        observableTreeItem.getChildren().sort(Comparator.comparing(t -> t.getValue().getName()));
        treeTable.setRoot(observableTreeItem);
    }

    public void readFromDatabase() throws SQLException {
        observableList.clear();
        ResultSet resultSet = dbHandler.findAll();
        while (resultSet.next()) {
            observableList.add(new Human(resultSet.getLong("id_human"), resultSet.getString("name"), resultSet.getInt("age"),
                    resultSet.getDate("birthday").toLocalDate()));
        }
    }

    private void delSelectedHuman() throws SQLException {
        MultipleSelectionModel<TreeItem<Human>> getSelectionModel = treeTable.getSelectionModel();
        getSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        TreeItem<Human> item = getSelectionModel.getSelectedItem();
        if (getSelectionModel.getSelectedItem() != null) {
            dbHandler.delete(item.getValue().getId());
        } else {
            showAlertWithDefaultHeaderText();
        }
        updateTreeTableView();
    }

    private void checkingDateSelectedHuman() {
        MultipleSelectionModel<TreeItem<Human>> getSelectionModel = treeTable.getSelectionModel();
        String[] myDate = {};
        String[] nowDate = LocalDate.now().toString().split("-");
        String myDayAndMonth = "";
        String nowDayAndMonth = "";
        getSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        TreeItem<Human> item = getSelectionModel.getSelectedItem();
        if (item.getValue().getBirthday() != null) {
            myDate = item.getValue().getBirthday().toString().split("-");
            myDayAndMonth = myDate[2] + "." + myDate[1];
            nowDayAndMonth = nowDate[2] + "." + nowDate[1];
            if (equalsDate(nowDayAndMonth, myDayAndMonth)) {
                congratulations(item.getValue());
            }
        }
    }

    private boolean equalsDate(String now, String birthday) {
        return now.equals(birthday);
    }

    private void congratulations(Human human) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("HAPPY BIRTHDAY!!!");

        alert.setHeaderText(null);
        alert.setContentText("HAPPY BIRTHDAY " + human.getName());

        alert.showAndWait();
    }

    private void showAlertWithDefaultHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");

        alert.setHeaderText(null);
        alert.setContentText("Select the field!");

        alert.showAndWait();
    }
}

