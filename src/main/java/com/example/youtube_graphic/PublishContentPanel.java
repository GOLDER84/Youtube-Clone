package com.example.youtube_graphic;

import Controller.ContentController;
import Controller.ChannelController;
import Model.Channel;
import Model.Playlist;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PublishContentPanel implements Initializable {
    private Stage stage;

    @FXML
    private ComboBox<String> contentTypeComboBox;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField durationField;

    @FXML
    private TextField fileLinkField;

    @FXML
    private TextField coverLinkField;

    @FXML
    private CheckBox isExclusiveCheckBox;

    @FXML
    private TextField subtitlesField;

    @FXML
    private TextField referenceMusicField;

    @FXML
    private TextField scheduledTimeField;

    @FXML
    private ComboBox<String> qualityComboBox;

    @FXML
    private ComboBox<String> formatComboBox;

    private final ContentController contentController = ContentController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contentTypeComboBox.getItems().addAll("Podcast", "Normal Video", "Short Video", "Live Stream");
        categoryComboBox.getItems().addAll("News", "Game", "Podcast", "Music", "Live", "Society", "History");
        qualityComboBox.getItems().addAll("SD_360", "HD_720", "HD_1080");
        formatComboBox.getItems().addAll("MP4", "MKV", "MOV", "WMV");

        contentTypeComboBox.setOnAction(e -> updateVisibleFields());
        updateVisibleFields();
    }

    @FXML
    private void updateVisibleFields() {
        String selected = contentTypeComboBox.getValue();
        boolean isNormalVideo = "Normal Video".equals(selected);
        boolean isShortVideo = "Short Video".equals(selected);
        boolean isLiveStream = "Live Stream".equals(selected);

        subtitlesField.setVisible(isNormalVideo || isShortVideo);
        referenceMusicField.setVisible(isShortVideo);
        scheduledTimeField.setVisible(isLiveStream);
        qualityComboBox.setVisible(isNormalVideo);
        formatComboBox.setVisible(isNormalVideo);
    }

    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Content File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.mov", "*.wmv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileLinkField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void chooseCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Cover Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            coverLinkField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void onPublishContent(ActionEvent event) {
        String type = contentTypeComboBox.getValue();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String category = categoryComboBox.getValue();
        String duration = durationField.getText();
        String fileLink = fileLinkField.getText();
        String cover = coverLinkField.getText();
        String isExclusive = isExclusiveCheckBox.isSelected() ? "Y" : "N";

        if (title.isEmpty() || description.isEmpty() || category == null || duration.isEmpty() || fileLink.isEmpty() || cover.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").showAndWait();
            return;
        }
        String currectQuality = qualityComboBox.getValue();
        if (!type.equals("Podcast")) {
            if (currectQuality.equals("SD_360")) {
                currectQuality = "P360";
            }else if (currectQuality.equals("HD_720")) {
                currectQuality = "P720";
            } else if (currectQuality.equals("HD_1080")) {
                currectQuality = "P1080";
            }
        }

        String result = "";

        switch (type) {
            case "Podcast":
                result = contentController.createPodcast(title, description, duration, category, fileLink, cover, isExclusive);
                break;
            case "Normal Video":
                result = contentController.createNormalVideo(title, isExclusive, description, duration, category, fileLink, cover,
                        subtitlesField.getText(), currectQuality, formatComboBox.getValue());
                break;
            case "Short Video":
                result = contentController.createShortVideo(title, isExclusive, description, duration, category, fileLink, cover,
                        subtitlesField.getText(), referenceMusicField.getText());
                break;
            case "Live Stream":
                result = contentController.createLiveStream(title, isExclusive, description, duration, category, fileLink, cover,
                        subtitlesField.getText(), scheduledTimeField.getText());
                break;
        }

        new Alert(Alert.AlertType.INFORMATION, "Content published successfully!").showAndWait();
        goToChannel();
    }

    @FXML
    private void onBack(ActionEvent event) {
        goToChannel();
    }

    private void goToChannel() {
        try {
            Stage stage = HelloApplication.primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
