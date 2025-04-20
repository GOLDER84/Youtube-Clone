package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LibraryPanel {
    UserController userController = UserController.getInstance();
    DatabaseController databaseController = DatabaseController.getInstance();

    @FXML
    private Label balance;

    @FXML
    private Button changeFullnameBtn;

    @FXML
    private Button changePasswordBtn;

    @FXML
    private ImageView channelCover;

    @FXML
    private Button createChannelBtn;

    @FXML
    private Button createPlaylistBtn;

    @FXML
    private Label email;

    @FXML
    private Label fullName;

    @FXML
    private Button getPremiumBtn;

    @FXML
    private Button inccreaseCreditBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label password;

    @FXML
    private Label phoneNumber;

    @FXML
    private ImageView profileCover;

    @FXML
    private Label subscriptionType;

    @FXML
    private Label username;

    @FXML
    void changeFullnameClicked(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Fullname");
        dialog.setHeaderText("Enter new fullname:");
        dialog.setContentText("Fullname:");

        dialog.showAndWait().ifPresent(newName -> {
            String result = userController.editProfile(newName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Change Fullname Result");
            alert.setHeaderText(null);
            alert.setContentText(result);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK && result.equals("Profile updated successfully")) {
                    fullName.setText(userController.lastSignedUpUser.getFullName());

                    // بروزرسانی در دیتابیس
                    databaseController.updateUser(userController.lastSignedUpUser);
                }
            });
        });
    }


    @FXML
    void changePasswordClicked(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new password:");
        dialog.setContentText("Password:");

        dialog.showAndWait().ifPresent(newPass -> {
            String result = userController.editPassword(newPass);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Change Password Result");
            alert.setHeaderText(null);
            alert.setContentText(result);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK && result.equals("Password updated successfully")) {
                    password.setText(userController.lastSignedUpUser.getPassword());

                    databaseController.updateUser(userController.lastSignedUpUser);
                }
            });
        });
    }


    @FXML
    void channelCoverClicked(MouseEvent event) {

    }

    @FXML
    void createChannelClicked(MouseEvent event) {

    }

    @FXML
    void createPlaylistClicked(MouseEvent event) {

    }

    @FXML
    void getPremiumClicked(MouseEvent event) {

    }

    @FXML
    void increaseCreditClicked(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Increase Credit");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter amount to add:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input);
                String result = userController.addBalance(amount);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Increase Credit");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();

                if (result.contains("credits added")) {
                    balance.setText(String.valueOf(userController.lastSignedUpUser.getBalance()));
                    databaseController.updateUser(userController.lastSignedUpUser);
                }

            } catch (NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Invalid Input");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please enter a valid number.");
                errorAlert.showAndWait();
            }
        });
    }


    @FXML
    void logoutClicked(MouseEvent event) {
        String result = userController.logout();
        showAlertAndGoToMain(result);

    }
    private void showAlertAndGoToMain(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpOrLoginPanel.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
