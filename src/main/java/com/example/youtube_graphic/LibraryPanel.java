package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.PremiumUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryPanel implements Initializable {
    private Stage stage;
    private final UserController userController = UserController.getInstance();
    private final DatabaseController databaseController = DatabaseController.getInstance();

    @FXML
    private ImageView profileCover;
    @FXML
    private Label username, fullName, email, phoneNumber, balance, subscriptionType;
    @FXML
    private Button changePasswordBtn, changeFullnameBtn;
    @FXML
    private Button getPremiumBtn, inccreaseCreditBtn;
    @FXML
    private ImageView channelCover;
    @FXML
    private Button createChannelBtn;
    @FXML
    private Button createPlaylistBtn;
    @FXML
    private ListView<String> playlistListView;
    @FXML
    private Button logoutBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(userController.lastSignedUpUser.getUsername());
        fullName.setText(userController.lastSignedUpUser.getFullName());
        email.setText(userController.lastSignedUpUser.getEmail());
        phoneNumber.setText(userController.lastSignedUpUser.getPhone());
        balance.setText(String.valueOf(userController.lastSignedUpUser.getBalance()));
        subscriptionType.setText(
                (userController.lastSignedUpUser instanceof PremiumUser) ? "Premium" : "Normal"
        );

        File pf = new File(userController.lastSignedUpUser.getProfileCover());
        if (pf.exists()) {
            profileCover.setImage(new Image(pf.toURI().toString()));
        }

        if (userController.lastSignedUpUser.getUserChannel() == null) {
            File df = new File("src/main/resources/Images/nothing.jpg");
            channelCover.setImage(new Image(df.toURI().toString()));
        } else {
            File cf = new File(userController.lastSignedUpUser.getUserChannel().getChannelCover());
            if (cf.exists()) {
                channelCover.setImage(new Image(cf.toURI().toString()));
            }
        }

        loadPlaylists();
    }

    private void loadPlaylists() {
        playlistListView.getItems().clear();
        userController.lastSignedUpUser.getPlaylists()
                .forEach(pl -> playlistListView.getItems().add(pl.getPlaylistName()));
    }

    @FXML
    void changePasswordClicked(MouseEvent event) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Change Password");
        dlg.setHeaderText("Enter new password:");
        dlg.setContentText("Password:");
        dlg.showAndWait().ifPresent(newPass -> {
            String res = userController.editPassword(newPass);
            new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK).showAndWait();
            if (res.equals("Password updated successfully")) {
                balance.setText(userController.lastSignedUpUser.getPassword()); // یا لِیبل مناسب
                databaseController.updateUser(userController.lastSignedUpUser);
            }
        });
    }

    @FXML
    void changeFullnameClicked(MouseEvent event) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Change Fullname");
        dlg.setHeaderText("Enter new fullname:");
        dlg.setContentText("Fullname:");
        dlg.showAndWait().ifPresent(newName -> {
            String res = userController.editProfile(newName);
            new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK).showAndWait();
            if (res.equals("Profile updated successfully")) {
                fullName.setText(userController.lastSignedUpUser.getFullName());
                databaseController.updateUser(userController.lastSignedUpUser);
            }
        });
    }

    @FXML
    void getPremiumClicked(MouseEvent event) throws IOException {
        stage = HelloApplication.primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("PremiumPackages.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void increaseCreditClicked(MouseEvent event) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Increase Credit");
        dlg.setContentText("Enter amount to add:");
        dlg.showAndWait().ifPresent(input -> {
            try {
                double amt = Double.parseDouble(input);
                String res = userController.addBalance(amt);
                new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK).showAndWait();
                balance.setText(String.valueOf(amt));
                loadPlaylists();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid number", ButtonType.OK).showAndWait();
            }
        });
    }

    @FXML
    void createChannelClicked(MouseEvent event) throws IOException {
        stage = HelloApplication.primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("CreateChannel.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void createPlaylistClicked(MouseEvent event) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Create Playlist");
        dlg.setHeaderText("Enter new playlist name:");
        dlg.setContentText("Name:");
        dlg.showAndWait().ifPresent(name -> {
            String res = userController.createPlaylist(name);
            new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK).showAndWait();
            loadPlaylists();
        });
    }

    @FXML
    void playlistSelected(MouseEvent event) {
        String name = playlistListView.getSelectionModel().getSelectedItem();
        if (name == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlaylistContent.fxml"));
            Parent root = loader.load();
            PlaylistContentPanel ctrl = loader.getController();
            ctrl.loadPlaylist(name);
            stage = HelloApplication.primaryStage;
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void channelCoverClicked(MouseEvent event) {
        // پیاده کنید
    }

    @FXML
    void logoutClicked(MouseEvent event) {
        String res = userController.logout();
        new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK).showAndWait();
        if (res.contains("successfully")) {
            try {
                stage = HelloApplication.primaryStage;
                Parent root = FXMLLoader.load(getClass().getResource("SignUpOrLogin.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
