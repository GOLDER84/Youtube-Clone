package com.example.youtube_graphic;

import Controller.UserController;
import Model.Channel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BasePageController {
    private UserController userController = UserController.getInstance();
    private Channel channel;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private void goToHome() {
        loadPage("Home.fxml");
    }

    @FXML
    private void goToLibrary() {
        loadPage("Library.fxml");
    }

    @FXML
    private void goToSubscriptions() {
        loadPage("Subscription.fxml");
    }

    @FXML
    private void goToChannel() {
        channel = userController.lastSignedUpUser.getUserChannel();
        if (channel == null) {
            new Alert(Alert.AlertType.WARNING, "You do not have a channel yet!").showAndWait();
        }else {
            loadPage("UserChannel.fxml");
        }
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            mainContent.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainContent(Parent root) {
        mainContent.getChildren().setAll(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
    }

}
