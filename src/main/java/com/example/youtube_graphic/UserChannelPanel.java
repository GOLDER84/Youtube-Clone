package com.example.youtube_graphic;

import Controller.ChannelController;
import Controller.UserController;
import Model.Channel;
import Model.Playlist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserChannelPanel implements Initializable {

    @FXML private ImageView channelCover;
    @FXML private Label channelName;
    @FXML private Label channelDescription;
    @FXML private Label subscriberCount;
    @FXML private ListView<String> playlistListView;
    @FXML private Button createChannelPlaylistBtn;
    @FXML private Button backBtn;

    private final UserController userController = UserController.getInstance();
    private final ChannelController channelController = ChannelController.getInstance();
    private Channel channel;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // گرفتن کانال کاربر
        channel = userController.lastSignedUpUser.getUserChannel();
        if (channel == null) {
            new Alert(Alert.AlertType.WARNING, "You do not have a channel yet!").showAndWait();
            return;
        }

        // مقداردهی اولیه‌ی فیلدها
        channelName.setText(channel.getChannelName());
        channelDescription.setText(channel.getChannelDescription());
        subscriberCount.setText(String.valueOf(channel.getSubscribersList().size()));

        File coverFile = new File(channel.getChannelCover());
        if (coverFile.exists()) {
            channelCover.setImage(new Image(coverFile.toURI().toString()));
        }

        // نمایش لیست پلی‌لیست‌ها (فقط آن‌ها که در channel.getPlaylists() هستند)
        playlistListView.getItems().clear();
        for (Playlist pl : channel.getPlaylists()) {
            playlistListView.getItems().add(pl.getPlaylistName());
        }

        stage = HelloApplication.primaryStage;
    }

    @FXML
    void onCreateChannelPlaylist(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Channel Playlist");
        dialog.setHeaderText("Enter a name for your new playlist:");
        dialog.setContentText("Playlist Name:");

        dialog.showAndWait().ifPresent(playlistName -> {
            String result = channelController.createChannelPlaylist(playlistName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION , result , ButtonType.OK);
            alert.showAndWait();

            if (result.startsWith("Playlist ")) {
                playlistListView.getItems().add(playlistName);
            }
        });
    }

    @FXML
    private void onPlaylistSelected(MouseEvent event) {
        String selected = playlistListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlaylistContent.fxml"));
            Parent root = loader.load();

            // به کنترلر بعدی نام پلی‌لیست را می‌دهیم
            PlaylistContentPanel ctrl = loader.getController();
            ctrl.loadPlaylist(selected);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open playlist: " + e.getMessage()).showAndWait();
        }
    }
    @FXML
    private void onPublishContent() {
        try {
            // لود کردن صفحه انتشار محتوا
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PublishContent.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Publish Content page: " + e.getMessage()).showAndWait();
        }
    }


}
