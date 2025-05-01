package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.Channel;
import Model.Comment;
import Model.Content;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ContentPlayerPanel implements Initializable {
    DatabaseController databaseController = DatabaseController.getInstance();

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playBtn, pauseBtn, stopBtn, backBtn, likeBtn, reportBtn, commentBtn;

    @FXML
    private Slider videoSlider, audioSlider;

    @FXML
    private Label timeLabel, videoNameLabel, descriptionLabel, durationLabel, categoryLabel, viewsLabel, likesLabel, channelNameLabel;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private ListView<String> commentListView;

    @FXML
    private ImageView channelImageView;

    private MediaPlayer mediaPlayer;
    private Content selectedContent;
    private UserController userController = UserController.getInstance();

    public void setContent(Content content) {
        this.selectedContent = content;
        playContent();
        loadVideo();
        showContentDetails();
        loadComments();
    }

    private void playContent() {
        if (selectedContent != null) {
            userController.playContent(selectedContent.getId());
        }
    }

    private void loadVideo() {
        if (selectedContent == null) return;

        try {
            File videoFile = new File(selectedContent.getFileLink());
            if (!videoFile.exists()) {
                showError("Video file not found!");
                return;
            }

            Media media = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                double totalSeconds = media.getDuration().toSeconds();
                videoSlider.setMax(totalSeconds);
                updateDurationLabels(Duration.ZERO, media.getDuration());
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                videoSlider.setValue(newValue.toSeconds());
                updateDurationLabels(newValue, mediaPlayer.getMedia().getDuration());
            });

            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());

            audioSlider.valueProperty().addListener(observable -> {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(audioSlider.getValue() / 100.0);
                }
            });

            videoSlider.setOnMouseReleased(e -> {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(videoSlider.getValue()));
                }
            });

            mediaPlayer.play();

        } catch (Exception e) {
            showError("Error loading video!");
            e.printStackTrace();
        }
    }

    private void showContentDetails() {
        if (selectedContent != null) {
            videoNameLabel.setText("Name: " + selectedContent.getName());
            descriptionLabel.setText("Description: " + selectedContent.getDescription());
            durationLabel.setText("Duration: " + selectedContent.getDuration());
            categoryLabel.setText("Category: " + selectedContent.getCategory().name());
            viewsLabel.setText("Views: " + selectedContent.getViews());
            likesLabel.setText("Likes: " + selectedContent.getLikes());

            Channel channel = databaseController.getChannels().stream()
                    .filter(ch -> ch.getChannelId() == selectedContent.getChannelId())
                    .findFirst().orElse(null);
            if (channel != null) {
                channelNameLabel.setText(channel.getChannelName());
                try {
                    File imageFile = new File(channel.getChannelCover());
                    if (imageFile.exists()) {
                        channelImageView.setImage(new Image(imageFile.toURI().toString()));
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    private void loadComments() {
        commentListView.getItems().clear();
        for (Comment comment : selectedContent.getCommentList()) {
            commentListView.getItems().add(comment.getComment() + " (" + comment.getCommentDate() + ")");
        }
    }

    @FXML
    private void playClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    @FXML
    private void pauseClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    @FXML
    private void stopClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    @FXML
    private void likeClicked() {
        if (selectedContent != null) {
            userController.likeContent(selectedContent.getId());
            likesLabel.setText("Likes: " + selectedContent.getLikes());
        }
    }

    @FXML
    private void reportClicked() {
        if (selectedContent != null) {

            final int contentId = selectedContent.getId();
            System.out.println("Reporting content id (before dialog): " + contentId);

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Report Content");
            dialog.setHeaderText("Enter the reason for reporting:");
            dialog.setContentText("Reason:");

            dialog.showAndWait().ifPresent(reason -> {
                System.out.println("Reporting content id (in lambda): " + contentId);
                String result = userController.reportContent(contentId, reason);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, result, ButtonType.OK);
                alert.showAndWait();
            });
        }
    }


    @FXML
    private void commentClicked() {
        if (selectedContent != null && !commentTextArea.getText().trim().isEmpty()) {
            userController.addComment(selectedContent.getId(), commentTextArea.getText().trim());
            commentTextArea.clear();
            loadComments();
        }
    }

    @FXML
    private void backClicked() {
        try {
            Stage stage = HelloApplication.primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();

            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateDurationLabels(Duration current, Duration total) {
        String currentTime = formatTime(current);
        String totalTime = formatTime(total);
        timeLabel.setText(currentTime + " / " + totalTime);
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void showError(String message) {
        timeLabel.setText(message);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
