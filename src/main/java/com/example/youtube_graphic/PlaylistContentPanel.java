package com.example.youtube_graphic;

import Controller.ChannelController;
import Controller.ContentController;
import Controller.DatabaseController;
import Controller.UserController;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlaylistContentPanel {

    @FXML
    private Label playlistNameLabel;

    @FXML
    private FlowPane contentFlowPane;

    @FXML
    private Button addContentBtn;

    @FXML
    private Button backBtn;

    private final UserController userController = UserController.getInstance();
    private final ContentController contentController = ContentController.getInstance();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private final ChannelController channelController = ChannelController.getInstance();
    private Playlist selectedPlaylist;

    private boolean isAllContentMode = false;

    public void loadPlaylist(String playlistName) {
        if (playlistName.equals("All Content")) {
            selectedPlaylist = userController.lastSignedUpUser.getUserChannel().getPlaylistById(userController.lastSignedUpUser.getUserChannel().getAllContentPlaylistId());
            isAllContentMode = true;
        } else {
            selectedPlaylist = userController.lastSignedUpUser.getPlaylists().stream()
                    .filter(pl -> pl.getPlaylistName().equals(playlistName))
                    .findFirst()
                    .orElse(null);
            isAllContentMode = false;
        }

        if (selectedPlaylist != null) {
            playlistNameLabel.setText(selectedPlaylist.getPlaylistName());
            showContents();
        }
    }

    private void showContents() {
        contentFlowPane.getChildren().clear();
        for (Content content : selectedPlaylist.getContentList()) {
            ImageView iv = createImageView(content);
            contentFlowPane.getChildren().add(iv);
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
//        iv.setOnMouseClicked(e -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("ContentPlayer.fxml"));
//                Parent root = loader.load();
//                ContentPlayerPanel controller = loader.getController();
//                controller.setContent(content);
//
//                Stage stage = HelloApplication.primaryStage;
//                stage.setScene(new Scene(root));
//                stage.show();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });

        return iv;
    }

//    @FXML
//    private void addContentClicked(MouseEvent event) {
//        ArrayList<Content> allContents = new ArrayList<>(databaseController.getContents());
//        if (allContents.isEmpty()) {
//            new Alert(Alert.AlertType.INFORMATION, "No content available!").showAndWait();
//            return;
//        }
//
//        ChoiceDialog<Content> dialog = new ChoiceDialog<>(allContents.get(0), allContents);
//        dialog.setTitle("Add Content");
//        dialog.setHeaderText("Select a content to add to the playlist:");
//        dialog.setContentText("Content:");
//
//        dialog.showAndWait().ifPresent(selectedContent -> {
//            String result = userController.addContentToPlaylist(selectedPlaylist.getId(), selectedContent.getId());
//            if (result.equals("Content added successfully")) {
//                showContents(); // بعد از افزودن، لیست را بروزرسانی کن
//                new Alert(Alert.AlertType.INFORMATION, "Content added to playlist!").showAndWait();
//            } else {
//                new Alert(Alert.AlertType.ERROR, result).showAndWait();
//            }
//        });
//    }
    @FXML
    private void addContentClicked(MouseEvent event) {
        if (selectedPlaylist == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No playlist selected.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Content");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter content name:");

        dialog.showAndWait().ifPresent(contentName -> {
            Content content = contentController.findContentByName(contentName);
            if (content != null) {
                String result;
                if (isAllContentMode) {
                    result = channelController.addContentToChannel(content.getId());
                } else {
                    result = userController.addContentToPlaylist(selectedPlaylist.getId(), content.getId());
                }
                showAlert(result);
                showContents(); // بروزرسانی لیست
            } else {
                showAlert("Content not found.");
            }
        });
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void backClicked(MouseEvent event) throws IOException {
        Stage stage = HelloApplication.primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
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
