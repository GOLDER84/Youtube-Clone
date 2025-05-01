package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.PremiumSubscriptionPackages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PremiumPackagesPanel {
    private final ArrayList<String> packageName = new ArrayList<>();
    UserController userController = UserController.getInstance();
    DatabaseController databaseController = DatabaseController.getInstance();
    private Stage stage;
    private void handlePackageSelection(CheckBox checkBox , String premiumPackage) {
        if (checkBox.isSelected()) {
            if (packageName.isEmpty()){
                packageName.add(premiumPackage);
            } else {
                checkBox.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("You have selected premium packages!");
                alert.showAndWait();
            }
        }else {
            packageName.remove(premiumPackage);
        }
    }

    @FXML
    private Button backBtn;

    @FXML
    private CheckBox bronzeBtn;

    @FXML
    private Button buyBtn;

    @FXML
    private CheckBox goldBtn;

    @FXML
    private CheckBox silverBtn;

    @FXML
    void backClicked(MouseEvent event) throws IOException {
        this.stage = HelloApplication.primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Library.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void bronzeClicked(MouseEvent event) {
        handlePackageSelection(bronzeBtn , "Bronze");
    }

    @FXML
    void buyClicked(MouseEvent event) throws IOException {
        if (packageName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one package!");
            alert.showAndWait();
        }else {
            String result = userController.buySubscription(PremiumSubscriptionPackages.valueOf(packageName.getFirst().toUpperCase()));
            if (result.contains("purchased") || result.contains("renewed")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK){
                    this.stage = HelloApplication.primaryStage;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void goldClicked(MouseEvent event) {
        handlePackageSelection(goldBtn , "Gold");
    }

    @FXML
    void silverClicked(MouseEvent event) {
        handlePackageSelection(silverBtn , "Silver");
    }

}
