package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.Channel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SubscriptionsPanel implements Initializable {
    private Stage stage;

    @FXML
    private ListView<String> subscriptionsListView;

    private final DatabaseController databaseController = DatabaseController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        subscriptionsListView.getItems().clear();
        for (Channel channel : databaseController.getChannels()) {
            subscriptionsListView.getItems().add(channel.getChannelName());
        }
    }

    @FXML
    private void onChannelSelected(MouseEvent event) {
        String selectedChannelName = subscriptionsListView.getSelectionModel().getSelectedItem();
        if (selectedChannelName == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChannelInformation.fxml"));
            Parent root = loader.load();

            ChannelInformationPanel controller = loader.getController();
            controller.loadChannelInfo(selectedChannelName);
            stage = HelloApplication.primaryStage;
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
