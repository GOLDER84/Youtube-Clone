package com.example.youtube_graphic;
import Model.Category;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import Controller.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseCategory {
    private Stage stage;
    private final ArrayList<String> selectedCategories = new ArrayList<>();

    private void handleCategorySelection(CheckBox checkBox, String categoryName) {
        if (checkBox.isSelected()) {
            if (selectedCategories.size() < 4) {
                selectedCategories.add(categoryName);
            } else {
                checkBox.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Limit Exceeded");
                alert.setHeaderText(null);
                alert.setContentText("You can select up to 4 categories only!");
                alert.showAndWait();
            }
        } else {
            selectedCategories.remove(categoryName);
        }
    }

    @FXML
    private CheckBox gameChoice;

    @FXML
    private CheckBox historyChoice;

    @FXML
    private CheckBox liveChoice;

    @FXML
    private CheckBox musicChoice;

    @FXML
    private CheckBox newsChoice;

    @FXML
    private Button okBtn;

    @FXML
    private CheckBox podcastChoice;

    @FXML
    private CheckBox societyChoice;

    @FXML
    void gameClicked(MouseEvent event) {
        handleCategorySelection(gameChoice, "GAME");
    }

    @FXML
    void historyClicked(MouseEvent event) {
        handleCategorySelection(historyChoice, "HISTORY");
    }

    @FXML
    void liveClicked(MouseEvent event) {
        handleCategorySelection(liveChoice, "LIVE");
    }

    @FXML
    void musicClicked(MouseEvent event) {
        handleCategorySelection(musicChoice, "MUSIC");
    }

    @FXML
    void newsClicked(MouseEvent event) {
        handleCategorySelection(newsChoice, "NEWS");
    }

    @FXML
    void podcastClicked(MouseEvent event) {
        handleCategorySelection(podcastChoice, "PODCAST");
    }

    @FXML
    void societyClicked(MouseEvent event) {
        handleCategorySelection(societyChoice, "SOCIETY");
    }
    @FXML
    void okClicked(MouseEvent event) throws IOException {
        if (selectedCategories.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one category.");
            alert.showAndWait();
            return;
        }
        ArrayList<Category> chosenCategories = new ArrayList<>();
        for (String name : selectedCategories) {
            try {
                Category category = Category.valueOf(name.toUpperCase());
                chosenCategories.add(category);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category name: " + name);
            }
        }

        UserController userController = UserController.getInstance();
        String result = userController.chooseFavouriteCategory(chosenCategories);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category Selection");
        alert.setHeaderText(null);
        alert.setContentText(result);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            this.stage = HelloApplication.primaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

}
