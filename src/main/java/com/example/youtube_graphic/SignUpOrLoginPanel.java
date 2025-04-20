package com.example.youtube_graphic;

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
import java.util.ResourceBundle;

public class SignUpOrLoginPanel implements Initializable {
    private  Scene scene;
    private Stage stage;
    UserController userController = UserController.getInstance();
    @FXML
    private Label SignupBtn;

    @FXML
    private PasswordField enterPassword;

    @FXML
    private TextField enterUsername;

    @FXML
    private Button loginBtn;

    @FXML
    private Label loginMassage;

    @FXML
    void loginClicked(MouseEvent event) throws IOException {
        String username = enterUsername.getText();
        String password = enterPassword.getText();
        String result = userController.login(username, password);
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        }
        else if (!(result.contains("Logged in successfully"))){
            loginMassage.setText(result);
        }else {

        }
    }

    @FXML
    private ImageView yuimage;

    @FXML
    void signupClicked(MouseEvent event) throws IOException {
        this.stage = HelloApplication.primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = HelloApplication.primaryStage;
        Animation animation = new Timeline();
        if (yuimage != null) {
            yuimage.setOnMouseEntered(event -> {
                yuimage.setEffect(new Bloom(100));
                yuimage.setEffect(new BoxBlur());
            });

        }
    }

//    @FXML
//    void dragDetected(MouseEvent event) {
//        yuimage.setEffect(new Bloom(100));
//        yuimage.setEffect(new BoxBlur());
//    }

    @FXML
    void dragDetected2(MouseEvent event) {
        yuimage.setEffect(null);
    }
    @FXML
    public void dragDetected(MouseEvent mouseEvent) {

    }
    //برای بعدیع
    @FXML
    private Button SignupBtn2;

    @FXML
    private Label SignupMassage;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField enterEmail;

    @FXML
    private TextField enterFullname;

    @FXML
    private PasswordField enterPassword2;

    @FXML
    private TextField enterPhonenumber;

    @FXML
    private TextField enterProfileCover;

    @FXML
    private TextField enterUsername2;

    @FXML
    void cancelClicked(MouseEvent event) throws IOException {
        this.stage = HelloApplication.primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUpOrLogin.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void signup2Clicked(MouseEvent event) throws IOException {
        String result =userController.createAccount(enterUsername2.getText() , enterPassword2.getText() , enterFullname.getText() , enterEmail.getText() , enterPhonenumber.getText(), enterProfileCover.getText());
        if (!result.contains("Account created successfully")) {
            SignupMassage.setText(result);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Signup");
            alert.setContentText("Account created successfully");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                this.stage = HelloApplication.primaryStage;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FavotiteCategory.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

}