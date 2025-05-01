package com.example.youtube_graphic;

import Controller.ContentController;
import Controller.DatabaseController;
import Controller.UserController;
import Model.Category;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static BasePageController basePageController;
    UserController userController = UserController.getInstance();
    DatabaseController databaseController = DatabaseController.getInstance();
    ContentController contentController = ContentController.getInstance();
    static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        userController.createAccount("Aref" , "ArefZargar.123" , "ArefZargar" , "aref.g@gmail.com" , "09139230918" , "src/main/resources/Images/ArefCover.jpg");

        userController.login("Aref", "ArefZargar.123");

        userController.createChannel("Gaming with Mike" , "I’m Mike, your guide to the exciting world of gaming." , "src/main/resources/Images/Channel1cover.jpg");
        contentController.createShortVideo("Mortal" , "N" , "Brutality" , "00:16" , "Game" , "src/main/resources/Contents/Mortal Kombat_GAME.mp4" , "src/main/resources/Images/Mortal-Cover.jpg" , "English" , "War music");
        contentController.createPodcast("Shab 2" , "Amir Tataloo" , "08:08" , "MUSIC" , "src/main/resources/Contents/Amir Tataloo - Shab 2.mp3" , "src/main/resources/Images/Amir Tataloo - Shab 2.jpg" , "Y");
        userController.lastSignedUpUser.getFavoriteCategories().add(Category.GAME);
        userController.logout();

        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUpOrLogin.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}