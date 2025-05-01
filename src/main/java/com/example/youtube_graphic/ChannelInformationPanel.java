package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChannelInformationPanel implements Initializable {
    private Stage stage;

    @FXML
    private ImageView channelCoverImage;

    @FXML
    private Label channelNameLabel;

    @FXML
    private TextArea channelDescriptionArea;

    @FXML
    private Label followersCountLabel;

    @FXML
    private Button followButton;

    @FXML
    private ListView<String> playlistListView;

    @FXML
    private FlowPane showContentFlowPane;

    private Channel selectedChannel;
    private final UserController userController = UserController.getInstance();
    private final DatabaseController databaseController = DatabaseController.getInstance();
    private final List<Content> displayedContents = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void loadChannelInfo(String channelName) {
        for (Channel ch : databaseController.getChannels()) {
            if (ch.getChannelName().equals(channelName)) {
                selectedChannel = ch;
                break;
            }
        }

        if (selectedChannel != null) {
            channelNameLabel.setText(selectedChannel.getChannelName());
            channelDescriptionArea.setText(selectedChannel.getChannelDescription());
            followersCountLabel.setText(String.valueOf(selectedChannel.getSubscribersList().size()));

            if (selectedChannel.getChannelCover() != null) {
                File coverFile = new File(selectedChannel.getChannelCover());
                if (coverFile.exists()) {
                    channelCoverImage.setImage(new Image(coverFile.toURI().toString()));
                }
            }

            playlistListView.getItems().clear();
            for (Playlist playlist : selectedChannel.getPlaylists()) {
                playlistListView.getItems().add(playlist.getPlaylistName());
            }

            displayedContents.clear();


            for (Playlist playlist : selectedChannel.getPlaylists()) {
                for (Content content : playlist.getContentList()) {
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding: 5;");

                    ImageView cover = new ImageView();
                    if (content.getCover() != null && !content.getCover().isEmpty()) {
                        File coverFile = new File(content.getCover());
                        if (coverFile.exists()) {
                            cover.setImage(new Image(coverFile.toURI().toString()));
                        }
                    }
                    cover.setFitHeight(50);
                    cover.setFitWidth(80);
                    cover.setPreserveRatio(true);

                    Label nameLabel = new Label(content.getName());
                    nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                    row.getChildren().addAll(cover, nameLabel);
                    displayedContents.add(content);
                }
            }
            //00
            showContents();
            //00
            if (userController.lastSignedUpUser.getSubscription().contains(selectedChannel)) {
                followButton.setDisable(true);
                followButton.setText("Following");
            } else {
                followButton.setDisable(false);
                followButton.setText("Follow");
            }
        }
    }

    @FXML
    private void onFollowClicked() {
        if (selectedChannel == null) return;

        String result = userController.subscribeChannel(selectedChannel.getChannelId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Subscription Result");
        alert.setHeaderText(null);
        alert.setContentText(result);
        alert.showAndWait();
        loadChannelInfo();
//        if (result.startsWith("Subscribed")) {
//            try {
//                this.stage = HelloApplication.primaryStage;
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
//                Parent root = loader.load();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
    //00
    private void showContents() {
        showContentFlowPane.getChildren().clear();
        displayedContents.clear();

        for (Playlist playlist : selectedChannel.getPlaylists()) {
            for (Content content : playlist.getContentList()) {
                ImageView iv = createImageView(content);
                showContentFlowPane.getChildren().add(iv);
                displayedContents.add(content);
            }
        }
    }

    private ImageView createImageView(Content content) {
        File file = new File(content.getCover());
        Image img;
        if (file.exists()) {
            img = new Image(file.toURI().toString());
        } else {
            img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(120);
        iv.setFitHeight(90);
        iv.setPreserveRatio(true);

        iv.setOnMouseEntered(e -> {
            iv.setScaleX(1.1);
            iv.setScaleY(1.1);
        });

        iv.setOnMouseExited(e -> {
            iv.setScaleX(1.0);
            iv.setScaleY(1.0);
        });

        if (!(content instanceof Podcast)){

            if (!content.isExclusive() && userController.lastSignedUpUser instanceof NormalUser ||(content.isExclusive() && userController.lastSignedUpUser instanceof PremiumUser) ||
                    (content.isExclusive() && (content.getOwnerId() == userController.lastSignedUpUser.getId()))){
                iv.setOnMouseClicked(event ->
                        openContentPlayer(content));
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inaccessibility");
                alert.setHeaderText(null);
                alert.setContentText("this content is exclusive");
                iv.setOnMouseClicked(event -> alert.showAndWait());
            }

        }else {

            if (!content.isExclusive() && userController.lastSignedUpUser instanceof NormalUser ||(content.isExclusive() && userController.lastSignedUpUser instanceof PremiumUser) ||
                    (content.isExclusive() && (content.getOwnerId() == userController.lastSignedUpUser.getId()))){
                iv.setOnMouseClicked(event ->
                        openAudioPlayer(content));
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inaccessibility");
                alert.setHeaderText(null);
                alert.setContentText("this content is exclusive");
                iv.setOnMouseClicked(event -> alert.showAndWait());
            }

        }

        return iv;
    }
    //00
    @FXML
    private void onBackClicked() {
        try {
            this.stage = HelloApplication.primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPlaylistSelected(MouseEvent event) {
//        String selectedName = playlistListView.getSelectionModel().getSelectedItem();
//        if (selectedName == null || selectedChannel == null) return;
//
//        Playlist selectedPlaylist = selectedChannel.getPlaylists().stream()
//                .filter(p -> p.getPlaylistName().equals(selectedName))
//                .findFirst().orElse(null);
//
//        if (selectedPlaylist != null) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("قراره عوض شه"));
//                Parent root = loader.load();
//
//                ChannelPlaylistContentsPanel controller = loader.getController();
//                controller.loadPlaylist(selectedPlaylist);
//
//                HelloApplication.basePageController.setMainContent(root);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        String name = playlistListView.getSelectionModel().getSelectedItem();
        if (name == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChannelPlaylistContent.fxml"));
            Parent root = loader.load();
            ChannelPlaylistContentPanel ctrl = loader.getController();
            ctrl.loadPlaylist(name);
            stage = HelloApplication.primaryStage;
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
//درست کار نمیکرد.فعلا تو آب نمک باشه
//    @FXML
//    private void onContentSelected(MouseEvent event) {
//        int selectedIndex = contentListView.getSelectionModel().getSelectedIndex();
//        if (selectedIndex < 0 || selectedIndex >= displayedContents.size()) return;
//
//        Content selectedContent = displayedContents.get(selectedIndex);
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("ContentPlayer.fxml"));
//            Parent root = loader.load();
//
//            ContentPlayerPanel controller = loader.getController();
//            controller.setContent(selectedContent);
//
//            HelloApplication.basePageController.setMainContent(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private void loadChannelInfo() {
        followersCountLabel.setText(String.valueOf(selectedChannel.getSubscribers().size()));
    }
    private void openContentPlayer(Content content) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ContentPlayer.fxml"));
            Parent root = loader.load();

            ContentPlayerPanel controller = loader.getController();
            controller.setContent(content);

            Stage stage = HelloApplication.primaryStage;
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openAudioPlayer(Content content) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AudioPlayer.fxml"));
            Parent root = loader.load();
            AudioPlayerPanel controller = loader.getController();
            controller.setContent(content);
            Stage stage = HelloApplication.primaryStage;
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
