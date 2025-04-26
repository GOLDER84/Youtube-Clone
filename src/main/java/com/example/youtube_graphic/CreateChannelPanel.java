package com.example.youtube_graphic;

import Controller.DatabaseController;
import Controller.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateChannelPanel {
    DatabaseController databaseController = DatabaseController.getInstance();
    UserController userController = UserController.getInstance();
    private Stage stage;

    @FXML
    private TextField ChannelCoverTxt;

    @FXML
    private Button backBtn;

    @FXML
    private TextField channelDescriptionTxt;

    @FXML
    private TextField channelNameTxt;

    @FXML
    private Button creatChannelBtn;

    @FXML
    private Label createChannelMassage;

    @FXML
    void backClicked(MouseEvent event) throws IOException {
        this.stage = HelloApplication.primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Library.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void createChannelClicked(MouseEvent event) throws IOException {
        String channelName = channelNameTxt.getText();
        String channelDescription = channelDescriptionTxt.getText();
        String channelCover = ChannelCoverTxt.getText();
        String result = userController.createChannel(channelName, channelDescription, channelCover);
        if (!result.contains("successfully")){
            createChannelMassage.setText(result);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(result);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                this.stage = HelloApplication.primaryStage;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserChannel.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

}
