package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import Model.Channel;
import Model.Comment;
import Model.Content;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AudioPlayerPanel implements Initializable {
    DatabaseController databaseController = DatabaseController.getInstance();

    @FXML
    private ImageView AudioView;

    @FXML
    private Slider audioSlider;

    @FXML
    private Button backBtn;

    @FXML
    private Label categoryLabel;

    @FXML
    private ImageView channelImageView;

    @FXML
    private Label channelNameLabel;

    @FXML
    private Button commentBtn;

    @FXML
    private ListView<String> commentListView;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Button likeBtn;

    @FXML
    private Label likesLabel;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button reportBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Label timeLabel;

    @FXML
    private Label videoNameLabel;

    @FXML
    private Slider videoSlider;

    @FXML
    private Label viewsLabel;

    private MediaPlayer mediaPlayer;
    private Content selectedContent;
    private final UserController userController = UserController.getInstance();

//    public void setContent(Content content) {
//        this.selectedContent = content;
//        playContent();
//        loadVideo();
//        showContentDetails();
//        loadComments();
//    }
    public void setContent(Content content) {
        System.out.println("HashCode from UI: " + content.hashCode());
        System.out.println("HashCode from DB: " + databaseController.getContentById(content.getId()).hashCode());
        this.selectedContent = content;
        playContent();
        loadVideo();
        showContentDetails();
        loadComments();
    }

    private void playContent() {
        if (selectedContent != null) {
            System.out.println("Calling playContent() from AudioPlayerPanel");
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
//            mediaView.setMediaPlayer(mediaPlayer);

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
//    private void loadImage(){
//        Channel channel = databaseController.getChannels().stream()
//                .filter(ch -> ch.getChannelId() == selectedContent.getChannelId())
//                .findFirst().orElse(null);
//        if (channel != null) {
//            channelNameLabel.setText(channel.getChannelName());
//            try {
//                File imageFile = new File(channel.getChannelCover());
//                if (imageFile.exists()) {
//                    channelImageView.setImage(new Image(imageFile.toURI().toString()));
//                }
//            } catch (Exception ignored) {}
//        }
//        try {
//
//        } catch (Exception ignored) {}

//        Image img = null;
//        try {
//            if (selectedContent.getCover() != null) {
//                URL imageUrl = getClass().getResource("/" + fixPath(selectedContent.getCover()));
//                if (imageUrl != null) {
//                    img = new Image(imageUrl.toExternalForm());
//                }
//            }
//        } catch (Exception e) {
//            img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
//        }
//        AudioView.setImage(img);
//    }

    private void showContentDetails() {
        if (selectedContent != null) {
            videoNameLabel.setText("Name: " + selectedContent.getName());
            descriptionLabel.setText("Description: " + selectedContent.getDescription());
            durationLabel.setText("Duration: " + selectedContent.getDuration());
            categoryLabel.setText("Category: " + selectedContent.getCategory().name());
            System.out.println("Views inside UI after playContent: " + selectedContent.getViews());

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
            //00
            File imageFile = new File(selectedContent.getCover());
            if (imageFile.exists()) {
                AudioView.setImage(new Image(imageFile.toURI().toString()));
            }
            //00
        }
    }

    private void loadComments() {
        commentListView.getItems().clear();
        for (Comment comment : selectedContent.getCommentList()) {
            commentListView.getItems().add(comment.getComment() + " (" + comment.getCommentDate() + ")");
        }
    }

    @FXML
    void backClicked(ActionEvent event) {
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

    @FXML
    void commentClicked(ActionEvent event) {
        if (selectedContent != null && !commentTextArea.getText().trim().isEmpty()) {
            userController.addComment(selectedContent.getId(), commentTextArea.getText().trim());
            commentTextArea.clear();
            loadComments();
        }
    }

    @FXML
    void likeClicked(ActionEvent event) {
        if (selectedContent != null) {
            userController.likeContent(selectedContent.getId());
            likesLabel.setText("Likes: " + selectedContent.getLikes());
        }
    }

    @FXML
    void pauseClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    @FXML
    void playClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    @FXML
    void reportClicked(ActionEvent event) {
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
    void stopClicked(MouseEvent event) {
        if (mediaPlayer != null) mediaPlayer.stop();
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
