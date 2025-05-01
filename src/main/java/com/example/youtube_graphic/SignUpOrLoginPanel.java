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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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

//    @FXML
//    void loginClicked(MouseEvent event) throws IOException {
//        String username = enterUsername.getText();
//        String password = enterPassword.getText();
//        String result = userController.login(username, password);
//        if (username.isEmpty() || password.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning");
//            alert.setContentText("Fields cannot be empty");
//            alert.showAndWait();
//        }
//        else if (!(result.contains("Logged in successfully"))){
//            loginMassage.setText(result);
//        }else {
//            this.stage = HelloApplication.primaryStage;
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
//            Parent root = fxmlLoader.load();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        }
//    }
//    @FXML
//    void loginClicked(MouseEvent event) throws IOException {
//        String username = enterUsername.getText();
//        String password = enterPassword.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning");
//            alert.setContentText("Fields cannot be empty");
//            alert.showAndWait();
//            return;
//        }
//
//        AdminController adminController = AdminController.getInstance();
//        String adminResult = adminController.login(username, password);
//
//        if (adminResult.equals("Admin logged in.")) {
//            this.stage = HelloApplication.primaryStage;
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//            return;
//        }
//        String result = userController.login(username, password);
//        if (!(result.contains("Logged in successfully"))) {
//            loginMassage.setText(result);
//        } else {
//            this.stage = HelloApplication.primaryStage;
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
//            Parent root = loader.load();
//            HelloApplication.basePageController = loader.getController();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        }
//    }
//@FXML
//void loginClicked(MouseEvent event) throws IOException {
//    String username = enterUsername.getText();
//    String password = enterPassword.getText();
//
//    if (username.isEmpty() || password.isEmpty()) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle("Warning");
//        alert.setContentText("Fields cannot be empty");
//        alert.showAndWait();
//        return;
//    }
//
//    AdminController adminController = AdminController.getInstance();
//    String adminResult = adminController.login(username, password);
//
//    if (adminResult.equals("Admin logged in.")) {
//        this.stage = HelloApplication.primaryStage;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//        return;
//    }
//
//    String result = userController.login(username, password);
//    if (!(result.contains("Logged in successfully"))) {
//        loginMassage.setText(result);
//    } else {
//        this.stage = HelloApplication.primaryStage;
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
//        Parent root = loader.load(); // اول load
//        HelloApplication.basePageController = loader.getController(); // بعد getController
//
//        FXMLLoader innerLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
//        Parent innerRoot = innerLoader.load();
//        HelloApplication.basePageController.setMainContent(innerRoot);
//
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//}

    @FXML
    void loginClicked(MouseEvent event) throws IOException {
        String username = enterUsername.getText();
        String password = enterPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
            return;
        }

        AdminController adminController = AdminController.getInstance();
        String adminResult = adminController.login(username, password);

        if (adminResult.equals("Admin logged in.")) {
            this.stage = HelloApplication.primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }

        String result = userController.login(username, password);
        if (!(result.contains("Logged in successfully"))) {
            loginMassage.setText(result);
        } else {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("BasePage.fxml"));
            Parent baseRoot = baseLoader.load();
            HelloApplication.basePageController = baseLoader.getController();

            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent homeRoot = homeLoader.load();
            HelloApplication.basePageController.setMainContent(homeRoot);

            this.stage = HelloApplication.primaryStage;
            Scene scene = new Scene(baseRoot);
            stage.setScene(scene);
            stage.show();
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
    private void chooseProfileCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(HelloApplication.primaryStage);
        if (selectedFile != null) {
            enterProfileCover.setText(selectedFile.getAbsolutePath());
        }
    }

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
            userController.login(enterUsername2.getText(), enterPassword2.getText());
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