package com.example.youtube_graphic;

import Controller.ContentController;
import Controller.UserController;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePanel implements Initializable {

    @FXML
    private FlowPane suggestedFlowPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchBtn;

    @FXML
    private FlowPane searchResultsFlowPane;

    private final UserController userController = UserController.getInstance();
    private final ContentController contentController = ContentController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showSuggestedContents();
    }

    private void showSuggestedContents() {
        String suggestedStr = userController.showSuggestedContents();
        suggestedStr = suggestedStr.replace("[", "").replace("]", "");
        String[] names = suggestedStr.split(",\\s*");

        suggestedFlowPane.getChildren().clear();
        for (String name : names) {
            Content c = contentController.findContentByName(name);
            if (c != null) {
                ImageView iv = createImageViewForContent(c);
                suggestedFlowPane.getChildren().add(iv);
            }
        }
    }

    @FXML
    private void searchClicked() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Search field cannot be empty!");
            alert.showAndWait();
            return;
        }
        String resultsStr = userController.searchContent(query);
        resultsStr = resultsStr.replace("[", "").replace("]", "");
        String[] names = resultsStr.split(",\\s*");

        searchResultsFlowPane.getChildren().clear();
        for (String name : names) {
            Content c = contentController.findContentByName(name);
            if (c != null) {
                ImageView iv = createImageViewForContent(c);
                searchResultsFlowPane.getChildren().add(iv);
            }
        }
    }

    private ImageView createImageViewForContent(Content content) {
        Image img;
        try {
//            File pf = new File(userController.lastSignedUpUser.getProfileCover());
//            if (pf.exists()) {
//                profileCover.setImage(new Image(pf.toURI().toString()));
//            }
            File contentCover = new File(content.getCover());
            if (contentCover.exists()) {
                img = new Image(contentCover.toURI().toString());
            } else {
                img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
            }
            //اصلی اینه
//            if (content.getCover() != null) {
//                URL imageUrl = getClass().getResource("/" + fixPath(content.getCover()));
//                if (imageUrl != null) {
//                    img = new Image(imageUrl.toExternalForm());
//                } else {
//                    img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
//                }
//            } else {
//                img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
//            }
            //اصلی اینه
        } catch (Exception e) {
            img = new Image(getClass().getResource("/Images/nothing.jpg").toExternalForm());
        }

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(180);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0.2, 0.2, 0.2, 0.6));
        imageView.setEffect(dropShadow);

        imageView.setOnMouseEntered(event -> {
            imageView.setScaleX(1.1);
            imageView.setScaleY(1.1);
        });

        imageView.setOnMouseExited(event -> {
            imageView.setScaleX(1.0);
            imageView.setScaleY(1.0);
        });
        if (!(content instanceof Podcast)){

            if (!content.isExclusive() && userController.lastSignedUpUser instanceof NormalUser ||(content.isExclusive() && userController.lastSignedUpUser instanceof PremiumUser) ||
                    (content.isExclusive() && (content.getOwnerId() == userController.lastSignedUpUser.getId()))) {
                imageView.setOnMouseClicked(event ->
                        openContentPlayer(content));
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inaccessibility");
                alert.setHeaderText(null);
                alert.setContentText("this content is exclusive");
                imageView.setOnMouseClicked(event -> alert.showAndWait());
            }

        }else {

            if (!content.isExclusive() && userController.lastSignedUpUser instanceof NormalUser ||(content.isExclusive() && userController.lastSignedUpUser instanceof PremiumUser) ||
                    (content.isExclusive() && (content.getOwnerId() == userController.lastSignedUpUser.getId()))){
                imageView.setOnMouseClicked(event ->
                        openAudioPlayer(content));
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inaccessibility");
                alert.setHeaderText(null);
                alert.setContentText("this content is exclusive");
                imageView.setOnMouseClicked(event -> alert.showAndWait());
            }

        }


        return imageView;
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


    private String fixPath(String originalPath) {
        if (originalPath.contains("resources/")) {
            return originalPath.substring(originalPath.indexOf("resources/") + 10);
        }
        return originalPath;
    }
}
